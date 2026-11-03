package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.BatchRequestResponse;
import one.truto.sdk.models.BatchRequestSchema;

/** Batch requests. Path: {@code /batch-request}. */
public class BatchRequests extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public BatchRequests(ApiClient api) {
        super(api, "batch-request");
    }

    /**
     * Runs a batch of requests.
     *
     * @param body body
     * @return the response
     */
    public BatchRequestResponse create(BatchRequestSchema body) {
        return create(body, null, null);
    }

    /**
     * Runs a batch of requests.
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public BatchRequestResponse create(BatchRequestSchema body, Map<String, ?> query, RequestOptions options) {
        return call("POST", base, query, body(body), options, BatchRequestResponse.class);
    }
}
