package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.TrutoRecord;

/** Alarms. Path: {@code /alarm}. */
public class Alarms extends CrudResource<TrutoRecord, Object, TrutoRecord, Object, TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Alarms(ApiClient api) {
        super(api, "alarm", TrutoRecord.class, TrutoRecord.class, TrutoRecord.class);
    }
}
