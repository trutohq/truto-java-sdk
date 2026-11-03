package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.DeleteResponse;
import one.truto.sdk.models.StaticGateCreateRequest;
import one.truto.sdk.models.StaticGateCreateResponse;
import one.truto.sdk.models.StaticGateResponse;
import one.truto.sdk.models.StaticGateUpdateRequest;

/** Static gates. Path: {@code /static-gate}. */
public class StaticGates extends CrudResource<StaticGateResponse, StaticGateCreateRequest, StaticGateCreateResponse, StaticGateUpdateRequest, DeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public StaticGates(ApiClient api) {
        super(api, "static-gate", StaticGateResponse.class, StaticGateCreateResponse.class, DeleteResponse.class);
    }
}
