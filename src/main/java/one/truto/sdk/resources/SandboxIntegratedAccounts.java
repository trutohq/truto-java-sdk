package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/** Sandbox integrated accounts. Path: {@code /sandbox-integrated-account}. */
public class SandboxIntegratedAccounts extends ReadResource<TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public SandboxIntegratedAccounts(ApiClient api) {
        super(api, "sandbox-integrated-account", TrutoRecord.class);
    }

    /**
     * Creates a sandbox integrated account.
     *
     * @param body body
     * @return the response
     */
    public TrutoRecord create(Object body) {
        return create(body, null, null);
    }

    /**
     * Creates a sandbox integrated account.
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord create(Object body, Map<String, ?> query, RequestOptions options) {
        return call("POST", base, query, body(body), options, TrutoRecord.class);
    }

    /**
     * Deletes a sandbox integrated account.
     *
     * @param id id
     * @return the response
     */
    public TrutoRecord delete(String id) {
        return delete(id, null, null);
    }

    /**
     * Deletes a sandbox integrated account.
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord delete(String id, Map<String, ?> query, RequestOptions options) {
        return call("DELETE", at(id), query, null, options, TrutoRecord.class);
    }
}
