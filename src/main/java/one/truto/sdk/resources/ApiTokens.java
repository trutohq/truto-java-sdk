package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.ApiTokenResponse;

/** API tokens. Path: {@code /api-token}. */
public class ApiTokens extends ReadResource<ApiTokenResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public ApiTokens(ApiClient api) {
        super(api, "api-token", ApiTokenResponse.class);
    }
}
