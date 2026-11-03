package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/** Teams. Path: {@code /team}. */
public class Teams extends ReadResource<TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Teams(ApiClient api) {
        super(api, "team", TrutoRecord.class);
    }

    /**
     * Updates a team.
     *
     * @param id id
     * @param body the fields to change
     * @return the response
     */
    public TrutoRecord update(String id, Object body) {
        return update(id, body, null, null);
    }

    /**
     * Updates a team.
     *
     * @param id id
     * @param body the fields to change
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord update(String id, Object body, Map<String, ?> query, RequestOptions options) {
        return call("PATCH", at(id), query, body(body), options, TrutoRecord.class);
    }
}
