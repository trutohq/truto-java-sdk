package one.truto.sdk.resources;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import one.truto.sdk.ApiClient;
import one.truto.sdk.Page;
import one.truto.sdk.Pager;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;
import one.truto.sdk.models.UnifiedApiMetaResponse;
import one.truto.sdk.models.UnifiedMetaGetResponse;

/**
 * Unified API: one schema across every integration in a category.
 *
 * <p>Every call takes the unified model (e.g. {@code crm}), the resource (e.g. {@code contacts})
 * and the integrated account ID, which is sent as the {@code integrated_account_id} query
 * parameter. Results are {@link TrutoRecord}s; pass a {@code Class<T>} to decode into your own
 * type.
 */
public class UnifiedApi extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public UnifiedApi(ApiClient api) {
        super(api, "unified");
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
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @return a pager over all records
     */
    public Pager<TrutoRecord> list(String unifiedModel, String resource, String integratedAccountId) {
        return list(unifiedModel, resource, integratedAccountId, null, null, TrutoRecord.class);
    }

    /**
     * Lists records.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters (filters, {@code limit}, ...), may be null
     * @return a pager over all records
     */
    public Pager<TrutoRecord> list(String unifiedModel, String resource, String integratedAccountId, Map<String, ?> query) {
        return list(unifiedModel, resource, integratedAccountId, query, null, TrutoRecord.class);
    }

    /**
     * Lists records.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a pager over all records
     */
    public Pager<TrutoRecord> list(String unifiedModel, String resource, String integratedAccountId, Map<String, ?> query, RequestOptions options) {
        return list(unifiedModel, resource, integratedAccountId, query, options, TrutoRecord.class);
    }

    /**
     * Lists records. Items are decoded into {@code type}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the item type
     * @param <T> the item type
     * @return a pager over all items
     */
    public <T> Pager<T> list(String unifiedModel, String resource, String integratedAccountId, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return pager(ApiClient.path("unified", unifiedModel, resource), withAccount(integratedAccountId, query), options, type);
    }

    /**
     * Fetches a single page asynchronously. Pass {@code next_cursor} in the query for later pages.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the page
     */
    public CompletableFuture<Page<TrutoRecord>> listPageAsync(String unifiedModel, String resource, String integratedAccountId, Map<String, ?> query, RequestOptions options) {
        return api.pageAsync(ApiClient.path("unified", unifiedModel, resource), withAccount(integratedAccountId, query), options, TrutoRecord.class);
    }

    /**
     * Fetches one record.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @return the response record
     */
    public TrutoRecord get(String unifiedModel, String resource, String integratedAccountId, String id) {
        return get(unifiedModel, resource, integratedAccountId, id, null, null, TrutoRecord.class);
    }

    /**
     * Fetches one record.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord get(String unifiedModel, String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return get(unifiedModel, resource, integratedAccountId, id, query, options, TrutoRecord.class);
    }

    /**
     * Fetches one record. The response is decoded into {@code type}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T get(String unifiedModel, String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("GET", ApiClient.path("unified", unifiedModel, resource, id), withAccount(integratedAccountId, query), null, options, type);
    }

    /**
     * Async form of {@code get}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> getAsync(String unifiedModel, String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("GET", ApiClient.path("unified", unifiedModel, resource, id), withAccount(integratedAccountId, query), null, options,
                TrutoRecord.class);
    }

    /**
     * Creates a record.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @return the response record
     */
    public TrutoRecord create(String unifiedModel, String resource, String integratedAccountId, Object body) {
        return create(unifiedModel, resource, integratedAccountId, body, null, null, TrutoRecord.class);
    }

    /**
     * Creates a record.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord create(String unifiedModel, String resource, String integratedAccountId, Object body, Map<String, ?> query, RequestOptions options) {
        return create(unifiedModel, resource, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * Creates a record. The response is decoded into {@code type}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T create(String unifiedModel, String resource, String integratedAccountId, Object body, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("POST", ApiClient.path("unified", unifiedModel, resource), withAccount(integratedAccountId, query), body(body), options, type);
    }

    /**
     * Async form of {@code create}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param body the record to create
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> createAsync(String unifiedModel, String resource, String integratedAccountId, Object body, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("POST", ApiClient.path("unified", unifiedModel, resource), withAccount(integratedAccountId, query), body(body), options,
                TrutoRecord.class);
    }

    /**
     * Updates a record (PATCH).
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @return the response record
     */
    public TrutoRecord update(String unifiedModel, String resource, String integratedAccountId, String id, Object body) {
        return update(unifiedModel, resource, integratedAccountId, id, body, null, null, TrutoRecord.class);
    }

