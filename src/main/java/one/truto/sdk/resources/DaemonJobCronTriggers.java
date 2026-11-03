package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TrutoRecord;

/** Cron triggers for daemon jobs. Path: {@code /daemon-job-cron-trigger}. */
public class DaemonJobCronTriggers extends CrudResource<TrutoRecord, Object, TrutoRecord, Object, TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public DaemonJobCronTriggers(ApiClient api) {
        super(api, "daemon-job-cron-trigger", TrutoRecord.class, TrutoRecord.class, TrutoRecord.class);
    }

    /**
     * Schedules the trigger now ({@code POST {id}/schedule}).
     *
     * @param id id
     * @return the response
     */
    public TrutoRecord schedule(String id) {
        return schedule(id, null, null);
    }

    /**
     * Schedules the trigger now ({@code POST {id}/schedule}).
     *
     * @param id id
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord schedule(String id, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(id, "schedule"), query, null, options, TrutoRecord.class);
    }
}
