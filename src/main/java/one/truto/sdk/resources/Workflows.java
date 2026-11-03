package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.WorkflowCreateSchema;
import one.truto.sdk.models.WorkflowDeleteResponse;
import one.truto.sdk.models.WorkflowSchema;

/** Workflows. Path: {@code /workflow}. */
public class Workflows extends CrudResource<WorkflowSchema, WorkflowCreateSchema, WorkflowSchema, Object, WorkflowDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Workflows(ApiClient api) {
        super(api, "workflow", WorkflowSchema.class, WorkflowSchema.class, WorkflowDeleteResponse.class);
    }
}
