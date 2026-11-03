package one.truto.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Raised for every failed Truto call: a non-2xx response, a timeout ({@link TrutoTimeoutException}),
 * a transport failure ({@link TrutoTransportException}) or a decode failure
 * ({@link TrutoDecodeException}).
 */
public class TrutoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String statusText;
    private final transient Object body;
    private final transient Map<String, List<String>> headers;

    /**
     * Creates an error for an HTTP response.
     *
     * @param message the message
     * @param statusCode the HTTP status code (0 when there was no response)
     * @param statusText the HTTP reason phrase
     * @param body the parsed body: a {@link JsonNode} for JSON, a String otherwise, or null
     * @param headers the response headers
     * @param cause the cause, or null
     */
    public TrutoException(String message, int statusCode, String statusText, Object body,
            Map<String, List<String>> headers, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.statusText = statusText == null ? "" : statusText;
        this.body = body;
        this.headers = headers == null ? Collections.emptyMap() : Collections.unmodifiableMap(headers);
    }

    /**
     * HTTP status code, or 0 when no response was received.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * HTTP reason phrase for the status code (HTTP/2 has none on the wire, so this is the standard
     * phrase).
     *
     * @return the status text
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * Parsed response body: a {@link JsonNode} when the response was JSON, a String otherwise,
     * or null when there was no body.
     *
     * @return the body
     */
    public Object getBody() {
        return body;
    }

    /**
     * The body as JSON, or null when the response was not JSON.
     *
     * @return the JSON body
     */
    public JsonNode getJsonBody() {
        return body instanceof JsonNode ? (JsonNode) body : null;
    }

    /**
     * Response headers (lower-case names).
     *
     * @return the headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Whether the status is 404.
     *
     * @return true for Not Found
     */
    public boolean isNotFound() {
        return statusCode == 404;
    }

    /**
     * Whether the status is 429.
     *
     * @return true for Too Many Requests
     */
    public boolean isRateLimited() {
        return statusCode == 429;
    }

    /**
     * Whether the status is 401.
     *
     * @return true for Unauthorized
     */
    public boolean isUnauthorized() {
        return statusCode == 401;
    }
}
