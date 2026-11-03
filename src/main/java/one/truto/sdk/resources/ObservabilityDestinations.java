package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.DeleteResponse;
import one.truto.sdk.models.ObservabilityDestinationCreateRequest;
import one.truto.sdk.models.ObservabilityDestinationResponse;
import one.truto.sdk.models.ObservabilityDestinationTestRequest;
import one.truto.sdk.models.ObservabilityDestinationTestResponse;
import one.truto.sdk.models.ObservabilityDestinationUpdateRequest;

/** Observability destinations (log/trace export). Path: {@code /observability-destination}. */
public class ObservabilityDestinations extends CrudResource<ObservabilityDestinationResponse, ObservabilityDestinationCreateRequest, ObservabilityDestinationResponse, ObservabilityDestinationUpdateRequest, DeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public ObservabilityDestinations(ApiClient api) {
        super(api, "observability-destination", ObservabilityDestinationResponse.class, ObservabilityDestinationResponse.class, DeleteResponse.class);
    }

    /**
     * Sends a test event to a destination.
     *
     * @param id id
     * @param body body
     * @return the response
     */
    public ObservabilityDestinationTestResponse test(String id, ObservabilityDestinationTestRequest body) {
        return test(id, body, null, null);
    }

    /**
     * Sends a test event to a destination.
     *
     * @param id id
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public ObservabilityDestinationTestResponse test(String id, ObservabilityDestinationTestRequest body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(id, "test"), query, body(body), options, ObservabilityDestinationTestResponse.class);
    }
}
