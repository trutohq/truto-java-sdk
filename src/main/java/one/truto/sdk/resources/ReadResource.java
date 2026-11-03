package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.Pager;
import one.truto.sdk.RequestOptions;

/**
 * A resource with {@code list} and {@code get}.
 *
 * @param <T> the item type
 */
public abstract class ReadResource<T> extends BaseResource {
    /** The item type. */
    protected final Class<T> itemType;

    /**
     * Creates the resource.
     *
     * @param api the HTTP layer
     * @param base the base path
     * @param itemType the item type
     */
    protected ReadResource(ApiClient api, String base, Class<T> itemType) {
        super(api, base);
        this.itemType = itemType;
    }

    /**
     * Lists items. Nothing is fetched until the pager is used.
     *
     * @return a pager over all items
     */
    public Pager<T> list() {
        return list(null, null);
    }

    /**
     * Lists items matching a query (filters, {@code limit}, ...).
     *
     * @param query the query, may be null
     * @return a pager over all matching items
     */
    public Pager<T> list(Map<String, ?> query) {
        return list(query, null);
    }

    /**
     * Lists items matching a query.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return a pager over all matching items
     */
    public Pager<T> list(Map<String, ?> query, RequestOptions options) {
        return pager(base, query, options, itemType);
    }

    /**
     * Fetches one item.
     *
     * @param id the ID
     * @return the item
     */
    public T get(String id) {
        return get(id, null, null);
    }

    /**
     * Fetches one item.
     *
     * @param id the ID
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the item
     */
    public T get(String id, Map<String, ?> query, RequestOptions options) {
        return call("GET", at(id), query, null, options, itemType);
    }
}
