# Truto Java SDK

Java client for the [Truto](https://truto.one) REST API: integrated accounts,
integrations, sync jobs and the rest of the admin API, plus the unified,
proxy, custom and MCP data-plane APIs.

- Java 11+, one runtime dependency (Jackson databind), HTTP via `java.net.http`.
- Immutable, thread-safe client with retries, timeouts and cursor pagination.
- Typed models generated from Truto's OpenAPI spec; dynamic `TrutoRecord`s
  for integration-specific payloads.

## Install

Maven:

```xml
<dependency>
  <groupId>one.truto</groupId>
  <artifactId>truto-java-sdk</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle:

```kotlin
implementation("one.truto:truto-java-sdk:0.1.0")
```

Requires Java 11 or later. The only runtime dependency is Jackson.

## Quick start

```java
import one.truto.sdk.TrutoClient;
import one.truto.sdk.models.IntegratedAccountResponse;

TrutoClient truto = TrutoClient.builder()
    .token(System.getenv("TRUTO_API_TOKEN"))
    .build();

for (IntegratedAccountResponse account : truto.integratedAccounts().list()) {
    System.out.println(account.getId() + " " + account.getStatus());
}
```

Create one client and share it; it is safe to use from many threads.

### Client options

```java
TrutoClient truto = TrutoClient.builder()
    .token(token)                              // required
    .baseUrl("https://api.truto.one")          // default
    .timeout(Duration.ofSeconds(30))           // default, per request
    .maxRetries(3)                             // default
    .defaultRetryAfter(Duration.ofSeconds(10)) // 429 without Retry-After
    .httpClient(myHttpClient)                  // optional custom java.net.http.HttpClient
    .onRetry((retry, status) -> log.warn("retry {} after {}", retry, status))
    .onRateLimited((seconds, retries) -> log.warn("rate limited for {}s", seconds))
    .build();
```

## Resources

Each resource is an accessor on the client: `truto.integratedAccounts()`,
`truto.integrations()`, `truto.tenants()`, `truto.syncJobs()`,
`truto.webhooks()`, `truto.logs()`, `truto.analytics()`, ... Standard methods
are `list`, `get`, `create`, `update` (PATCH) and `delete`; resources add their
own (`integratedAccounts().refreshCredentials(id)`,
`integrations().install(id)`, `syncJobCronTriggers().schedule(id)`, ...).

Every method has a short form and a long form taking a query map and
`RequestOptions`:

```java
truto.integratedAccounts().get(id);
truto.integratedAccounts().get(id, Query.of("expand", "integration"), RequestOptions.builder().maxRetries(0).build());
```

Query maps are plain `Map<String, Object>`; `Query` is a small builder that is
itself a map. Nested maps and lists are encoded the way Truto expects
(`filter[status]=open`, `ids[0]=a`).

`truto.logs().list(...)` needs a `log_type` query parameter (the server rejects
requests without it).

## Pagination

`list` methods return a `Pager<T>`. Nothing is fetched until you use it.

```java
Pager<IntegratedAccountResponse> accounts = truto.integratedAccounts().list(Query.of("limit", 100));

for (IntegratedAccountResponse a : accounts) { ... }        // lazy, all pages
List<IntegratedAccountResponse> all = accounts.toList();   // everything
Optional<IntegratedAccountResponse> one = accounts.first();
accounts.stream().filter(...).limit(10).forEach(...);       // lazy stream

while (accounts.hasNextPage()) {                            // page by page
    Page<IntegratedAccountResponse> page = accounts.nextPage();
    page.items(); page.nextCursor();
}
```

Your query map is copied, never modified.

## Unified API with pagination

```java
Pager<TrutoRecord> contacts = truto.unifiedApi()
    .list("crm", "contacts", integratedAccountId, Query.of("limit", 50));

for (TrutoRecord contact : contacts) {
    System.out.println(contact.getString("first_name"));
}

TrutoRecord created = truto.unifiedApi().create("crm", "contacts", integratedAccountId,
    new TrutoRecord().with("first_name", "Ada").with("last_name", "Lovelace"));
```

The integrated account ID is sent as `integrated_account_id`. Also available:
`get`, `update`, `delete`, `customMethod`, `meta`, `modelMeta`, and async
variants (`getAsync`, `createAsync`, `updateAsync`, `deleteAsync`,
`customMethodAsync`, `listPageAsync`) that return `CompletableFuture`.

## Proxy and custom APIs

```java
TrutoRecord ticket = truto.proxyApi().get("tickets", integratedAccountId, "123");

TrutoRecord result = truto.customApi().post("reports/run", integratedAccountId, Map.of("year", 2026));

// Non-JSON responses (files): use the raw variant.
RawResponse file = truto.customApi().requestRaw("GET", "files/42/download", integratedAccountId, null, null, null);
byte[] bytes = file.body();
```

`customApi().request(method, path, accountId, body, query, options, JsonNode.class)`
handles endpoints that return a JSON array.

## Typed decode

Data-plane calls return `TrutoRecord` (a `Map<String, Object>`). Decode into
your own types with a `Class<T>` overload, `TrutoRecord.as`, or `Pager.map`:

```java
public class Contact {
    public String id;
    @JsonProperty("first_name") public String firstName;
}

Contact c = truto.unifiedApi().get("crm", "contacts", accountId, "c1", null, null, Contact.class);
Pager<Contact> all = truto.unifiedApi().list("crm", "contacts", accountId, null, null, Contact.class);
Contact again = record.as(Contact.class);
```

Generated models decode leniently: unknown fields are ignored, missing fields
stay `null`, and enum values the SDK does not know are kept (`status.value()`,
`status.isKnown()`). Union types (`IntegrationPagination`, `Log`, ...) wrap
the raw JSON and decode on demand: `pagination.asIntegrationPagePagination()`.

## Errors

Every failure is a `TrutoException` (unchecked):

```java
try {
    truto.integratedAccounts().get("missing");
} catch (TrutoTimeoutException e) {        // status 408, never retried
} catch (TrutoTransportException e) {      // no response (connection refused, ...)
} catch (TrutoDecodeException e) {         // 2xx body did not fit the type
} catch (TrutoException e) {
    e.getStatusCode();  e.getStatusText();
    e.getBody();        // JsonNode for JSON bodies, String otherwise
    e.getJsonBody();    e.getHeaders();
    e.isNotFound();     e.isRateLimited();  e.isUnauthorized();
}
```

Async methods complete exceptionally with the same exceptions (wrapped in
`CompletionException` by `join()`).

## Retries and timeouts

Requests are retried on 408, 409, 425, 429, 502, 503 and 504 and on
connection errors, never on timeouts. A 429 waits for `Retry-After` seconds
(or the default retry-after); other statuses use exponential backoff with full
jitter (500 ms base, 10 s cap). Per request:

```java
RequestOptions opts = RequestOptions.builder()
    .timeout(Duration.ofSeconds(5))
    .maxRetries(1)
    .retryStatusCodes(500)          // in addition to the defaults
    .noRetry()                      // or disable retries entirely
    .header("X-Request-Id", "abc")
    .onRetry((retry, status) -> {})
    .onRateLimited((seconds, retries) -> {})
    .build();
```

Cancelling an async future stops any further retries.

## Endpoints the SDK does not wrap

```java
TrutoRecord r = truto.http().request("GET", "some/new-endpoint", query, null, null, TrutoRecord.class);
```

## Regenerating models

Models in `src/main/java/one/truto/sdk/models` are generated from
`spec/openapi.json` (files with a `DO NOT EDIT` header; `TrutoRecord` is
hand-written). After updating the spec:

```bash
python3 codegen/normalize_spec.py spec/openapi.json spec/overlay.json > codegen/models.json
python3 codegen/generate_java.py codegen/models.json src/main/java/one/truto/sdk/models
mvn verify
```

Where the published spec doesn't match what the API returns, the fix lives
in `spec/overlay.json`, an [OpenAPI Overlay](https://spec.openapis.org/overlay/v1.0.0)
applied before generation. For example, a resource method's `body` is a
`JsonNode`, because some integrations send a JSON array there.

Both scripts need only the Python 3 standard library. Generated files for
models that disappear from the spec are deleted.

## Development

```bash
mvn verify      # compiles with -Xlint:all and runs the tests (no network)
```

Examples live in `examples/`; they are plain Java files using the SDK.

## License

MIT, see [LICENSE](LICENSE).
