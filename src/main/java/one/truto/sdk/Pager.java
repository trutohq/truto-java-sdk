package one.truto.sdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Cursor pager over a list endpoint.
 *
 * <p>Nothing is fetched until it is used. Iterating (for-each, {@link #stream()},
 * {@link #toList()}, {@link #first()}) always starts from the first page and fetches further
 * pages lazily; {@link #nextPage()} walks the pages one call at a time. The caller's query map is
 * copied and never modified.
 *
 * <pre>{@code
 * for (IntegratedAccountResponse a : client.integratedAccounts().list()) { ... }
 * Page<TrutoRecord> page = client.unifiedApi().list("crm", "contacts", accountId).nextPage();
 * }</pre>
 *
 * @param <T> the item type
 */
public final class Pager<T> implements Iterable<T> {
    /**
     * Fetches one page for a query.
     *
     * @param <T> the item type
     */
    @FunctionalInterface
    public interface PageFetcher<T> {
        /**
         * Fetches a page.
         *
         * @param query the query to send (owned by the pager; do not keep it)
         * @return the page
         */
        Page<T> fetch(Map<String, Object> query);
    }

    private final Map<String, Object> query;
    private final PageFetcher<T> fetcher;
    private String cursor;
    private boolean started;
    private boolean done;

    /**
     * Creates a pager.
     *
     * @param query the caller's query (copied), may be null
     * @param fetcher fetches a page for a query
     */
    public Pager(Map<String, ?> query, PageFetcher<T> fetcher) {
        Map<String, Object> q = new LinkedHashMap<>();
        if (query != null) {
            q.putAll(query);
        }
        Object nc = q.get("next_cursor");
        if (nc == null || nc.toString().isEmpty()) {
            q.remove("next_cursor");
        }
        this.query = Collections.unmodifiableMap(q);
        this.fetcher = fetcher;
    }

    private Page<T> fetch(String nextCursor) {
        Map<String, Object> q = new LinkedHashMap<>(query);
        if (nextCursor != null) {
            q.put("next_cursor", nextCursor);
        }
        return fetcher.fetch(q);
    }

    /**
     * Whether {@link #nextPage()} has another page to return.
     *
     * @return true until the last page has been returned
     */
    public synchronized boolean hasNextPage() {
        return !done;
    }

    /**
     * Fetches the next page (the first page on the first call).
     *
     * @return the page
     * @throws NoSuchElementException after the last page has been returned
     * @throws TrutoException if the request fails
     */
    public synchronized Page<T> nextPage() {
        if (done) {
            throw new NoSuchElementException("no more pages");
        }
        Page<T> page = fetch(started ? cursor : null);
        started = true;
        cursor = page.nextCursor();
        done = cursor == null;
        return page;
    }

    /**
     * All pages, fetched lazily from the first page each time it is iterated.
     *
     * @return the pages
     */
    public Iterable<Page<T>> pages() {
        return () -> new Iterator<Page<T>>() {
            private boolean first = true;
            private String next;

            @Override
            public boolean hasNext() {
                return first || next != null;
            }

            @Override
            public Page<T> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Page<T> page = fetch(first ? null : next);
                first = false;
                next = page.nextCursor();
                return page;
            }
        };
    }

    /**
     * Iterates every item across all pages, fetching pages on demand. Empty pages that still carry
     * a cursor are skipped over.
     *
     * @return the iterator
     */
    @Override
    public Iterator<T> iterator() {
        Iterator<Page<T>> pages = pages().iterator();
        return new Iterator<T>() {
            private Iterator<T> current = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                while (!current.hasNext()) {
                    if (!pages.hasNext()) {
                        return false;
                    }
                    current = pages.next().items().iterator();
                }
                return true;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return current.next();
            }
        };
    }

    /**
     * A lazy sequential stream over every item.
     *
     * @return the stream
     */
    public Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }

    /**
     * Fetches every page and collects all items.
     *
     * @return the items
     */
    public List<T> toList() {
        List<T> out = new ArrayList<>();
        for (T item : this) {
            out.add(item);
        }
        return out;
    }

    /**
     * The first item, fetching only as many pages as needed.
     *
     * @return the first item, or empty when there are none
     */
    public Optional<T> first() {
        Iterator<T> it = iterator();
        return it.hasNext() ? Optional.ofNullable(it.next()) : Optional.empty();
    }

    /**
     * A pager whose items are transformed, for example into your own type:
     * {@code pager.map(r -> r.as(Contact.class))}.
     *
     * @param fn the transform
     * @param <R> the new item type
     * @return the mapped pager (starting from the first page)
     */
    public <R> Pager<R> map(Function<? super T, ? extends R> fn) {
        return new Pager<R>(query, q -> {
            Page<T> p = fetcher.fetch(q);
            List<R> items = new ArrayList<>(p.items().size());
            for (T t : p.items()) {
                items.add(fn.apply(t));
            }
            return new Page<R>(items, p.nextCursor(), p.raw());
        });
    }
}
