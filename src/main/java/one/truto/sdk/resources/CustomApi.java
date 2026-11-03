package one.truto.sdk.resources;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RawResponse;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/**
 * Custom API: your own endpoints defined on an integration, at {@code /custom/<path>}.
 *
 * <p>The path may contain slashes; they are kept as-is and every other unsafe character is
 * percent-encoded. The integrated account ID is sent as {@code integrated_account_id}. The verb
 * methods decode a JSON object into a {@link TrutoRecord}; use {@link #request} with your own type
 * (for example {@code JsonNode.class} for an array) or {@link #requestRaw} for non-JSON bodies.
 */
public class CustomApi extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public CustomApi(ApiClient api) {
        super(api, "custom");
    }

    private static String customPath(String path) {
        String p = ApiClient.rawPath(path);
        return p.isEmpty() ? "custom" : "custom/" + p;
    }

    /**
     * Sends any method to a custom endpoint and decodes the JSON response into {@code type}.
     *
     * @param method the HTTP method
     * @param path the custom API path
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    public <T> T request(String method, String path, String integratedAccountId, Object body, Map<String, ?> query,
            RequestOptions options, Class<T> type) {
        return call(method, customPath(path), UnifiedApi.withAccount(integratedAccountId, query), body, options, type);
    }

    /**
     * Async form of {@link #request}.
     *
     * @param method the HTTP method
     * @param path the custom API path
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return a future of the decoded response
     */
    public <T> CompletableFuture<T> requestAsync(String method, String path, String integratedAccountId, Object body,
            Map<String, ?> query, RequestOptions options, Class<T> type) {
        return api.requestAsync(method, customPath(path), UnifiedApi.withAccount(integratedAccountId, query), body,
                options, type);
    }

    /**
     * Sends any method to a custom endpoint and returns the undecoded response (status, headers,
     * bytes), whatever its content type.
     *
     * @param method the HTTP method
     * @param path the custom API path
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the raw response
     */
    public RawResponse requestRaw(String method, String path, String integratedAccountId, Object body,
            Map<String, ?> query, RequestOptions options) {
        return api.send(method, customPath(path), UnifiedApi.withAccount(integratedAccountId, query), body, options);
    }

    /**
     * Async form of {@link #requestRaw}.
     *
     * @param method the HTTP method
     * @param path the custom API path
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the raw response
     */
    public CompletableFuture<RawResponse> requestRawAsync(String method, String path, String integratedAccountId,
            Object body, Map<String, ?> query, RequestOptions options) {
        return api.sendAsync(method, customPath(path), UnifiedApi.withAccount(integratedAccountId, query), body,
                options);
    }

    /**
     * GET {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @return the response record
     */
    public TrutoRecord get(String path, String integratedAccountId) {
        return request("GET", path, integratedAccountId, null, null, null, TrutoRecord.class);
    }

    /**
     * GET {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord get(String path, String integratedAccountId, Map<String, ?> query,
            RequestOptions options) {
        return request("GET", path, integratedAccountId, null, query, options, TrutoRecord.class);
    }

    /**
     * Async GET {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> getAsync(String path, String integratedAccountId,
            Map<String, ?> query, RequestOptions options) {
        return requestAsync("GET", path, integratedAccountId, null, query, options, TrutoRecord.class);
    }

    /**
     * POST {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @return the response record
     */
    public TrutoRecord post(String path, String integratedAccountId, Object body) {
        return request("POST", path, integratedAccountId, body, null, null, TrutoRecord.class);
    }

    /**
     * POST {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord post(String path, String integratedAccountId, Object body, Map<String, ?> query,
            RequestOptions options) {
        return request("POST", path, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * Async POST {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> postAsync(String path, String integratedAccountId, Object body,
            Map<String, ?> query, RequestOptions options) {
        return requestAsync("POST", path, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * PUT {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @return the response record
     */
    public TrutoRecord put(String path, String integratedAccountId, Object body) {
        return request("PUT", path, integratedAccountId, body, null, null, TrutoRecord.class);
    }

    /**
     * PUT {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord put(String path, String integratedAccountId, Object body, Map<String, ?> query,
            RequestOptions options) {
        return request("PUT", path, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * Async PUT {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> putAsync(String path, String integratedAccountId, Object body,
            Map<String, ?> query, RequestOptions options) {
        return requestAsync("PUT", path, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * PATCH {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @return the response record
     */
    public TrutoRecord patch(String path, String integratedAccountId, Object body) {
        return request("PATCH", path, integratedAccountId, body, null, null, TrutoRecord.class);
    }

    /**
     * PATCH {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord patch(String path, String integratedAccountId, Object body, Map<String, ?> query,
            RequestOptions options) {
        return request("PATCH", path, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * Async PATCH {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param body the JSON body, may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> patchAsync(String path, String integratedAccountId, Object body,
            Map<String, ?> query, RequestOptions options) {
        return requestAsync("PATCH", path, integratedAccountId, body, query, options, TrutoRecord.class);
    }

    /**
     * DELETE {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @return the response record
     */
    public TrutoRecord delete(String path, String integratedAccountId) {
        return request("DELETE", path, integratedAccountId, null, null, null, TrutoRecord.class);
    }

    /**
     * DELETE {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the response record
     */
    public TrutoRecord delete(String path, String integratedAccountId, Map<String, ?> query,
            RequestOptions options) {
        return request("DELETE", path, integratedAccountId, null, query, options, TrutoRecord.class);
    }

    /**
     * Async DELETE {@code /custom/<path>}.
     *
     * @param path the custom API path (may contain {@code /})
     * @param integratedAccountId the integrated account ID
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return a future of the response record
     */
    public CompletableFuture<TrutoRecord> deleteAsync(String path, String integratedAccountId,
            Map<String, ?> query, RequestOptions options) {
        return requestAsync("DELETE", path, integratedAccountId, null, query, options, TrutoRecord.class);
    }
}
