package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.SessionTokenDeleteRequest;
import one.truto.sdk.models.SessionTokenDeleteResponse;
import one.truto.sdk.models.SessionTokenMeResponse;
import one.truto.sdk.models.SessionTokenRenewResponse;
import one.truto.sdk.models.SessionTokenRequest;
import one.truto.sdk.models.SessionTokenResponse;

/** Session tokens. Path: {@code /session-token}. */
public class SessionTokens extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public SessionTokens(ApiClient api) {
        super(api, "session-token");
    }

    /**
     * Creates a session token.
     *
     * @param body body
     * @return the response
     */
    public SessionTokenResponse create(SessionTokenRequest body) {
        return create(body, null, null);
    }

    /**
     * Creates a session token.
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public SessionTokenResponse create(SessionTokenRequest body, Map<String, ?> query, RequestOptions options) {
        return call("POST", base, query, body(body), options, SessionTokenResponse.class);
    }

    /**
     * Revokes a session token ({@code DELETE /session-token} with a JSON body).
     *
     * @param body body
     * @return the response
     */
    public SessionTokenDeleteResponse revoke(SessionTokenDeleteRequest body) {
        return revoke(body, null, null);
    }

    /**
     * Revokes a session token ({@code DELETE /session-token} with a JSON body).
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public SessionTokenDeleteResponse revoke(SessionTokenDeleteRequest body, Map<String, ?> query, RequestOptions options) {
        return call("DELETE", base, query, body(body), options, SessionTokenDeleteResponse.class);
    }

    /**
     * Renews the session token used for this request ({@code POST /session-token/renew}).
     *
     * @return the response
     */
    public SessionTokenRenewResponse renew() {
        return renew(null, null);
    }

    /**
     * Renews the session token used for this request ({@code POST /session-token/renew}).
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public SessionTokenRenewResponse renew(Map<String, ?> query, RequestOptions options) {
        return call("POST", at("renew"), query, null, options, SessionTokenRenewResponse.class);
    }

    /**
     * Describes the session token used for this request ({@code GET /session-token/me}).
     *
     * @return the response
     */
    public SessionTokenMeResponse me() {
        return me(null, null);
    }

    /**
     * Describes the session token used for this request ({@code GET /session-token/me}).
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public SessionTokenMeResponse me(Map<String, ?> query, RequestOptions options) {
        return call("GET", at("me"), query, null, options, SessionTokenMeResponse.class);
    }
}
