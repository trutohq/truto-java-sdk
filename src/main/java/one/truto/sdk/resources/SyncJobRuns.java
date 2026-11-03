package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.SyncJobRunCreateRequest;
import one.truto.sdk.models.SyncJobRunResponse;

/** Sync job runs. Path: {@code /sync-job-run}. */
public class SyncJobRuns extends CrudResource<SyncJobRunResponse, SyncJobRunCreateRequest, SyncJobRunResponse, Object, SyncJobRunResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public SyncJobRuns(ApiClient api) {
        super(api, "sync-job-run", SyncJobRunResponse.class, SyncJobRunResponse.class, SyncJobRunResponse.class);
    }
}
