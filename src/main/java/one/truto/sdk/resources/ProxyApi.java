package one.truto.sdk.resources;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import one.truto.sdk.ApiClient;
import one.truto.sdk.Page;
import one.truto.sdk.Pager;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/**
 * Proxy API: the integration's native resources, passed through with Truto's auth and
 * pagination. Every call takes the resource name and the integrated account ID (sent as the
 * {@code integrated_account_id} query parameter).
 */
public class ProxyApi extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public ProxyApi(ApiClient api) {
        super(api, "proxy");
    }

    static Map<String, Object> withAccount(String integratedAccountId, Map<String, ?> query) {
        if (integratedAccountId == null || integratedAccountId.isEmpty()) {
            throw new IllegalArgumentException("integratedAccountId must not be null or empty");
        }
        Map<String, Object> q = new LinkedHashMap<>();
        if (query != null) {
            q.putAll(query);
        }
        q.put("integrated_account_id", integratedAccountId);
        return q;
    }

    /**
     * Lists records.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @return a pager over all records
     */
    public Pager<TrutoRecord> list(String resource, String integratedAccountId) {
        return list(resource, integratedAccountId, null, null, TrutoRecord.class);
    }

    /**
     * Lists records.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters (filters, {@code limit}, ...), may be null
     * @return a pager over all records
     */
    public Pager<TrutoRecord> list(String resource, String integratedAccountId, Map<String, ?> query) {
        return list(resource, integratedAccountId, query, null, TrutoRecord.class);
    }

    /**
     * Lists records.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a pager over all records
     */
    public Pager<TrutoRecord> list(String resource, String integratedAccountId, Map<String, ?> query, RequestOptions options) {
        return list(resource, integratedAccountId, query, options, TrutoRecord.class);
    }

    /**
     * Lists records. Items are decoded into {@code type}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the item type
     * @param <T> the item type
     * @return a pager over all items
     */
    public <T> Pager<T> list(String resource, String integratedAccountId, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return pager(ApiClient.path("proxy", resource), withAccount(integratedAccountId, query), options, type);
    }

    /**
     * Fetches a single page asynchronously. Pass {@code next_cursor} in the query for later pages.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the page
     */
    public CompletableFuture<Page<TrutoRecord>> listPageAsync(String resource, String integratedAccountId, Map<String, ?> query, RequestOptions options) {
        return api.pageAsync(ApiClient.path("proxy", resource), withAccount(integratedAccountId, query), options, TrutoRecord.class);
    }

    /**
     * Fetches one record.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @return the response record
     */
    public TrutoRecord get(String resource, String integratedAccountId, String id) {
        return get(resource, integratedAccountId, id, null, null, TrutoRecord.class);
    }

    /**
     * Fetches one record.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord get(String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return get(resource, integratedAccountId, id, query, options, TrutoRecord.class);
    }

    /**
     * Fetches one record. The response is decoded into {@code type}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T get(String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("GET", ApiClient.path("proxy", resource, id), withAccount(integratedAccountId, query), null, options, type);
    }

    /**
     * Async form of {@code get}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> getAsync(String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("GET", ApiClient.path("proxy", resource, id), withAccount(integratedAccountId, query), null, options,
                TrutoRecord.class);
    }

    /**
     * Creates a record.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @return the response record
     */
    public TrutoRecord create(String resource, String integratedAccountId, Object body) {
        return create(resource, integratedAccountId, body, null, null, TrutoRecord.class);
    }

    /**
     * Creates a record.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord create(String resource, String integratedAccountId, Object body, Map<String, ?> query, RequestOptions options) {
        return create(resource, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * Creates a record. The response is decoded into {@code type}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T create(String resource, String integratedAccountId, Object body, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("POST", ApiClient.path("proxy", resource), withAccount(integratedAccountId, query), body(body), options, type);
    }

    /**
     * Async form of {@code create}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> createAsync(String resource, String integratedAccountId, Object body, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("POST", ApiClient.path("proxy", resource), withAccount(integratedAccountId, query), body(body), options,
                TrutoRecord.class);
    }

    /**
     * Updates a record (PATCH).
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @return the response record
     */
    public TrutoRecord update(String resource, String integratedAccountId, String id, Object body) {
        return update(resource, integratedAccountId, id, body, null, null, TrutoRecord.class);
    }

    /**
     * Updates a record (PATCH).
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord update(String resource, String integratedAccountId, String id, Object body, Map<String, ?> query, RequestOptions options) {
        return update(resource, integratedAccountId, id, body, query, options, TrutoRecord.class);
    }

    /**
     * Updates a record (PATCH). The response is decoded into {@code type}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T update(String resource, String integratedAccountId, String id, Object body, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("PATCH", ApiClient.path("proxy", resource, id), withAccount(integratedAccountId, query), body(body), options, type);
    }

    /**
     * Async form of {@code update}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> updateAsync(String resource, String integratedAccountId, String id, Object body, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("PATCH", ApiClient.path("proxy", resource, id), withAccount(integratedAccountId, query), body(body), options,
                TrutoRecord.class);
    }

    /**
     * Deletes a record.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @return the response record
     */
    public TrutoRecord delete(String resource, String integratedAccountId, String id) {
        return delete(resource, integratedAccountId, id, null, null, TrutoRecord.class);
    }

    /**
     * Deletes a record.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord delete(String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return delete(resource, integratedAccountId, id, query, options, TrutoRecord.class);
    }

    /**
     * Deletes a record. The response is decoded into {@code type}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T delete(String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("DELETE", ApiClient.path("proxy", resource, id), withAccount(integratedAccountId, query), null, options, type);
    }

    /**
     * Async form of {@code delete}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> deleteAsync(String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("DELETE", ApiClient.path("proxy", resource, id), withAccount(integratedAccountId, query), null, options,
                TrutoRecord.class);
    }

    /**
     * Calls a custom method (POST).
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @return the response record
     */
    public TrutoRecord customMethod(String resource, String integratedAccountId, String method, Object body) {
        return customMethod(resource, integratedAccountId, method, body, null, null, TrutoRecord.class);
    }

    /**
     * Calls a custom method (POST).
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord customMethod(String resource, String integratedAccountId, String method, Object body, Map<String, ?> query, RequestOptions options) {
        return customMethod(resource, integratedAccountId, method, body, query, options, TrutoRecord.class);
    }

    /**
     * Calls a custom method (POST). The response is decoded into {@code type}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T customMethod(String resource, String integratedAccountId, String method, Object body, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("POST", ApiClient.path("proxy", resource, method), withAccount(integratedAccountId, query), body, options, type);
    }

    /**
     * Async form of {@code customMethod}.
     *
     * @param resource the proxy resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> customMethodAsync(String resource, String integratedAccountId, String method, Object body, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("POST", ApiClient.path("proxy", resource, method), withAccount(integratedAccountId, query), body, options,
                TrutoRecord.class);
    }
}
