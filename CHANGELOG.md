# Changelog

All notable changes to this project are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project uses
[Semantic Versioning](https://semver.org/).

## Unreleased

- Fix: listing the full integration catalog no longer throws `TrutoDecodeException`. `IntegrationResourceMethod.getBody()` and `EnvironmentIntegrationResourceMethod.getBody()` now return a `JsonNode` instead of a `Map`, because some integrations (greythr) send a JSON array there.
- Fixes to the published spec now live in `spec/overlay.json` (OpenAPI Overlay), applied when regenerating models.

## [0.1.0] - 2026-11-03

### Added

- `TrutoClient` with a builder (token, base URL, timeout, retries, default
  Retry-After, custom `HttpClient`, retry and rate-limit hooks). Immutable and
  thread-safe; built on `java.net.http.HttpClient`.
- Retries on 408, 409, 425, 429, 502, 503 and 504 and on connection errors,
  honouring `Retry-After` for 429 and using exponential backoff with full
  jitter otherwise. Per-request overrides through `RequestOptions`.
- `TrutoException` with status, status text, parsed body and headers, plus
  `TrutoTimeoutException`, `TrutoTransportException` and
  `TrutoDecodeException`.
- Cursor pagination through `Pager<T>` (lazy `Iterable`, `stream()`,
  `toList()`, `first()`, `nextPage()`, `pages()`, `map()`).
- Query strings encoded exactly like the TypeScript SDK (`qs.stringify` with
  sorted keys), with a `Query` builder.
- Resources for the admin API (integrated accounts, integrations, tenants,
  sync jobs, workflows, webhooks, datastores, logs, analytics and more) and
  the data-plane APIs (unified, proxy, custom, MCP), with `...Async` variants
  returning `CompletableFuture` for the unified, proxy and custom APIs.
- 475 model classes generated from Truto's OpenAPI spec, with lenient
  decoding, open enums and union wrappers.
