package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.LinkToken;
import one.truto.sdk.models.LinkTokenRequest;

/** Link tokens for the connect flow. Path: {@code /link-token}. */
public class LinkTokens extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public LinkTokens(ApiClient api) {
        super(api, "link-token");
    }

    /**
     * Creates a link token.
     *
     * @param body body
     * @return the response
     */
    public LinkToken create(LinkTokenRequest body) {
        return create(body, null, null);
    }

    /**
     * Creates a link token.
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public LinkToken create(LinkTokenRequest body, Map<String, ?> query, RequestOptions options) {
        return call("POST", base, query, body(body), options, LinkToken.class);
    }
}
