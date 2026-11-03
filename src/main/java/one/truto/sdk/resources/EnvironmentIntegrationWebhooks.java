package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/** Inbound webhooks addressed to an environment integration. Path: {@code /environment-integration-webhook}. */
public class EnvironmentIntegrationWebhooks extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public EnvironmentIntegrationWebhooks(ApiClient api) {
        super(api, "environment-integration-webhook");
    }

    /**
     * Delivers a webhook payload for processing.
     *
     * @param environmentIntegrationId environmentIntegration ID
     * @param body the webhook payload
     * @return the response
     */
    public TrutoRecord process(String environmentIntegrationId, Object body) {
        return process(environmentIntegrationId, body, null, null);
    }

    /**
     * Delivers a webhook payload for processing.
     *
     * @param environmentIntegrationId environmentIntegration ID
     * @param body the webhook payload
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord process(String environmentIntegrationId, Object body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(environmentIntegrationId), query, body(body), options, TrutoRecord.class);
    }
}
