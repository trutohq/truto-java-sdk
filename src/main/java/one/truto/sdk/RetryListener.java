package one.truto.sdk;

/** Called right before each retry. */
@FunctionalInterface
public interface RetryListener {
    /**
     * Called before a retry is sent.
     *
     * @param retryCount 1 for the first retry, 2 for the second, ...
     * @param statusCode status of the response that triggered the retry, or 0 for a connection error
     */
    void onRetry(int retryCount, int statusCode);
}
