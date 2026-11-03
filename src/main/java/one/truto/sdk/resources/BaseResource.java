package one.truto.sdk.resources;

import java.util.Map;
import java.util.Objects;
import one.truto.sdk.ApiClient;
import one.truto.sdk.Pager;
import one.truto.sdk.RequestOptions;

/** Shared plumbing for resource classes. */
public abstract class BaseResource {
    /** The HTTP layer. */
    protected final ApiClient api;
    /** The resource's base path, e.g. {@code integrated-account}. */
    protected final String base;

    /**
     * Creates a resource.
     *
     * @param api the HTTP layer
     * @param base the base path
     */
    protected BaseResource(ApiClient api, String base) {
        this.api = Objects.requireNonNull(api, "api");
        this.base = base;
    }

    /**
     * Builds {@code base/seg1/seg2...} with each segment percent-encoded.
     *
     * @param segments the segments after the base path
     * @return the path
     */
    protected String at(String... segments) {
        String[] all = new String[segments.length + 1];
        all[0] = base;
        System.arraycopy(segments, 0, all, 1, segments.length);
        return ApiClient.path(all);
    }

    /**
     * Sends a request and decodes the response.
     *
     * @param method the HTTP method
     * @param path the path
     * @param query the query, may be null
     * @param body the body, may be null
     * @param options the options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return the decoded response
     */
    protected <T> T call(String method, String path, Map<String, ?> query, Object body,
            RequestOptions options, Class<T> type) {
        return api.request(method, path, query, body, options, type);
    }

    /**
     * Creates a pager.
     *
     * @param path the list path
     * @param query the query, may be null
     * @param options the options, may be null
     * @param type the item type
     * @param <T> the item type
     * @return the pager
     */
    protected <T> Pager<T> pager(String path, Map<String, ?> query, RequestOptions options, Class<T> type) {
        return api.pager(path, query, options, type);
    }

    /**
     * Fails fast on a null body.
     *
     * @param body the body
     * @param <B> the body type
     * @return the body
     */
    protected static <B> B body(B body) {
        return Objects.requireNonNull(body, "body");
    }
}
