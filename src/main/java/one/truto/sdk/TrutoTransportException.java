package one.truto.sdk;

/** The request failed before a response arrived (connection refused, reset, DNS, ...). Status code is 0. */
public class TrutoTransportException extends TrutoException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a transport error.
     *
     * @param message the message
     * @param cause the underlying exception
     */
    public TrutoTransportException(String message, Throwable cause) {
        super(message, 0, "", null, null, cause);
    }
}
