package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.Pager;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.CategorySchema;

/** Integration categories. Path: {@code /category}. */
public class Categories extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Categories(ApiClient api) {
        super(api, "category");
    }

    /**
     * Lists integration categories.
     *
     * @return a pager over all items
     */
    public Pager<CategorySchema> list() {
        return list(null, null);
    }

    /**
     * Lists integration categories.
     *
     * @param query the query, may be null
     * @return a pager over all matching items
     */
    public Pager<CategorySchema> list(Map<String, ?> query) {
        return list(query, null);
    }

    /**
     * Lists integration categories.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return a pager over all matching items
     */
    public Pager<CategorySchema> list(Map<String, ?> query, RequestOptions options) {
        return pager(base, query, options, CategorySchema.class);
    }
}
