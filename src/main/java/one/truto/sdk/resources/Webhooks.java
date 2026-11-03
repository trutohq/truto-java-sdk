package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.DeleteResponse;
import one.truto.sdk.models.WebhookCreateRequest;
import one.truto.sdk.models.WebhookCreateResponse;
import one.truto.sdk.models.WebhookSchema;
import one.truto.sdk.models.WebhookTestRequest;
import one.truto.sdk.models.WebhookTestResponse;
import one.truto.sdk.models.WebhookUpdateRequest;

/** Outbound webhooks. Path: {@code /webhook}. */
public class Webhooks extends CrudResource<WebhookSchema, WebhookCreateRequest, WebhookCreateResponse, WebhookUpdateRequest, DeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Webhooks(ApiClient api) {
        super(api, "webhook", WebhookSchema.class, WebhookCreateResponse.class, DeleteResponse.class);
    }

    /**
     * Sends a test event to a webhook ({@code POST /webhook/test}).
     *
     * @param body body
     * @return the response
     */
    public WebhookTestResponse test(WebhookTestRequest body) {
        return test(body, null, null);
    }

    /**
     * Sends a test event to a webhook ({@code POST /webhook/test}).
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public WebhookTestResponse test(WebhookTestRequest body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("test"), query, body(body), options, WebhookTestResponse.class);
    }
}
