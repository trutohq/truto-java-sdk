package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.EnvironmentUnifiedModelCreateRequest;
import one.truto.sdk.models.EnvironmentUnifiedModelDeleteResponse;
import one.truto.sdk.models.EnvironmentUnifiedModelResponse;
import one.truto.sdk.models.EnvironmentUnifiedModelUpdateRequest;

/** Unified models installed in the environment. Path: {@code /environment-unified-model}. */
public class EnvironmentUnifiedModels extends CrudResource<EnvironmentUnifiedModelResponse, EnvironmentUnifiedModelCreateRequest, EnvironmentUnifiedModelResponse, EnvironmentUnifiedModelUpdateRequest, EnvironmentUnifiedModelDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public EnvironmentUnifiedModels(ApiClient api) {
        super(api, "environment-unified-model", EnvironmentUnifiedModelResponse.class, EnvironmentUnifiedModelResponse.class, EnvironmentUnifiedModelDeleteResponse.class);
    }
}
