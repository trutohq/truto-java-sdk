package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.DeleteResponse;
import one.truto.sdk.models.SyncJobCronTriggerCreateRequest;
import one.truto.sdk.models.SyncJobCronTriggerSchema;
import one.truto.sdk.models.SyncJobCronTriggerUpdateRequest;
import one.truto.sdk.models.TrutoRecord;

/** Cron triggers for sync jobs. Path: {@code /sync-job-cron-trigger}. */
public class SyncJobCronTriggers extends CrudResource<SyncJobCronTriggerSchema, SyncJobCronTriggerCreateRequest, SyncJobCronTriggerSchema, SyncJobCronTriggerUpdateRequest, DeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public SyncJobCronTriggers(ApiClient api) {
        super(api, "sync-job-cron-trigger", SyncJobCronTriggerSchema.class, SyncJobCronTriggerSchema.class, DeleteResponse.class);
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
