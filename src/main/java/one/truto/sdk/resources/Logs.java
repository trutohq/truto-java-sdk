package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.Pager;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.Log;

/** Logs. The server requires the {@code log_type} query parameter (for example {@code unified_proxy_api}, {@code rapid_bridge}, {@code webhook}). Path: {@code /log}. */
public class Logs extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Logs(ApiClient api) {
        super(api, "log");
    }

    /**
     * Lists log entries. Pass {@code log_type} in the query; the server rejects requests without it. Each entry is a {@link Log} union; use {@code asX()} for the variant matching {@code log_type}.
     *
     * @return a pager over all items
     */
    public Pager<Log> list() {
        return list(null, null);
    }

    /**
     * Lists log entries. Pass {@code log_type} in the query; the server rejects requests without it. Each entry is a {@link Log} union; use {@code asX()} for the variant matching {@code log_type}.
     *
     * @param query the query, may be null
     * @return a pager over all matching items
     */
    public Pager<Log> list(Map<String, ?> query) {
        return list(query, null);
    }

    /**
     * Lists log entries. Pass {@code log_type} in the query; the server rejects requests without it. Each entry is a {@link Log} union; use {@code asX()} for the variant matching {@code log_type}.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return a pager over all matching items
     */
    public Pager<Log> list(Map<String, ?> query, RequestOptions options) {
        return pager(base, query, options, Log.class);
    }
}
