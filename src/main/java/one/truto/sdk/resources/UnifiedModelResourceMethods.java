package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.UnifiedModelResourceMethodCreateRequest;
import one.truto.sdk.models.UnifiedModelResourceMethodDeleteResponse;
import one.truto.sdk.models.UnifiedModelResourceMethodRowResponse;
import one.truto.sdk.models.UnifiedModelResourceMethodUpdateRequest;

/** Unified model resource method mappings. Path: {@code /unified-model-resource-method}. */
public class UnifiedModelResourceMethods extends CrudResource<UnifiedModelResourceMethodRowResponse, UnifiedModelResourceMethodCreateRequest, UnifiedModelResourceMethodRowResponse, UnifiedModelResourceMethodUpdateRequest, UnifiedModelResourceMethodDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public UnifiedModelResourceMethods(ApiClient api) {
        super(api, "unified-model-resource-method", UnifiedModelResourceMethodRowResponse.class, UnifiedModelResourceMethodRowResponse.class, UnifiedModelResourceMethodDeleteResponse.class);
    }
}
