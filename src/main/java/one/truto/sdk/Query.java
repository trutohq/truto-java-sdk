package one.truto.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A small fluent builder for query parameters. It is a plain {@code Map<String, Object>}, so it
 * can be passed anywhere a query map is accepted.
 *
 * <pre>{@code
 * Query q = Query.of("integrated_account_id", id).with("limit", 50);
 * }</pre>
 */
public final class Query extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /** Creates an empty query. */
    public Query() {
        super();
    }

    /**
     * Creates a query with one parameter.
     *
     * @param key the parameter name
     * @param value the value (String, Number, Boolean, null, Map, List, ...)
     * @return the query
     */
    public static Query of(String key, Object value) {
        return new Query().with(key, value);
    }

    /**
     * Creates a query from an existing map (copied).
     *
     * @param values the parameters
     * @return the query
     */
    public static Query from(Map<String, ?> values) {
        Query q = new Query();
        if (values != null) {
            q.putAll(values);
        }
        return q;
    }

    /**
     * Sets a parameter and returns this query.
     *
     * @param key the parameter name
     * @param value the value
     * @return this
     */
    public Query with(String key, Object value) {
        put(key, value);
        return this;
    }

    /**
     * Encodes the query string (no leading {@code ?}).
     *
     * @return the encoded query
     */
    public String encode() {
        return QueryString.encode(this);
    }
}
