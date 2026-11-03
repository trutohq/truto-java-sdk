package one.truto.sdk.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.EnvironmentUnifiedModelDeleteResponse;
import one.truto.sdk.models.EnvironmentUnifiedModelResponse;
import one.truto.sdk.models.UnifiedModelCreateRequest;
import one.truto.sdk.models.UnifiedModelDeleteResponse;
import one.truto.sdk.models.UnifiedModelResponse;
import one.truto.sdk.models.UnifiedModelUpdateRequest;

/** Unified models. Path: {@code /unified-model}. */
public class UnifiedModels extends CrudResource<UnifiedModelResponse, UnifiedModelCreateRequest, UnifiedModelResponse, UnifiedModelUpdateRequest, UnifiedModelDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public UnifiedModels(ApiClient api) {
        super(api, "unified-model", UnifiedModelResponse.class, UnifiedModelResponse.class, UnifiedModelDeleteResponse.class);
    }

    /**
     * Installs a unified model in the environment ({@code POST /environment-unified-model}).
     *
     * @param unifiedModelId unifiedModel ID
     * @return the response
     */
    public EnvironmentUnifiedModelResponse install(String unifiedModelId) {
        return install(unifiedModelId, null, null);
    }

    /**
     * Installs a unified model in the environment ({@code POST /environment-unified-model}).
     *
     * @param unifiedModelId unifiedModel ID
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public EnvironmentUnifiedModelResponse install(String unifiedModelId, Map<String, ?> query, RequestOptions options) {
        return call("POST", "environment-unified-model", query, Collections.singletonMap("unified_model_id", unifiedModelId), options, EnvironmentUnifiedModelResponse.class);
    }

    /**
     * Uninstalls a unified model: finds its environment-unified-model rows and deletes each.
     *
     * @param unifiedModelId the unified model ID
     * @return one delete result per removed installation
     */
    public List<EnvironmentUnifiedModelDeleteResponse> uninstall(String unifiedModelId) {
        return uninstall(unifiedModelId, null);
    }

    /**
     * Uninstalls a unified model: finds its environment-unified-model rows and deletes each.
     *
     * @param unifiedModelId the unified model ID
     * @param options per-request options, may be null
     * @return one delete result per removed installation
     */
    public List<EnvironmentUnifiedModelDeleteResponse> uninstall(String unifiedModelId, RequestOptions options) {
        List<EnvironmentUnifiedModelDeleteResponse> out = new ArrayList<>();
        List<EnvironmentUnifiedModelResponse> rows = pager("environment-unified-model",
                Collections.singletonMap("unified_model_id", unifiedModelId), options,
                EnvironmentUnifiedModelResponse.class).toList();
        for (EnvironmentUnifiedModelResponse row : rows) {
            out.add(call("DELETE", ApiClient.path("environment-unified-model", row.getId()), null, null, options,
                    EnvironmentUnifiedModelDeleteResponse.class));
        }
        return out;
    }
}
