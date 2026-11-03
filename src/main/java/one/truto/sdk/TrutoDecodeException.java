package one.truto.sdk;

/** A 2xx response (or a value) could not be decoded into the requested type. */
public class TrutoDecodeException extends TrutoException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a decode error.
     *
     * @param message the message
     * @param cause the underlying exception, or null
     */
    public TrutoDecodeException(String message, Throwable cause) {
        super(message, 0, "", null, null, cause);
    }

    /**
     * Creates a decode error for a response.
     *
     * @param message the message
     * @param response the response that could not be decoded
     * @param cause the underlying exception, or null
     */
    public TrutoDecodeException(String message, RawResponse response, Throwable cause) {
        super(message, response.statusCode(), HttpStatus.reason(response.statusCode()),
                response.bodyAsString(), response.headers(), cause);
    }
}
