package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/** Inbound webhooks addressed to an integrated account. Path: {@code /integrated-account-webhook}. */
public class IntegratedAccountWebhooks extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public IntegratedAccountWebhooks(ApiClient api) {
        super(api, "integrated-account-webhook");
    }

    /**
     * Delivers a webhook payload for processing.
     *
     * @param integratedAccountId integratedAccount ID
     * @param body the webhook payload
     * @return the response
     */
    public TrutoRecord process(String integratedAccountId, Object body) {
        return process(integratedAccountId, body, null, null);
    }

    /**
     * Delivers a webhook payload for processing.
     *
     * @param integratedAccountId integratedAccount ID
     * @param body the webhook payload
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord process(String integratedAccountId, Object body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(integratedAccountId), query, body(body), options, TrutoRecord.class);
    }
}
