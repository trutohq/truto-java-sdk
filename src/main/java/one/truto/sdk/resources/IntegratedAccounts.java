package one.truto.sdk.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.Pager;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.CapabilitiesResponse;
import one.truto.sdk.models.DeleteResponse;
import one.truto.sdk.models.IntegratedAccountCreate;
import one.truto.sdk.models.IntegratedAccountCredentialsUpdateRequest;
import one.truto.sdk.models.IntegratedAccountFormTokenResponse;
import one.truto.sdk.models.IntegratedAccountMcpCreateRequest;
import one.truto.sdk.models.IntegratedAccountMcpUpdateRequest;
import one.truto.sdk.models.IntegratedAccountRefreshCredentialsResponse;
import one.truto.sdk.models.IntegratedAccountRefreshMappingCacheRequest;
import one.truto.sdk.models.IntegratedAccountRefreshMappingCacheResponse;
import one.truto.sdk.models.IntegratedAccountResponse;
import one.truto.sdk.models.IntegratedAccountRunPostInstallActionsResponse;
import one.truto.sdk.models.IntegratedAccountTokenResponse;
import one.truto.sdk.models.IntegratedAccountUpdate;
import one.truto.sdk.models.IntegratedAccountValidateResponse;
import one.truto.sdk.models.MCP;
import one.truto.sdk.models.MCPCreateResponse;
import one.truto.sdk.models.ToolsResponseSchema;
import one.truto.sdk.models.TrutoRecord;

