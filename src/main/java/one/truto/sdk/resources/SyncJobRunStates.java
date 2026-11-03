package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.SyncJobRunState;
import one.truto.sdk.models.SyncJobRunStateCreateRequest;
import one.truto.sdk.models.SyncJobRunStateUpdateRequest;

/** Persisted state of sync job runs. Path: {@code /sync-job-run-state}. */
public class SyncJobRunStates extends CrudResource<SyncJobRunState, SyncJobRunStateCreateRequest, SyncJobRunState, SyncJobRunStateUpdateRequest, SyncJobRunState> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public SyncJobRunStates(ApiClient api) {
        super(api, "sync-job-run-state", SyncJobRunState.class, SyncJobRunState.class, SyncJobRunState.class);
    }
}
