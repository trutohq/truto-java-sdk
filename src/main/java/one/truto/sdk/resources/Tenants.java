package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.TenantBulkCreateResponse;
import one.truto.sdk.models.TenantBulkRequest;
import one.truto.sdk.models.TenantCreateRequest;
import one.truto.sdk.models.TenantResponse;
import one.truto.sdk.models.TenantUpdateRequest;

/** Tenants (end customers) in the environment. Path: {@code /tenant}. */
public class Tenants extends CrudResource<TenantResponse, TenantCreateRequest, TenantResponse, TenantUpdateRequest, TenantResponse> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Tenants(ApiClient api) {
        super(api, "tenant", TenantResponse.class, TenantResponse.class, TenantResponse.class);
    }

    /**
     * Creates many tenants at once ({@code POST /tenant/bulk}).
     *
     * @param body body
     * @return the response
     */
    public TenantBulkCreateResponse bulkCreate(TenantBulkRequest body) {
        return bulkCreate(body, null, null);
    }

    /**
     * Creates many tenants at once ({@code POST /tenant/bulk}).
     *
     * @param body body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public TenantBulkCreateResponse bulkCreate(TenantBulkRequest body, Map<String, ?> query, RequestOptions options) {
        return call("POST", at("bulk"), query, body(body), options, TenantBulkCreateResponse.class);
    }
}
