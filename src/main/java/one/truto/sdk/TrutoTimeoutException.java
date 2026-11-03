package one.truto.sdk;

/** The request did not complete within its timeout. Status code is 408. Never retried. */
public class TrutoTimeoutException extends TrutoException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a timeout error.
     *
     * @param message the message
     * @param cause the underlying exception
     */
    public TrutoTimeoutException(String message, Throwable cause) {
        super(message, 408, "Request Timeout", null, null, cause);
    }
}