/** Integrated accounts (connected customer accounts). Path: {@code /integrated-account}. */
public class IntegratedAccounts extends CrudResource<IntegratedAccountResponse, IntegratedAccountCreate, IntegratedAccountResponse, IntegratedAccountUpdate, DeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public IntegratedAccounts(ApiClient api) {
        super(api, "integrated-account", IntegratedAccountResponse.class, IntegratedAccountResponse.class, DeleteResponse.class);
    }

    /**
     * Replaces an account's credentials ({@code PATCH /integrated-account/{id}/credentials}).
     *
     * @param id id
     * @param body body
     * @return the response
     */
    public IntegratedAccountResponse updateCredentials(String id, IntegratedAccountCredentialsUpdateRequest body) {
        return updateCredentials(id, body, null, null);
    }

    /**
     * Replaces an account's credentials ({@code PATCH /integrated-account/{id}/credentials}).
     *
     * @param id id
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegratedAccountResponse updateCredentials(String id, IntegratedAccountCredentialsUpdateRequest body, Map<String, ?> query, RequestOptions options) {
        return call("PATCH", at(id, "credentials"), query, body(body), options, IntegratedAccountResponse.class);
    }

    /**
     * Deletes many accounts ({@code POST /integrated-account/bulk-delete}).
     *
     * @param body body
     * @return the response
     */
    public TrutoRecord bulkDelete(Object body) {
        return bulkDelete(body, null, null);
    }

    /**
     * Deletes many accounts ({@code POST /integrated-account/bulk-delete}).
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord bulkDelete(Object body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("bulk-delete"), query, body(body), options, TrutoRecord.class);
    }

    /**
     * Mints a token scoped to one account ({@code POST /integrated-account/token}).
     *
     * @param id id
     * @return the response
     */
    public IntegratedAccountTokenResponse createToken(String id) {
        return createToken(id, null, null);
    }

    /**
     * Mints a token scoped to one account ({@code POST /integrated-account/token}).
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegratedAccountTokenResponse createToken(String id, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("token"), query, Collections.singletonMap("integrated_account_id", id), options, IntegratedAccountTokenResponse.class);
    }

    /**
     * Mints a form token for an account ({@code POST /integrated-account/form-token}).
     *
     * @param id id
     * @return the response
     */
    public IntegratedAccountFormTokenResponse createFormToken(String id) {
        return createFormToken(id, null, null);
    }

    /**
     * Mints a form token for an account ({@code POST /integrated-account/form-token}).
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegratedAccountFormTokenResponse createFormToken(String id, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("form-token"), query, Collections.singletonMap("integrated_account_id", id), options, IntegratedAccountFormTokenResponse.class);
    }

    /**
     * Refreshes an account's OAuth credentials ({@code POST /integrated-account/refresh-credentials}).
     *
     * @param id id
     * @return the response
     */
    public IntegratedAccountRefreshCredentialsResponse refreshCredentials(String id) {
        return refreshCredentials(id, null, null);
    }

    /**
     * Refreshes an account's OAuth credentials ({@code POST /integrated-account/refresh-credentials}).
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegratedAccountRefreshCredentialsResponse refreshCredentials(String id, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("refresh-credentials"), query, Collections.singletonMap("id", id), options, IntegratedAccountRefreshCredentialsResponse.class);
    }

    /**
     * Re-runs post-install actions ({@code POST /integrated-account/run-post-install-actions}).
     *
     * @param id id
     * @return the response
     */
    public IntegratedAccountRunPostInstallActionsResponse runPostInstallActions(String id) {
        return runPostInstallActions(id, null, null);
    }

    /**
     * Re-runs post-install actions ({@code POST /integrated-account/run-post-install-actions}).
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegratedAccountRunPostInstallActionsResponse runPostInstallActions(String id, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("run-post-install-actions"), query, Collections.singletonMap("id", id), options, IntegratedAccountRunPostInstallActionsResponse.class);
    }

    /**
     * Refreshes cached unified mappings ({@code POST /integrated-account/refresh-mapping-cache}).
     *
     * @param body body
     * @return the response
     */
    public IntegratedAccountRefreshMappingCacheResponse refreshMappingCache(IntegratedAccountRefreshMappingCacheRequest body) {
        return refreshMappingCache(body, null, null);
    }

    /**
     * Refreshes cached unified mappings ({@code POST /integrated-account/refresh-mapping-cache}).
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegratedAccountRefreshMappingCacheResponse refreshMappingCache(IntegratedAccountRefreshMappingCacheRequest body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("refresh-mapping-cache"), query, body(body), options, IntegratedAccountRefreshMappingCacheResponse.class);
    }

    /**
     * Runs the integration's validation against an account.
     *
     * @param id id
     * @return the response
     */
    public IntegratedAccountValidateResponse validate(String id) {
        return validate(id, null, null);
    }

    /**
     * Runs the integration's validation against an account.
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public IntegratedAccountValidateResponse validate(String id, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(id, "validate"), query, null, options, IntegratedAccountValidateResponse.class);
    }

    /**
     * Lists the tools available on an account ({@code GET /integrated-account/{id}/tools}).
     *
     * @param id the integrated account ID
     * @return the tools
     */
    public ToolsResponseSchema tools(String id) {
        return tools(id, null, null, null, null);
    }

    /**
     * Lists the tools available on an account, filtered by method and tag.
     *
     * @param id the integrated account ID
     * @param methods methods to include (sent as the array {@code methods[0]=...}), may be null
     * @param tags tags to include (sent as the array {@code tags[0]=...}), may be null
     * @param query extra query parameters, may be null
     * @param options per-request options, may be null
     * @return the tools
     */
    public ToolsResponseSchema tools(String id, List<String> methods, List<String> tags, Map<String, ?> query,
            RequestOptions options) {
        Map<String, Object> q = new LinkedHashMap<>();
        if (query != null) {
            q.putAll(query);
        }
        if (methods != null && !methods.isEmpty()) {
            q.put("methods", new ArrayList<>(methods));
        }
        if (tags != null && !tags.isEmpty()) {
            q.put("tags", new ArrayList<>(tags));
        }
        return call("GET", at(id, "tools"), q, null, options, ToolsResponseSchema.class);
    }

    /**
     * Lists the resources and methods available on an account.
     *
     * @param id id
     * @return the response
     */
    public CapabilitiesResponse capabilities(String id) {
        return capabilities(id, null, null);
    }

    /**
     * Lists the resources and methods available on an account.
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public CapabilitiesResponse capabilities(String id, Map<String, ?> query, RequestOptions options) {
        return call("GET", at(id, "capabilities"), query, null, options, CapabilitiesResponse.class);
    }

    /**
     * Lists an account's MCP servers ({@code GET /integrated-account/{id}/mcp}).
     *
     * @param id the integrated account ID
     * @return a pager over the servers
     */
    public Pager<MCP> listMcpServers(String id) {
        return listMcpServers(id, null, null);
    }

    /**
     * Lists an account's MCP servers ({@code GET /integrated-account/{id}/mcp}).
     *
     * @param id the integrated account ID
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return a pager over the servers
     */
    public Pager<MCP> listMcpServers(String id, Map<String, ?> query, RequestOptions options) {
        return pager(at(id, "mcp"), query, options, MCP.class);
    }

    /**
     * Fetches one MCP server.
     *
     * @param id id
     * @param mcpId the MCP server ID
     * @return the response
     */
    public MCP getMcpServer(String id, String mcpId) {
        return getMcpServer(id, mcpId, null, null);
    }

    /**
     * Fetches one MCP server.
     *
     * @param id id
     * @param mcpId the MCP server ID
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public MCP getMcpServer(String id, String mcpId, Map<String, ?> query, RequestOptions options) {
        return call("GET", at(id, "mcp", mcpId), query, null, options, MCP.class);
    }

    /**
     * Creates an MCP server for an account.
     *
     * @param id id
     * @param body body
     * @return the response
     */
    public MCPCreateResponse createMcpServer(String id, IntegratedAccountMcpCreateRequest body) {
        return createMcpServer(id, body, null, null);
    }

    /**
     * Creates an MCP server for an account.
     *
     * @param id id
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public MCPCreateResponse createMcpServer(String id, IntegratedAccountMcpCreateRequest body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(id, "mcp"), query, body(body), options, MCPCreateResponse.class);
    }

    /**
     * Updates an MCP server.
     *
     * @param id id
     * @param mcpId the MCP server ID
     * @param body body
     * @return the response
     */
    public MCP updateMcpServer(String id, String mcpId, IntegratedAccountMcpUpdateRequest body) {
        return updateMcpServer(id, mcpId, body, null, null);
    }

    /**
     * Updates an MCP server.
     *
     * @param id id
     * @param mcpId the MCP server ID
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public MCP updateMcpServer(String id, String mcpId, IntegratedAccountMcpUpdateRequest body, Map<String, ?> query, RequestOptions options) {
        return call("PATCH", at(id, "mcp", mcpId), query, body(body), options, MCP.class);
    }

    /**
     * Deletes an MCP server.
     *
     * @param id id
     * @param mcpId the MCP server ID
     * @return the response
     */
    public DeleteResponse deleteMcpServer(String id, String mcpId) {
        return deleteMcpServer(id, mcpId, null, null);
    }

    /**
     * Deletes an MCP server.
     *
     * @param id id
     * @param mcpId the MCP server ID
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public DeleteResponse deleteMcpServer(String id, String mcpId, Map<String, ?> query, RequestOptions options) {
        return call("DELETE", at(id, "mcp", mcpId), query, null, options, DeleteResponse.class);
    }
}
