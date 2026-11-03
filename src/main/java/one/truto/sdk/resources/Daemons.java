package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.TrutoRecord;

/** Daemons. Path: {@code /daemon}. */
public class Daemons extends CrudResource<TrutoRecord, Object, TrutoRecord, Object, TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Daemons(ApiClient api) {
        super(api, "daemon", TrutoRecord.class, TrutoRecord.class, TrutoRecord.class);
    }
}
