package one.truto.sdk.resources;

import java.util.LinkedHashMap;
import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/**
 * MCP servers created for integrated accounts, called over JSON-RPC at {@code POST /mcp/<token>}.
 * The token comes from {@code integratedAccounts().createMcpServer(...)}.
 */
public class Mcp extends BaseResource {
    /** The MCP protocol version sent by {@link #initialize}. */
    public static final String PROTOCOL_VERSION = "2024-11-05";

    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Mcp(ApiClient api) {
        super(api, "mcp");
    }

    /**
     * Sends a JSON-RPC payload as-is.
     *
     * @param token the MCP server token
     * @param payload the JSON-RPC message
     * @return the JSON-RPC response
     */
    public TrutoRecord call(String token, Map<String, ?> payload) {
        return call(token, payload, null, null);
    }

    /**
     * Sends a JSON-RPC payload as-is.
     *
     * @param token the MCP server token
     * @param payload the JSON-RPC message
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the JSON-RPC response
     */
    public TrutoRecord call(String token, Map<String, ?> payload, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(token), query, body(payload), options, TrutoRecord.class);
    }

    /**
     * Sends {@code initialize}.
     *
     * @param token the MCP server token
     * @param clientName your client's name
     * @param clientVersion your client's version
     * @return the JSON-RPC response
     */
    public TrutoRecord initialize(String token, String clientName, String clientVersion) {
        return call(token, initializePayload(clientName, clientVersion));
    }

    /**
     * Sends {@code tools/list}.
     *
     * @param token the MCP server token
     * @return the JSON-RPC response
     */
    public TrutoRecord listTools(String token) {
        return call(token, listToolsPayload());
    }

    /**
     * Sends {@code tools/call}.
     *
     * @param token the MCP server token
     * @param name the tool name
     * @param arguments the tool arguments, may be null
     * @return the JSON-RPC response
     */
    public TrutoRecord callTool(String token, String name, Map<String, ?> arguments) {
        return call(token, callToolPayload(name, arguments));
    }

    /**
     * The {@code initialize} payload.
     *
     * @param clientName your client's name
     * @param clientVersion your client's version
     * @return the payload
     */
    public static Map<String, Object> initializePayload(String clientName, String clientVersion) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", clientName);
        info.put("version", clientVersion);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("protocolVersion", PROTOCOL_VERSION);
        params.put("clientInfo", info);
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("jsonrpc", "2.0");
        p.put("method", "initialize");
        p.put("params", params);
        return p;
    }

    /**
     * The {@code tools/list} payload.
     *
     * @return the payload
     */
    public static Map<String, Object> listToolsPayload() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("jsonrpc", "2.0");
        p.put("method", "tools/list");
        p.put("id", 1);
        return p;
    }

    /**
     * The {@code tools/call} payload.
     *
     * @param name the tool name
     * @param arguments the tool arguments, may be null (sent as {@code {}})
     * @return the payload
     */
    public static Map<String, Object> callToolPayload(String name, Map<String, ?> arguments) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("arguments", arguments == null ? new LinkedHashMap<String, Object>() : arguments);
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("jsonrpc", "2.0");
        p.put("method", "tools/call");
        p.put("id", 2);
        p.put("params", params);
        return p;
    }
}
