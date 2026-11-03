package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.DatastoreCreateRequest;
import one.truto.sdk.models.DatastoreDeleteResponse;
import one.truto.sdk.models.DatastoreSchema;
import one.truto.sdk.models.DatastoreUpdateRequest;
import one.truto.sdk.models.TrutoRecord;

/** Datastores that sync jobs write to. Path: {@code /datastore}. */
public class Datastores extends CrudResource<DatastoreSchema, DatastoreCreateRequest, DatastoreSchema, DatastoreUpdateRequest, DatastoreDeleteResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Datastores(ApiClient api) {
        super(api, "datastore", DatastoreSchema.class, DatastoreSchema.class, DatastoreDeleteResponse.class);
    }

    /**
     * Tests a datastore connection ({@code POST /datastore/{id}/test/{method}}).
     *
     * @param id id
     * @param method the test method
     * @param body the request body, may be null
     * @return the response
     */
    public TrutoRecord test(String id, String method, Object body) {
        return test(id, method, body, null, null);
    }

    /**
     * Tests a datastore connection ({@code POST /datastore/{id}/test/{method}}).
     *
     * @param id id
     * @param method the test method
     * @param body the request body, may be null
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TrutoRecord test(String id, String method, Object body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at(id, "test", method), query, body, options, TrutoRecord.class);
    }
}
