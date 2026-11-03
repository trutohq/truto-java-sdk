package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.DeleteResponse;
import one.truto.sdk.models.SyncJobCreateRequest;
import one.truto.sdk.models.SyncJobSchema;
import one.truto.sdk.models.SyncJobUpdateRequest;

/** RapidBridge sync jobs. Path: {@code /sync-job}. */
public class SyncJobs extends CrudResource<SyncJobSchema, SyncJobCreateRequest, SyncJobSchema, SyncJobUpdateRequest, DeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public SyncJobs(ApiClient api) {
        super(api, "sync-job", SyncJobSchema.class, SyncJobSchema.class, DeleteResponse.class);
    }
}
