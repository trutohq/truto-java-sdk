package one.truto.sdk.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.CapabilitiesResponse;
import one.truto.sdk.models.DeleteResponse;
import one.truto.sdk.models.EnvironmentIntegrationDeleteResponse;
import one.truto.sdk.models.EnvironmentIntegrationResponse;
import one.truto.sdk.models.IntegrationCreateRequest;
import one.truto.sdk.models.IntegrationResponse;
import one.truto.sdk.models.IntegrationToolsResponse;
import one.truto.sdk.models.IntegrationUnifiedApisResponse;
import one.truto.sdk.models.IntegrationUpdateRequest;

/** Integrations (connector definitions). Path: {@code /integration}. */
public class Integrations extends CrudResource<IntegrationResponse, IntegrationCreateRequest, IntegrationResponse, IntegrationUpdateRequest, DeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Integrations(ApiClient api) {
        super(api, "integration", IntegrationResponse.class, IntegrationResponse.class, DeleteResponse.class);
    }

    /**
     * Installs an integration in the environment, enabled ({@code POST /environment-integration}).
     *
     * @param integrationId the integration ID
     * @return the environment integration
     */
    public EnvironmentIntegrationResponse install(String integrationId) {
        return install(integrationId, true, null, null);
    }

    /**
     * Installs an integration in the environment ({@code POST /environment-integration}).
     *
     * @param integrationId the integration ID
     * @param isEnabled whether the installation starts enabled
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the environment integration
     */
    public EnvironmentIntegrationResponse install(String integrationId, boolean isEnabled, Map<String, ?> query,
            RequestOptions options) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("integration_id", integrationId);
        payload.put("is_enabled", isEnabled);
        return call("POST", "environment-integration", query, payload, options, EnvironmentIntegrationResponse.class);
    }

    /**
     * Uninstalls an integration: lists {@code environment-integration?integration_id=...} and
     * deletes each row by its ID.
     *
     * @param integrationId the integration ID
     * @return one delete result per removed installation
     */
    public List<EnvironmentIntegrationDeleteResponse> uninstall(String integrationId) {
        return uninstall(integrationId, null);
    }

    /**
     * Uninstalls an integration: lists {@code environment-integration?integration_id=...} and
     * deletes each row by its ID.
     *
     * @param integrationId the integration ID
     * @param options per-request options, may be null
     * @return one delete result per removed installation
     */
    public List<EnvironmentIntegrationDeleteResponse> uninstall(String integrationId, RequestOptions options) {
        List<EnvironmentIntegrationResponse> rows = pager("environment-integration",
                Collections.singletonMap("integration_id", integrationId), options,
                EnvironmentIntegrationResponse.class).toList();
        List<EnvironmentIntegrationDeleteResponse> out = new ArrayList<>();
        for (EnvironmentIntegrationResponse row : rows) {
            out.add(call("DELETE", ApiClient.path("environment-integration", row.getId()), null, null, options,
                    EnvironmentIntegrationDeleteResponse.class));
        }
        return out;
    }

    /**
     * Lists the tools an integration exposes (deprecated by Truto in favour of {@link #capabilities}).
     *
     * @param id the integration ID or name
     * @return the response
     */
    public IntegrationToolsResponse tools(String id) {
        return tools(id, null, null);
    }

    /**
     * Lists the tools an integration exposes (deprecated by Truto in favour of {@link #capabilities}).
     *
     * @param id the integration ID or name
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegrationToolsResponse tools(String id, Map<String, ?> query, RequestOptions options) {
        return call("GET", at(id, "tools"), query, null, options, IntegrationToolsResponse.class);
    }

    /**
     * Lists the unified APIs an integration supports (deprecated by Truto in favour of {@link #capabilities}).
     *
     * @param id the integration ID or name
     * @return the response
     */
    public IntegrationUnifiedApisResponse unifiedApis(String id) {
        return unifiedApis(id, null, null);
    }

    /**
     * Lists the unified APIs an integration supports (deprecated by Truto in favour of {@link #capabilities}).
     *
     * @param id the integration ID or name
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegrationUnifiedApisResponse unifiedApis(String id, Map<String, ?> query, RequestOptions options) {
        return call("GET", at(id, "unified-apis"), query, null, options, IntegrationUnifiedApisResponse.class);
    }

    /**
     * Lists the resources and methods an integration supports.
     *
     * @param id the integration ID or name
     * @return the response
     */
    public CapabilitiesResponse capabilities(String id) {
        return capabilities(id, null, null);
    }

    /**
     * Lists the resources and methods an integration supports.
     *
     * @param id the integration ID or name
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public CapabilitiesResponse capabilities(String id, Map<String, ?> query, RequestOptions options) {
        return call("GET", at(id, "capabilities"), query, null, options, CapabilitiesResponse.class);
    }
}