    /**
     * Updates a record (PATCH).
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord update(String unifiedModel, String resource, String integratedAccountId, String id, Object body, Map<String, ?> query, RequestOptions options) {
        return update(unifiedModel, resource, integratedAccountId, id, body, query, options, TrutoRecord.class);
    }

    /**
     * Updates a record (PATCH). The response is decoded into {@code type}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T update(String unifiedModel, String resource, String integratedAccountId, String id, Object body, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("PATCH", ApiClient.path("unified", unifiedModel, resource, id), withAccount(integratedAccountId, query), body(body), options, type);
    }

    /**
     * Async form of {@code update}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param body the fields to change
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> updateAsync(String unifiedModel, String resource, String integratedAccountId, String id, Object body, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("PATCH", ApiClient.path("unified", unifiedModel, resource, id), withAccount(integratedAccountId, query), body(body), options,
                TrutoRecord.class);
    }

    /**
     * Deletes a record.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @return the response record
     */
    public TrutoRecord delete(String unifiedModel, String resource, String integratedAccountId, String id) {
        return delete(unifiedModel, resource, integratedAccountId, id, null, null, TrutoRecord.class);
    }

    /**
     * Deletes a record.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord delete(String unifiedModel, String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return delete(unifiedModel, resource, integratedAccountId, id, query, options, TrutoRecord.class);
    }

    /**
     * Deletes a record. The response is decoded into {@code type}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T delete(String unifiedModel, String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("DELETE", ApiClient.path("unified", unifiedModel, resource, id), withAccount(integratedAccountId, query), null, options, type);
    }

    /**
     * Async form of {@code delete}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param id the record ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> deleteAsync(String unifiedModel, String resource, String integratedAccountId, String id, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("DELETE", ApiClient.path("unified", unifiedModel, resource, id), withAccount(integratedAccountId, query), null, options,
                TrutoRecord.class);
    }

    /**
     * Calls a custom method (POST).
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @return the response record
     */
    public TrutoRecord customMethod(String unifiedModel, String resource, String integratedAccountId, String method, Object body) {
        return customMethod(unifiedModel, resource, integratedAccountId, method, body, null, null, TrutoRecord.class);
    }

    /**
     * Calls a custom method (POST).
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord customMethod(String unifiedModel, String resource, String integratedAccountId, String method, Object body, Map<String, ?> query, RequestOptions options) {
        return customMethod(unifiedModel, resource, integratedAccountId, method, body, query, options, TrutoRecord.class);
    }

    /**
     * Calls a custom method (POST). The response is decoded into {@code type}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T customMethod(String unifiedModel, String resource, String integratedAccountId, String method, Object body, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return call("POST", ApiClient.path("unified", unifiedModel, resource, method), withAccount(integratedAccountId, query), body, options, type);
    }

    /**
     * Async form of {@code customMethod}.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integratedAccountId the integrated account ID
     * @param method the custom method name
     * @param body the request body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> customMethodAsync(String unifiedModel, String resource, String integratedAccountId, String method, Object body, Map<String, ?> query, RequestOptions options) {
        return api.requestAsync("POST", ApiClient.path("unified", unifiedModel, resource, method), withAccount(integratedAccountId, query), body, options,
                TrutoRecord.class);
    }

    /**
     * Describes one integration's mapping for a unified method
     * ({@code GET /unified/{model}/{resource}/{integration}/meta/{method}}). Needs no integrated account.
     *
     * @param unifiedModel the unified model, e.g. {@code crm}
     * @param resource the resource, e.g. {@code contacts}
     * @param integrationName the integration name, e.g. {@code hubspot}
     * @param method the method, e.g. {@code list}
     * @return the metadata
     */
    public UnifiedApiMetaResponse meta(String unifiedModel, String resource, String integrationName, String method) {
        return meta(unifiedModel, resource, integrationName, method, null, null);
    }

    /**
     * Describes one integration's mapping for a unified method.
     *
     * @param unifiedModel the unified model
     * @param resource the resource
     * @param integrationName the integration name
     * @param method the method
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the metadata
     */
    public UnifiedApiMetaResponse meta(String unifiedModel, String resource, String integrationName, String method,
            Map<String, ?> query, RequestOptions options) {
        return call("GET", ApiClient.path("unified", unifiedModel, resource, integrationName, "meta", method), query,
                null, options, UnifiedApiMetaResponse.class);
    }

    /**
     * Lists the resources and methods of a unified model that an integration supports
     * ({@code GET /unified/meta/{model}/{integration}}).
     *
     * @param unifiedModel the unified model
     * @param integrationName the integration name
     * @return the metadata
     */
    public UnifiedMetaGetResponse modelMeta(String unifiedModel, String integrationName) {
        return modelMeta(unifiedModel, integrationName, null, null);
    }

    /**
     * Lists the resources and methods of a unified model that an integration supports.
     *
     * @param unifiedModel the unified model
     * @param integrationName the integration name
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the metadata
     */
    public UnifiedMetaGetResponse modelMeta(String unifiedModel, String integrationName, Map<String, ?> query,
            RequestOptions options) {
        return call("GET", ApiClient.path("unified", "meta", unifiedModel, integrationName), query, null, options,
                UnifiedMetaGetResponse.class);
    }
}
