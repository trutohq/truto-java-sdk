package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.SyncJobTemplateCreate;
import one.truto.sdk.models.SyncJobTemplateDeleteResponse;
import one.truto.sdk.models.SyncJobTemplatePatch;
import one.truto.sdk.models.SyncJobTemplateSchema;

/** Sync job templates. Path: {@code /sync-job-template}. */
public class SyncJobTemplates extends CrudResource<SyncJobTemplateSchema, SyncJobTemplateCreate, SyncJobTemplateSchema, SyncJobTemplatePatch, SyncJobTemplateDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public SyncJobTemplates(ApiClient api) {
        super(api, "sync-job-template", SyncJobTemplateSchema.class, SyncJobTemplateSchema.class, SyncJobTemplateDeleteResponse.class);
    }
}
