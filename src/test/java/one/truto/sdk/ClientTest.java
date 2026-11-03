package one.truto.sdk;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import one.truto.sdk.models.Documentation;
import one.truto.sdk.models.EnvironmentIntegrationDeleteResponse;
import one.truto.sdk.models.IntegratedAccountResponse;
import one.truto.sdk.models.IntegratedAccountStatus;
import one.truto.sdk.models.Log;
import one.truto.sdk.models.Ping;
import one.truto.sdk.models.TenantCreateRequest;
import one.truto.sdk.models.TenantResponse;
import one.truto.sdk.models.TrutoRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientTest {
    private MockServer server;
    private TrutoClient client;

    @BeforeEach
    void setUp() throws Exception {
        server = new MockServer();
        client = TrutoClient.builder().token("tok_123").baseUrl(server.url() + "/").build();
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void sendsAuthAcceptAndUserAgentHeaders() {
        server.json(200, "{\"message\":\"pong\"}");
        Ping ping = client.ping();
        assertEquals("pong", ping.getMessage());
        MockServer.Recorded r = server.last();
        assertEquals("GET", r.method);
        assertEquals("/ping", r.rawPath);
        assertNull(r.rawQuery);
        assertEquals("Bearer tok_123", r.header("Authorization"));
        assertEquals("application/json", r.header("Accept"));
        assertEquals("truto-java-sdk/" + TrutoClient.VERSION, r.header("User-Agent"));
        assertNull(r.header("Content-Type"));
    }

    @Test
    void getWithQueryAndEncodedPathId() {
        server.json(200, "{\"id\":\"acme/eu\",\"name\":\"Acme\"}");
        TenantResponse t = client.tenants().get("acme/eu", Query.of("include", "stats").with("limit", 5), null);
        assertEquals("Acme", t.getName());
        MockServer.Recorded r = server.last();
        assertEquals("/tenant/acme%2Feu", r.rawPath);
        assertEquals("include=stats&limit=5", r.rawQuery);
    }

    @Test
    void postSendsJsonBody() throws Exception {
        server.json(201, "{\"id\":\"t1\",\"name\":\"Tenant One\"}");
        TenantResponse t = client.tenants().create(new TenantCreateRequest().id("t1").name("Tenant One"));
        assertEquals("t1", t.getId());
        MockServer.Recorded r = server.last();
        assertEquals("POST", r.method);
        assertEquals("/tenant", r.rawPath);
        assertEquals("application/json", r.header("Content-Type"));
        JsonNode body = Json.mapper().readTree(r.body);
        assertEquals("t1", body.get("id").asText());
        assertEquals("Tenant One", body.get("name").asText());
        assertFalse(body.has("metadata"), "unset optional fields are omitted");
    }

    @Test
    void noContentGivesEmptyResult() {
        server.enqueue(new MockServer.Reply(204, null, null));
        Documentation none = client.documentation().get("d1");
        assertNull(none);
        server.enqueue(new MockServer.Reply(204, null, null));
        TrutoRecord rec = client.unifiedApi().delete("crm", "contacts", "ia_1", "c1");
        assertTrue(rec.isEmpty());
    }

    @Test
    void nonJsonBodyIsReturnedAsRawBytes() {
        server.enqueue(new MockServer.Reply(200, "application/pdf", "%PDF-1.4 fake"));
        RawResponse raw = client.customApi().requestRaw("GET", "/files/report 1.pdf", "ia_1", null, null, null);
        assertArrayEquals("%PDF-1.4 fake".getBytes(StandardCharsets.UTF_8), raw.body());
        assertEquals("application/pdf", raw.contentType());
        MockServer.Recorded r = server.last();
        assertEquals("/custom/files/report%201.pdf", r.rawPath);
        assertEquals("integrated_account_id=ia_1", r.rawQuery);

        server.enqueue(new MockServer.Reply(200, "text/plain", "hello"));
        assertThrows(TrutoDecodeException.class, () -> client.customApi().get("files", "ia_1"));
    }

    @Test
    void notFoundRaisesErrorWithParsedBody() {
        server.json(404, "{\"message\":\"Integrated account not found\"}");
        TrutoException e = assertThrows(TrutoException.class, () -> client.integratedAccounts().get("missing"));
        assertEquals(404, e.getStatusCode());
        assertEquals("Not Found", e.getStatusText());
        assertTrue(e.isNotFound());
        assertFalse(e.isRateLimited());
        assertFalse(e.isUnauthorized());
        assertEquals("Integrated account not found", e.getJsonBody().get("message").asText());
        assertTrue(e.getMessage().contains("Integrated account not found"));
        assertTrue(e.getHeaders().containsKey("content-type"));
        assertEquals(1, server.requests().size(), "404 is not retried");
    }

    @Test
    void retriesOn503AndCallsOnRetry() {
        server.json(503, "{\"error\":\"busy\"}");
        server.json(200, "{\"message\":\"pong\"}");
        List<int[]> calls = new ArrayList<>();
        Ping p = client.ping(null, RequestOptions.builder().onRetry((n, s) -> calls.add(new int[] {n, s})).build());
        assertEquals("pong", p.getMessage());
        assertEquals(2, server.requests().size());
        assertEquals(1, calls.size());
        assertArrayEquals(new int[] {1, 503}, calls.get(0));
    }

    @Test
    void waitsRetryAfterOn429AndCallsOnRateLimited() {
        server.enqueue(new MockServer.Reply(429, "application/json", "{}").header("Retry-After", "1"));
        server.json(200, "{\"message\":\"pong\"}");
        List<long[]> limited = new ArrayList<>();
        List<int[]> retried = new ArrayList<>();
        TrutoClient c = TrutoClient.builder().token("t").baseUrl(server.url())
                .onRateLimited((after, n) -> limited.add(new long[] {after, n}))
                .onRetry((n, s) -> retried.add(new int[] {n, s}))
                .build();
        long start = System.nanoTime();
        c.ping();
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsedMs >= 900, "waited for Retry-After, took " + elapsedMs + "ms");
        assertEquals(1, limited.size());
        assertArrayEquals(new long[] {1, 0}, limited.get(0));
        assertArrayEquals(new int[] {1, 429}, retried.get(0));
        assertEquals(2, server.requests().size());
    }

    @Test
    void raisesLastErrorWhenRetriesAreExhausted() {
        for (int i = 0; i < 3; i++) {
            server.json(502, "{\"error\":\"bad gateway " + i + "\"}");
        }
        TrutoClient c = TrutoClient.builder().token("t").baseUrl(server.url()).maxRetries(2).build();
        TrutoException e = assertThrows(TrutoException.class, c::ping);
        assertEquals(502, e.getStatusCode());
        assertEquals("bad gateway 2", e.getJsonBody().get("error").asText());
        assertEquals(3, server.requests().size());
    }

    @Test
    void noRetryDisablesRetries() {
        server.json(503, "{}");
        server.json(200, "{\"message\":\"pong\"}");
        TrutoException e = assertThrows(TrutoException.class,
                () -> client.ping(null, RequestOptions.builder().noRetry().build()));
        assertEquals(503, e.getStatusCode());
        assertEquals(1, server.requests().size());
    }

    @Test
    void extraRetryStatusCodes() {
        server.json(500, "{}");
        server.json(200, "{\"message\":\"pong\"}");
        Ping p = client.ping(null, RequestOptions.builder().retryStatusCodes(500).build());
        assertEquals("pong", p.getMessage());
        assertEquals(2, server.requests().size());
    }

    @Test
    void timeoutRaisesTimeoutErrorAndIsNotRetried() {
        server.enqueue(new MockServer.Reply(200, "application/json", "{\"message\":\"late\"}").delay(1500));
        TrutoClient c = TrutoClient.builder().token("t").baseUrl(server.url())
                .timeout(Duration.ofMillis(200)).build();
        TrutoTimeoutException e = assertThrows(TrutoTimeoutException.class, c::ping);
        assertEquals(408, e.getStatusCode());
        assertEquals(1, server.requests().size());
    }

    @Test
    void connectionErrorIsATransportError() throws Exception {
        int port;
        try (ServerSocket s = new ServerSocket(0)) {
            port = s.getLocalPort();
        }
        TrutoClient c = TrutoClient.builder().token("t").baseUrl("http://127.0.0.1:" + port).maxRetries(0).build();
        TrutoTransportException e = assertThrows(TrutoTransportException.class, c::ping);
        assertEquals(0, e.getStatusCode());
    }

    @Test
    void paginatesAcrossThreePages() {
        server.json(200, "{\"result\":[{\"id\":\"a\"},{\"id\":\"b\"}],\"next_cursor\":\"c2\"}");
        server.json(200, "{\"result\":[],\"next_cursor\":\"c3\"}");
        server.json(200, "{\"results\":[{\"id\":\"c\"}],\"next_cursor\":null}");
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("limit", 2);
        query.put("next_cursor", "");
        Map<String, Object> before = new LinkedHashMap<>(query);

        List<IntegratedAccountResponse> all = client.integratedAccounts().list(query).toList();

        assertEquals(3, all.size());
        assertEquals("c", all.get(2).getId());
        assertEquals(before, query, "caller query not mutated");
        List<MockServer.Recorded> reqs = server.requests();
        assertEquals(3, reqs.size());
        assertEquals("/integrated-account", reqs.get(0).rawPath);
        assertEquals("limit=2", reqs.get(0).rawQuery);
        assertEquals("limit=2&next_cursor=c2", reqs.get(1).rawQuery);
        assertEquals("limit=2&next_cursor=c3", reqs.get(2).rawQuery);
    }

    @Test
    void pagerNextPageAndFirst() {
        server.json(200, "{\"result\":[{\"id\":\"a\"}],\"next_cursor\":\"x\"}");
        server.json(200, "{\"result\":[{\"id\":\"b\"}]}");
        Pager<TrutoRecord> pager = client.users().list();
        Page<TrutoRecord> p1 = pager.nextPage();
        assertEquals("x", p1.nextCursor());
        assertTrue(pager.hasNextPage());
        Page<TrutoRecord> p2 = pager.nextPage();
        assertEquals("b", p2.items().get(0).getString("id"));
        assertFalse(pager.hasNextPage());

        server.json(200, "{\"result\":[{\"id\":\"first\"}],\"next_cursor\":\"more\"}");
        assertEquals("first", client.users().list().first().get().getString("id"));
        assertEquals(3, server.requests().size(), "first() fetches only one page");
    }

    @Test
    void decodesRealisticIntegratedAccountLeniently() throws Exception {
        String json = "{\"id\":\"8a1b\",\"tenant_id\":\"acme\",\"environment_integration_id\":\"ei_1\","
                + "\"context\":{\"instance_url\":\"https://x\"},\"status\":\"quarantined\","
                + "\"created_at\":\"2026-01-02 03:04:05\",\"is_sandbox\":false,"
                + "\"brand_new_field\":{\"nested\":[1,2,3]},"
                + "\"integration\":{\"name\":\"hubspot\",\"unknown_too\":true}}";
        server.json(200, json);
        IntegratedAccountResponse a = client.integratedAccounts().get("8a1b");
        assertEquals("acme", a.getTenantId());
        assertEquals("quarantined", a.getStatus().value());
        assertFalse(a.getStatus().isKnown());
        assertEquals("2026-01-02 03:04:05", a.getCreatedAt());
        assertEquals("https://x", a.getContext().get("instance_url").asText());
        assertEquals("hubspot", a.getIntegration().getName());
        assertEquals(IntegratedAccountStatus.ACTIVE, IntegratedAccountStatus.of("active"));

        JsonNode back = Json.toTree(a);
        assertEquals("quarantined", back.get("status").asText(), "unknown enum value round-trips");
        assertFalse(back.has("brand_new_field"));
    }

    @Test
    void unionAccessorDecodesVariant() {
        server.json(200, "{\"result\":[{\"log_type\":\"webhook\",\"timestamp\":\"t\",\"extra\":1}]}");
        List<Log> logs = client.logs().list(Query.of("log_type", "webhook")).toList();
        assertEquals("log_type=webhook", server.last().rawQuery);
        Log log = logs.get(0);
        assertEquals("webhook", log.discriminator());
        assertEquals(1, log.raw().get("extra").asInt());

        one.truto.sdk.models.IntegrationPagination pg = Json.fromTree(
                Json.mapper().createObjectNode().put("format", "page")
                        .set("config", Json.mapper().createObjectNode().put("page_key", "p")),
                one.truto.sdk.models.IntegrationPagination.class);
        assertEquals("page", pg.discriminator());
        assertEquals("p", pg.asIntegrationPagePagination().getConfig().getPageKey());
        one.truto.sdk.models.IntegrationPagination rebuilt =
                one.truto.sdk.models.IntegrationPagination.of(pg.asIntegrationPagePagination());
        assertEquals(pg, rebuilt);
    }

    @Test
    void unifiedListReturnsRecords() {
        server.json(200, "{\"result\":[{\"id\":\"c1\",\"first_name\":\"Ada\"}],\"next_cursor\":null}");
        List<TrutoRecord> contacts = client.unifiedApi().list("crm", "contacts", "ia_1",
                Query.of("limit", 10)).toList();
        assertEquals(1, contacts.size());
        assertEquals("Ada", contacts.get(0).getString("first_name"));
        assertEquals("/unified/crm/contacts", server.last().rawPath);
        assertEquals("integrated_account_id=ia_1&limit=10", server.last().rawQuery);
    }

    @Test
    void unifiedTypedDecodeAndAsync() {
        server.json(200, "{\"id\":\"c1\",\"first_name\":\"Ada\",\"ignored\":1}");
        Contact c = client.unifiedApi().get("crm", "contacts", "ia_1", "c1", null, null, Contact.class);
        assertEquals("Ada", c.firstName);
        assertEquals("/unified/crm/contacts/c1", server.last().rawPath);

        server.json(503, "{}");
        server.json(200, "{\"id\":\"c2\"}");
        TrutoRecord r = client.unifiedApi().getAsync("crm", "contacts", "ia_1", "c2", null, null).join();
        assertEquals("c2", r.getString("id"));
        assertEquals("c2", r.as(Contact.class).id);

        server.json(404, "{\"message\":\"nope\"}");
        CompletionException ce = assertThrows(CompletionException.class,
                () -> client.proxyApi().getAsync("tickets", "ia_1", "x", null, null).join());
        assertInstanceOf(TrutoException.class, ce.getCause());
        assertEquals(404, ((TrutoException) ce.getCause()).getStatusCode());
    }

    @Test
    void mcpPayloadShapes() throws Exception {
        server.json(200, "{\"jsonrpc\":\"2.0\",\"id\":null,\"result\":{}}");
        server.json(200, "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"tools\":[]}}");
        server.json(200, "{\"jsonrpc\":\"2.0\",\"id\":2,\"result\":{}}");
        client.mcp().initialize("mcp_tok", "my-app", "1.2.3");
        client.mcp().listTools("mcp_tok");
        client.mcp().callTool("mcp_tok", "list_contacts", Collections.singletonMap("limit", 5));
        List<MockServer.Recorded> reqs = server.requests();
        assertEquals("/mcp/mcp_tok", reqs.get(0).rawPath);
        assertEquals("{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"params\":{\"protocolVersion\":\"2024-11-05\","
                + "\"clientInfo\":{\"name\":\"my-app\",\"version\":\"1.2.3\"}}}", reqs.get(0).body);
        assertEquals("{\"jsonrpc\":\"2.0\",\"method\":\"tools/list\",\"id\":1}", reqs.get(1).body);
        assertEquals("{\"jsonrpc\":\"2.0\",\"method\":\"tools/call\",\"id\":2,\"params\":{\"name\":\"list_contacts\","
                + "\"arguments\":{\"limit\":5}}}", reqs.get(2).body);
    }

    @Test
    void integrationUninstallListsThenDeletesEach() {
        server.json(200, "{\"result\":[{\"id\":\"ei_1\"},{\"id\":\"ei_2\"}],\"next_cursor\":null}");
        server.json(200, "{\"id\":\"ei_1\"}");
        server.json(200, "{\"id\":\"ei_2\"}");
        List<EnvironmentIntegrationDeleteResponse> out = client.integrations().uninstall("int_9");
        assertEquals(2, out.size());
        List<MockServer.Recorded> reqs = server.requests();
        assertEquals("GET", reqs.get(0).method);
        assertEquals("/environment-integration", reqs.get(0).rawPath);
        assertEquals("integration_id=int_9", reqs.get(0).rawQuery);
        assertEquals("DELETE", reqs.get(1).method);
        assertEquals("/environment-integration/ei_1", reqs.get(1).rawPath);
        assertEquals("/environment-integration/ei_2", reqs.get(2).rawPath);
    }

    @Test
    void integrationInstallAndToolsQuery() throws Exception {
        server.json(201, "{\"id\":\"ei_1\",\"integration_id\":\"int_9\"}");
        assertNotNull(client.integrations().install("int_9"));
        JsonNode body = Json.mapper().readTree(server.last().body);
        assertEquals("int_9", body.get("integration_id").asText());
        assertTrue(body.get("is_enabled").asBoolean());

        server.json(200, "{}");
        client.integratedAccounts().tools("ia_1", java.util.Arrays.asList("list", "get"),
                java.util.Arrays.asList("crm"), null, null);
        assertEquals("/integrated-account/ia_1/tools", server.last().rawPath);
        assertEquals("methods%5B0%5D=list&methods%5B1%5D=get&tags%5B0%5D=crm", server.last().rawQuery);

        server.json(200, "{}");
        client.integratedAccounts().tools("ia_1", Collections.emptyList(), null, null, null);
        assertNull(server.last().rawQuery, "empty methods/tags are omitted");
    }

    @Test
    void sessionTokenRevokeSendsDeleteWithBody() {
        server.json(200, "{\"success\":true}");
        client.sessionTokens().revoke(new one.truto.sdk.models.SessionTokenDeleteRequest());
        assertEquals("DELETE", server.last().method);
        assertEquals("/session-token", server.last().rawPath);
        assertEquals("application/json", server.last().header("Content-Type"));
    }

    @Test
    void perRequestHeadersAndBuilderValidation() {
        server.json(200, "{\"message\":\"pong\"}");
        client.ping(null, RequestOptions.builder().header("X-Trace", "abc").build());
        assertEquals("abc", server.last().header("X-Trace"));
        assertThrows(IllegalArgumentException.class, () -> TrutoClient.builder().build());
        assertThrows(IllegalArgumentException.class, () -> client.integratedAccounts().get(""));
    }

    /** A caller-defined type for typed decode. */
    public static final class Contact {
        public String id;
        @com.fasterxml.jackson.annotation.JsonProperty("first_name")
        public String firstName;
    }
}
