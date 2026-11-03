package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.EnvironmentResponse;

/** Environments. Path: {@code /environment}. */
public class Environments extends ReadResource<EnvironmentResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Environments(ApiClient api) {
        super(api, "environment", EnvironmentResponse.class);
    }

    /**
     * Updates an environment.
     *
     * @param id id
     * @param body the fields to change
     * @return the response
     */
    public EnvironmentResponse update(String id, Object body) {
        return update(id, body, null, null);
    }

    /**
     * Updates an environment.
     *
     * @param id id
     * @param body the fields to change
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public EnvironmentResponse update(String id, Object body, Map<String, ?> query, RequestOptions options) {
        return call("PATCH", at(id), query, body(body), options, EnvironmentResponse.class);
    }
}
