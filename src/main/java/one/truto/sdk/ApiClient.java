package one.truto.sdk;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import one.truto.sdk.models.TrutoRecord;

/**
 * The HTTP layer behind every resource: builds URLs, sends requests, retries, and decodes
 * responses. Obtain it from {@link TrutoClient#http()} to call endpoints the SDK does not wrap.
 * Thread-safe.
 */
public final class ApiClient {
    static final Set<Integer> DEFAULT_RETRY_STATUS_CODES =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(408, 409, 425, 429, 502, 503, 504)));
    private static final long BACKOFF_BASE_MS = 500;
    private static final long BACKOFF_CAP_MS = 10_000;

    private final String baseUrl;
    private final String token;
    private final HttpClient http;
    private final Duration timeout;
    private final int maxRetries;
    private final Duration defaultRetryAfter;
    private final RetryListener onRetry;
    private final RateLimitListener onRateLimited;

    ApiClient(String baseUrl, String token, HttpClient http, Duration timeout, int maxRetries,
            Duration defaultRetryAfter, RetryListener onRetry, RateLimitListener onRateLimited) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.http = http;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.defaultRetryAfter = defaultRetryAfter;
        this.onRetry = onRetry;
        this.onRateLimited = onRateLimited;
    }

    /**
     * The base URL requests are sent to (no trailing slash).
     *
     * @return the base URL
     */
    public String baseUrl() {
        return baseUrl;
    }

    // ---------------------------------------------------------------- paths

    /**
     * Joins path segments, percent-encoding each one (so IDs containing {@code /} or {@code ?}
     * stay inside their segment).
     *
     * @param segments the segments
     * @return the path, without a leading slash
     * @throws IllegalArgumentException if a segment is null or empty
     */
    public static String path(String... segments) {
        StringBuilder sb = new StringBuilder();
        for (String s : segments) {
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("path parameter must not be null or empty");
            }
            if (sb.length() > 0) {
                sb.append('/');
            }
            sb.append(QueryString.encodeComponent(s));
        }
        return sb.toString();
    }

    /**
     * Encodes a caller-supplied multi-segment path (custom API). Slashes are kept; each segment
     * is percent-encoded, but existing {@code %XX} escapes are left alone.
     *
     * @param raw the path, with or without a leading slash
     * @return the encoded path without a leading slash
     */
    public static String rawPath(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("path must not be null");
        }
        String p = raw;
        while (p.startsWith("/")) {
            p = p.substring(1);
        }
        StringBuilder sb = new StringBuilder();
        String keep = "-_.~!$&'()*+,;=:@";
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            boolean escape = c == '%' && i + 2 < p.length() && isHex(p.charAt(i + 1)) && isHex(p.charAt(i + 2));
            if (c == '/' || escape || (c < 128 && (Character.isLetterOrDigit(c) || keep.indexOf(c) >= 0))) {
                sb.append(c);
            } else {
                int end = i + 1;
                if (Character.isHighSurrogate(c) && end < p.length()) {
                    end++;
                }
                sb.append(QueryString.encodeComponent(p.substring(i, end)));
                i = end - 1;
            }
        }
        return sb.toString();
    }

    private static boolean isHex(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    // ---------------------------------------------------------------- typed helpers

    /**
     * Sends a request and decodes the JSON response.
     *
     * @param method HTTP method
     * @param path path relative to the base URL
     * @param query query parameters, may be null
     * @param body JSON body, or null for none
     * @param options per-request options, may be null
     * @param type the response type ({@code Void.class} to ignore the body)
     * @param <T> the response type
     * @return the decoded response; null for an empty body (an empty {@link TrutoRecord} when
     *     {@code type} is TrutoRecord)
     */
    public <T> T request(String method, String path, Map<String, ?> query, Object body,
            RequestOptions options, Class<T> type) {
        return decode(send(method, path, query, body, options), Json.shared().constructType(type));
    }

    /**
     * Async form of {@link #request}.
     *
     * @param method HTTP method
     * @param path path relative to the base URL
     * @param query query parameters, may be null
     * @param body JSON body, or null for none
     * @param options per-request options, may be null
     * @param type the response type
     * @param <T> the response type
     * @return a future of the decoded response
     */
    public <T> CompletableFuture<T> requestAsync(String method, String path, Map<String, ?> query,
            Object body, RequestOptions options, Class<T> type) {
        JavaType jt = Json.shared().constructType(type);
        return sendAsync(method, path, query, body, options).thenApply(r -> decode(r, jt));
    }

    /**
     * Fetches one page of a list endpoint.
     *
     * @param path path relative to the base URL
     * @param query query parameters, may be null
     * @param options per-request options, may be null
     * @param itemType the item type
     * @param <T> the item type
     * @return the page
     */
    public <T> Page<T> page(String path, Map<String, ?> query, RequestOptions options, Class<T> itemType) {
        return decodePage(send("GET", path, query, null, options), itemType);
    }

    /**
     * Async form of {@link #page}.
     *
     * @param path path relative to the base URL
     * @param query query parameters, may be null
     * @param options per-request options, may be null
     * @param itemType the item type
     * @param <T> the item type
     * @return a future of the page
     */
    public <T> CompletableFuture<Page<T>> pageAsync(String path, Map<String, ?> query,
            RequestOptions options, Class<T> itemType) {
        return sendAsync("GET", path, query, null, options).thenApply(r -> decodePage(r, itemType));
    }

    /**
     * A lazy pager over a list endpoint.
     *
     * @param path path relative to the base URL
     * @param query query parameters (copied), may be null
     * @param options per-request options, may be null
     * @param itemType the item type
     * @param <T> the item type
     * @return the pager
     */
    public <T> Pager<T> pager(String path, Map<String, ?> query, RequestOptions options, Class<T> itemType) {
        return new Pager<>(query, q -> page(path, q, options, itemType));
    }

    <T> T decode(RawResponse r, JavaType type) {
        Class<?> raw = type.getRawClass();
        if (raw == Void.class || raw == void.class) {
            return null;
        }
        if (raw == RawResponse.class) {
            @SuppressWarnings("unchecked")
            T same = (T) r;
            return same;
        }
        if (r.statusCode() == 204 || r.bodyUnsafe().length == 0) {
            if (raw == TrutoRecord.class) {
                @SuppressWarnings("unchecked")
                T empty = (T) new TrutoRecord();
                return empty;
            }
            return null;
        }
        if (!r.isJson()) {
            throw new TrutoDecodeException("expected a JSON response but got content-type "
                    + r.contentType(), r, null);
        }
        try {
            return Json.read(r.bodyUnsafe(), type);
        } catch (TrutoDecodeException e) {
            throw new TrutoDecodeException(e.getMessage(), r, e.getCause());
        }
    }

    <T> Page<T> decodePage(RawResponse r, Class<T> itemType) {
        if (r.statusCode() == 204 || r.bodyUnsafe().length == 0) {
            return new Page<>(Collections.emptyList(), null, null);
        }
        if (!r.isJson()) {
            throw new TrutoDecodeException("expected a JSON response but got content-type "
                    + r.contentType(), r, null);
        }
        JsonNode tree;
        try {
            tree = Json.readTree(r.bodyUnsafe());
        } catch (TrutoDecodeException e) {
            throw new TrutoDecodeException(e.getMessage(), r, e.getCause());
        }
        JsonNode arr = tree.get("result");
        if (arr == null || arr.isNull()) {
            arr = tree.get("results");
        }
        List<T> items = new ArrayList<>();
        if (arr != null && arr.isArray()) {
            for (JsonNode item : arr) {
                try {
                    items.add(Json.fromTree(item, itemType));
                } catch (TrutoDecodeException e) {
                    throw new TrutoDecodeException(e.getMessage(), r, e.getCause());
                }
            }
        }
        JsonNode nc = tree.get("next_cursor");
        String cursor = nc == null || nc.isNull() ? null : nc.asText();
        return new Page<>(items, cursor, tree);
    }

    // ---------------------------------------------------------------- transport

    private final class Call {
        final HttpRequest request;
        final int maxRetries;
        final boolean noRetry;
        final Set<Integer> retryStatusCodes;
        final RetryListener onRetry;
        final RateLimitListener onRateLimited;

        Call(String method, String path, Map<String, ?> query, Object body, RequestOptions o) {
            RequestOptions opts = o == null ? RequestOptions.none() : o;
            String qs = QueryString.encode(query);
            String p = path.startsWith("/") ? path.substring(1) : path;
            String url = baseUrl + "/" + p + (qs.isEmpty() ? "" : "?" + qs);
            HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(url))
                    .timeout(opts.timeout() != null ? opts.timeout() : timeout)
                    .setHeader("Authorization", "Bearer " + token)
                    .setHeader("Accept", "application/json")
                    .setHeader("User-Agent", "truto-java-sdk/" + TrutoClient.VERSION);
            HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.noBody();
            if (body != null) {
                b.setHeader("Content-Type", "application/json");
                publisher = HttpRequest.BodyPublishers.ofByteArray(Json.toJsonBytes(body));
            }
            for (Map.Entry<String, String> h : opts.headers().entrySet()) {
                b.setHeader(h.getKey(), h.getValue());
            }
            b.method(method, publisher);
            this.request = b.build();
            this.maxRetries = opts.maxRetries() != null ? opts.maxRetries() : ApiClient.this.maxRetries;
            this.noRetry = opts.noRetry();
            Set<Integer> codes = new HashSet<>(DEFAULT_RETRY_STATUS_CODES);
            codes.addAll(opts.retryStatusCodes());
            this.retryStatusCodes = codes;
            this.onRetry = opts.onRetry() != null ? opts.onRetry() : ApiClient.this.onRetry;
            this.onRateLimited = opts.onRateLimited() != null ? opts.onRateLimited() : ApiClient.this.onRateLimited;
        }

        boolean canRetry(int retries) {
            return !noRetry && retries < maxRetries;
        }

        /** Milliseconds to wait before retrying this response, or -1 to give up. */
        long retryDelay(RawResponse r, int retries) {
            if (!canRetry(retries) || !retryStatusCodes.contains(r.statusCode())) {
                return -1;
            }
            if (r.statusCode() == 429) {
                long seconds = parseRetryAfter(r.header("retry-after"));
                if (onRateLimited != null) {
                    onRateLimited.onRateLimited(seconds, retries);
                }
                return seconds * 1000;
            }
            return backoff(retries + 1);
        }

        void beforeRetry(int retryCount, int status) {
            if (onRetry != null) {
                onRetry.onRetry(retryCount, status);
            }
        }
    }

    private long parseRetryAfter(String header) {
        if (header != null) {
            try {
                long v = Long.parseLong(header.trim());
                if (v >= 0) {
                    return v;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the default
            }
        }
        return Math.max(0, (defaultRetryAfter.toMillis() + 999) / 1000);
    }

    private static long backoff(int attempt) {
        long ceiling = Math.min(BACKOFF_CAP_MS, BACKOFF_BASE_MS << Math.min(attempt - 1, 20));
        return ThreadLocalRandom.current().nextLong(ceiling + 1);
    }

    /**
     * Sends a request with retries and returns the raw 2xx response.
     *
     * @param method HTTP method
     * @param path path relative to the base URL
     * @param query query parameters, may be null
     * @param body JSON body, or null for none
     * @param options per-request options, may be null
     * @return the response
     * @throws TrutoException for a non-2xx response after retries
     * @throws TrutoTimeoutException when the request times out
     * @throws TrutoTransportException when no response could be obtained
     */
    public RawResponse send(String method, String path, Map<String, ?> query, Object body, RequestOptions options) {
        Call call = new Call(method, path, query, body, options);
        int retries = 0;
        while (true) {
            HttpResponse<byte[]> resp;
            try {
                resp = http.send(call.request, HttpResponse.BodyHandlers.ofByteArray());
            } catch (HttpTimeoutException e) {
                throw timeoutError(call, e);
            } catch (IOException e) {
                if (!call.canRetry(retries)) {
                    throw transportError(call, e);
                }
                sleep(backoff(retries + 1));
                retries++;
                call.beforeRetry(retries, 0);
                continue;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new TrutoTransportException("interrupted while sending request", e);
            }
            RawResponse raw = toRaw(resp);
            if (raw.statusCode() >= 200 && raw.statusCode() < 300) {
                return raw;
            }
            long delay = call.retryDelay(raw, retries);
            if (delay < 0) {
                throw errorFor(raw);
            }
            sleep(delay);
            retries++;
            call.beforeRetry(retries, raw.statusCode());
        }
    }

    /**
     * Async form of {@link #send}. Cancelling the returned future stops further retries.
     *
     * @param method HTTP method
     * @param path path relative to the base URL
     * @param query query parameters, may be null
     * @param body JSON body, or null for none
     * @param options per-request options, may be null
     * @return a future of the response
     */
    public CompletableFuture<RawResponse> sendAsync(String method, String path, Map<String, ?> query,
            Object body, RequestOptions options) {
        CompletableFuture<RawResponse> result = new CompletableFuture<>();
        Call call;
        try {
            call = new Call(method, path, query, body, options);
        } catch (RuntimeException e) {
            result.completeExceptionally(e);
            return result;
        }
        attempt(call, 0, result);
        return result;
    }

    private void attempt(Call call, int retries, CompletableFuture<RawResponse> result) {
        if (result.isDone()) {
            return;
        }
        CompletableFuture<HttpResponse<byte[]>> f =
                http.sendAsync(call.request, HttpResponse.BodyHandlers.ofByteArray());
        result.whenComplete((r, e) -> {
            if (result.isCancelled()) {
                f.cancel(true);
            }
        });
        f.whenComplete((resp, err) -> {
            try {
                if (err != null) {
                    Throwable c = unwrap(err);
                    if (c instanceof HttpTimeoutException) {
                        result.completeExceptionally(timeoutError(call, c));
                    } else if (c instanceof IOException && call.canRetry(retries)) {
                        later(backoff(retries + 1), result, () -> {
                            call.beforeRetry(retries + 1, 0);
                            attempt(call, retries + 1, result);
                        });
                    } else if (c instanceof IOException) {
                        result.completeExceptionally(transportError(call, c));
                    } else {
                        result.completeExceptionally(c);
                    }
                    return;
                }
                RawResponse raw = toRaw(resp);
                if (raw.statusCode() >= 200 && raw.statusCode() < 300) {
                    result.complete(raw);
                    return;
                }
                long delay = call.retryDelay(raw, retries);
                if (delay < 0) {
                    result.completeExceptionally(errorFor(raw));
                    return;
                }
                later(delay, result, () -> {
                    call.beforeRetry(retries + 1, raw.statusCode());
                    attempt(call, retries + 1, result);
                });
            } catch (Throwable t) {
                result.completeExceptionally(t);
            }
        });
    }

    private static void later(long delayMs, CompletableFuture<RawResponse> result, Runnable task) {
        CompletableFuture.runAsync(() -> {
            if (result.isDone()) {
                return;
            }
            try {
                task.run();
            } catch (Throwable t) {
                result.completeExceptionally(t);
            }
        }, CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS));
    }

    private static Throwable unwrap(Throwable t) {
        Throwable c = t;
        while ((c instanceof CompletionException || c instanceof ExecutionException) && c.getCause() != null) {
            c = c.getCause();
        }
        return c;
    }

    private static void sleep(long ms) {
        if (ms <= 0) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TrutoTransportException("interrupted while waiting to retry", e);
        }
    }

    private static RawResponse toRaw(HttpResponse<byte[]> resp) {
        return new RawResponse(resp.statusCode(), resp.headers().map(), resp.body());
    }

    private static TrutoTimeoutException timeoutError(Call call, Throwable cause) {
        return new TrutoTimeoutException("request timed out: " + call.request.method() + " "
                + call.request.uri().getPath(), cause);
    }

    private static TrutoTransportException transportError(Call call, Throwable cause) {
        return new TrutoTransportException("request failed: " + call.request.method() + " "
                + call.request.uri().getPath() + ": " + cause, cause);
    }

    static TrutoException errorFor(RawResponse r) {
        Object body = null;
        if (r.bodyUnsafe().length > 0) {
            if (r.isJson()) {
                try {
                    body = Json.readTree(r.bodyUnsafe());
                } catch (TrutoDecodeException e) {
                    body = r.bodyAsString();
                }
            } else {
                body = r.bodyAsString();
            }
        }
        String reason = HttpStatus.reason(r.statusCode());
        StringBuilder msg = new StringBuilder("Truto API error: ").append(r.statusCode());
        if (!reason.isEmpty()) {
            msg.append(' ').append(reason);
        }
        String detail = null;
        if (body instanceof JsonNode) {
            JsonNode n = (JsonNode) body;
            for (String k : new String[] {"message", "error", "detail"}) {
                if (n.hasNonNull(k) && n.get(k).isTextual()) {
                    detail = n.get(k).asText();
                    break;
                }
            }
        } else if (body instanceof String && ((String) body).length() <= 200) {
            detail = (String) body;
        }
        if (detail != null && !detail.isEmpty()) {
            msg.append(": ").append(detail);
        }
        return new TrutoException(msg.toString(), r.statusCode(), reason, body, r.headers(), null);
    }
}
