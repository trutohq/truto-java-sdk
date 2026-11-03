package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.TrutoRecord;
import one.truto.sdk.models.WorkflowRunResponse;

/** Workflow runs. Path: {@code /workflow-run}. */
public class WorkflowRuns extends CrudResource<WorkflowRunResponse, Object, TrutoRecord, Object, TrutoRecord> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public WorkflowRuns(ApiClient api) {
        super(api, "workflow-run", WorkflowRunResponse.class, TrutoRecord.class, TrutoRecord.class);
    }
}
