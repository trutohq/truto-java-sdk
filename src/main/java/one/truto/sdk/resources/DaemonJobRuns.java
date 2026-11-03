package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.TrutoRecord;

/** Daemon job runs. Path: {@code /daemon-job-run}. */
public class DaemonJobRuns extends CrudResource<TrutoRecord, Object, TrutoRecord, Object, TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public DaemonJobRuns(ApiClient api) {
        super(api, "daemon-job-run", TrutoRecord.class, TrutoRecord.class, TrutoRecord.class);
    }
}
