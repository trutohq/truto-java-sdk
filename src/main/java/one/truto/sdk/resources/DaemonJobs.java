package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.TrutoRecord;

/** Daemon jobs. Path: {@code /daemon-job}. */
public class DaemonJobs extends CrudResource<TrutoRecord, Object, TrutoRecord, Object, TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public DaemonJobs(ApiClient api) {
        super(api, "daemon-job", TrutoRecord.class, TrutoRecord.class, TrutoRecord.class);
    }
}
