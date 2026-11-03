package one.truto.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.List;

/**
 * One page of a list response: {@code { "result": [...], "next_cursor": "..." }}.
 *
 * @param <T> the item type
 */
public final class Page<T> {
    private final List<T> items;
    private final String nextCursor;
    private final JsonNode raw;

    /**
     * Creates a page.
     *
     * @param items the items
     * @param nextCursor the cursor for the next page, or null when this is the last page
     * @param raw the full response body, or null
     */
    public Page(List<T> items, String nextCursor, JsonNode raw) {
        this.items = items == null ? Collections.emptyList() : Collections.unmodifiableList(items);
        this.nextCursor = nextCursor == null || nextCursor.isEmpty() ? null : nextCursor;
        this.raw = raw;
    }

    /**
     * The items on this page.
     *
     * @return the items
     */
    public List<T> items() {
        return items;
    }

    /**
     * The cursor for the next page, or null when there are no more pages.
     *
     * @return the cursor
     */
    public String nextCursor() {
        return nextCursor;
    }

    /**
     * Whether another page follows.
     *
     * @return true when {@link #nextCursor()} is set
     */
    public boolean hasNext() {
        return nextCursor != null;
    }

    /**
     * The full response body, for fields beyond {@code result} and {@code next_cursor}.
     *
     * @return the JSON body, or null
     */
    public JsonNode raw() {
        return raw;
    }

    @Override
    public String toString() {
        return "Page{items=" + items.size() + ", nextCursor=" + nextCursor + "}";
    }
}
