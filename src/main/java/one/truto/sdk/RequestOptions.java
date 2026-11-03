package one.truto.sdk;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Per-request overrides. Anything left unset falls back to the client's settings.
 *
 * <pre>{@code
 * RequestOptions opts = RequestOptions.builder()
 *     .header("X-Trace", "abc")
 *     .timeout(Duration.ofSeconds(5))
 *     .maxRetries(1)
 *     .build();
 * }</pre>
 */
public final class RequestOptions {
    private static final RequestOptions NONE = builder().build();

    private final Map<String, String> headers;
    private final Duration timeout;
    private final Integer maxRetries;
    private final boolean noRetry;
    private final Set<Integer> retryStatusCodes;
    private final RetryListener onRetry;
    private final RateLimitListener onRateLimited;

    private RequestOptions(Builder b) {
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(b.headers));
        this.timeout = b.timeout;
        this.maxRetries = b.maxRetries;
        this.noRetry = b.noRetry;
        this.retryStatusCodes = Collections.unmodifiableSet(new LinkedHashSet<>(b.retryStatusCodes));
        this.onRetry = b.onRetry;
        this.onRateLimited = b.onRateLimited;
    }

    /**
     * Options with nothing overridden.
     *
     * @return the empty options
     */
    public static RequestOptions none() {
        return NONE;
    }

    /**
     * Starts a builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Extra headers; they override the client's default headers of the same name.
     *
     * @return the headers
     */
    public Map<String, String> headers() {
        return headers;
    }

    /**
     * Request timeout, or null for the client default.
     *
     * @return the timeout
     */
    public Duration timeout() {
        return timeout;
    }

    /**
     * Maximum retries, or null for the client default.
     *
     * @return the maximum number of retries
     */
    public Integer maxRetries() {
        return maxRetries;
    }

    /**
     * Whether retries are disabled for this request.
     *
     * @return true when retries are off
     */
    public boolean noRetry() {
        return noRetry;
    }

    /**
     * Status codes retried in addition to the defaults (408, 409, 425, 429, 502, 503, 504).
     *
     * @return the extra status codes
     */
    public Set<Integer> retryStatusCodes() {
        return retryStatusCodes;
    }

    /**
     * Retry hook, or null for the client's hook.
     *
     * @return the hook
     */
    public RetryListener onRetry() {
        return onRetry;
    }

    /**
     * Rate-limit hook, or null for the client's hook.
     *
     * @return the hook
     */
    public RateLimitListener onRateLimited() {
        return onRateLimited;
    }

    /** Builder for {@link RequestOptions}. */
    public static final class Builder {
        private final Map<String, String> headers = new LinkedHashMap<>();
        private Duration timeout;
        private Integer maxRetries;
        private boolean noRetry;
        private final Set<Integer> retryStatusCodes = new LinkedHashSet<>();
        private RetryListener onRetry;
        private RateLimitListener onRateLimited;

        private Builder() {
        }

        /**
         * Adds a header.
         *
         * @param name the header name
         * @param value the header value
         * @return this builder
         */
        public Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        /**
         * Adds several headers.
         *
         * @param values the headers
         * @return this builder
         */
        public Builder headers(Map<String, String> values) {
            headers.putAll(values);
            return this;
        }

        /**
         * Sets the request timeout.
         *
         * @param value the timeout
         * @return this builder
         */
        public Builder timeout(Duration value) {
            this.timeout = value;
            return this;
        }

        /**
         * Sets the maximum number of retries.
         *
         * @param value the maximum (0 disables retries)
         * @return this builder
         */
        public Builder maxRetries(int value) {
            if (value < 0) {
                throw new IllegalArgumentException("maxRetries must be >= 0");
            }
            this.maxRetries = value;
            return this;
        }

        /**
         * Disables all retries for this request.
         *
         * @return this builder
         */
        public Builder noRetry() {
            this.noRetry = true;
            return this;
        }

        /**
         * Sets whether retries are disabled.
         *
         * @param value true to disable retries
         * @return this builder
         */
        public Builder noRetry(boolean value) {
            this.noRetry = value;
            return this;
        }

        /**
         * Adds status codes to retry on.
         *
         * @param codes the extra status codes
         * @return this builder
         */
        public Builder retryStatusCodes(int... codes) {
            for (int c : codes) {
                retryStatusCodes.add(c);
            }
            return this;
        }

        /**
         * Sets the retry hook.
         *
         * @param value the hook
         * @return this builder
         */
        public Builder onRetry(RetryListener value) {
            this.onRetry = value;
            return this;
        }

        /**
         * Sets the rate-limit hook.
         *
         * @param value the hook
         * @return this builder
         */
        public Builder onRateLimited(RateLimitListener value) {
            this.onRateLimited = value;
            return this;
        }

        /**
         * Builds the options.
         *
         * @return the immutable options
         */
        public RequestOptions build() {
            return new RequestOptions(this);
        }
    }
}
