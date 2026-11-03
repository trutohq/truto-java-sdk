package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.TrutoRecord;

/** Users in the team. Path: {@code /user}. */
public class Users extends ReadResource<TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Users(ApiClient api) {
        super(api, "user", TrutoRecord.class);
    }
}
