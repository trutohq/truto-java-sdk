package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.EnvironmentUnifiedModelResourceMethodCreateRequest;
import one.truto.sdk.models.EnvironmentUnifiedModelResourceMethodDeleteResponse;
import one.truto.sdk.models.EnvironmentUnifiedModelResourceMethodRowResponse;
import one.truto.sdk.models.EnvironmentUnifiedModelResourceMethodUpdateRequest;

/** Environment-level overrides of unified model resource method mappings. Path: {@code /environment-unified-model-resource-method}. */
public class EnvironmentUnifiedModelResourceMethods extends CrudResource<EnvironmentUnifiedModelResourceMethodRowResponse, EnvironmentUnifiedModelResourceMethodCreateRequest, EnvironmentUnifiedModelResourceMethodRowResponse, EnvironmentUnifiedModelResourceMethodUpdateRequest, EnvironmentUnifiedModelResourceMethodDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public EnvironmentUnifiedModelResourceMethods(ApiClient api) {
        super(api, "environment-unified-model-resource-method", EnvironmentUnifiedModelResourceMethodRowResponse.class, EnvironmentUnifiedModelResourceMethodRowResponse.class, EnvironmentUnifiedModelResourceMethodDeleteResponse.class);
    }
}
