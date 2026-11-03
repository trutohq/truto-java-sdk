package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;

/**
 * A resource with {@code list}, {@code get}, {@code create}, {@code update} and {@code delete}.
 *
 * @param <T> the item type (list, get, update)
 * @param <C> the create request type
 * @param <R> the create response type
 * @param <U> the update request type
 * @param <D> the delete response type
 */
public abstract class CrudResource<T, C, R, U, D> extends ReadResource<T> {
    private final Class<R> createType;
    private final Class<D> deleteType;

    /**
     * Creates the resource.
     *
     * @param api the HTTP layer
     * @param base the base path
     * @param itemType the item type
     * @param createType the create response type
     * @param deleteType the delete response type
     */
    protected CrudResource(ApiClient api, String base, Class<T> itemType, Class<R> createType, Class<D> deleteType) {
        super(api, base, itemType);
        this.createType = createType;
        this.deleteType = deleteType;
    }

    /**
     * Creates an item.
     *
     * @param body the request body
     * @return the created item
     */
    public R create(C body) {
        return create(body, null, null);
    }

    /**
     * Creates an item.
     *
     * @param body the request body
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the created item
     */
    public R create(C body, Map<String, ?> query, RequestOptions options) {
        return call("POST", base, query, body(body), options, createType);
    }

    /**
     * Updates an item (PATCH).
     *
     * @param id the ID
     * @param body the fields to change
     * @return the updated item
     */
    public T update(String id, U body) {
        return update(id, body, null, null);
    }

    /**
     * Updates an item (PATCH).
     *
     * @param id the ID
     * @param body the fields to change
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the updated item
     */
    public T update(String id, U body, Map<String, ?> query, RequestOptions options) {
        return call("PATCH", at(id), query, body(body), options, itemType);
    }

    /**
     * Deletes an item.
     *
     * @param id the ID
     * @return the delete response
     */
    public D delete(String id) {
        return delete(id, null, null);
    }

    /**
     * Deletes an item.
     *
     * @param id the ID
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the delete response
     */
    public D delete(String id, Map<String, ?> query, RequestOptions options) {
        return call("DELETE", at(id), query, null, options, deleteType);
    }
}
