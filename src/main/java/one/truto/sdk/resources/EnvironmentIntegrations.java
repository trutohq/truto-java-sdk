package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.EnvironmentIntegrationCreateRequest;
import one.truto.sdk.models.EnvironmentIntegrationDeleteResponse;
import one.truto.sdk.models.EnvironmentIntegrationResponse;
import one.truto.sdk.models.EnvironmentIntegrationUpdateRequest;

/** Integrations installed in the environment. Path: {@code /environment-integration}. */
public class EnvironmentIntegrations extends CrudResource<EnvironmentIntegrationResponse, EnvironmentIntegrationCreateRequest, EnvironmentIntegrationResponse, EnvironmentIntegrationUpdateRequest, EnvironmentIntegrationDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public EnvironmentIntegrations(ApiClient api) {
        super(api, "environment-integration", EnvironmentIntegrationResponse.class, EnvironmentIntegrationResponse.class, EnvironmentIntegrationDeleteResponse.class);
    }
}
