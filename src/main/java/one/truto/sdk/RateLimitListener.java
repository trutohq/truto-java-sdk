package one.truto.sdk;

/** Called when a 429 response is about to be waited out. */
@FunctionalInterface
public interface RateLimitListener {
    /**
     * Called before the client sleeps for {@code retryAfterSeconds}.
     *
     * @param retryAfterSeconds how long the client will wait
     * @param retryCount retries already made for this request (0 on the first 429)
     */
    void onRateLimited(long retryAfterSeconds, int retryCount);
}
