package one.truto.sdk;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** An undecoded HTTP response: status, headers and body bytes. Returned by the raw variants. */
public final class RawResponse {
    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final byte[] body;

    /**
     * Creates a response.
     *
     * @param statusCode the status code
     * @param headers the headers
     * @param body the body bytes
     */
    public RawResponse(int statusCode, Map<String, List<String>> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers == null ? Collections.emptyMap() : headers;
        this.body = body == null ? new byte[0] : body;
    }

    /**
     * The HTTP status code.
     *
     * @return the status code
     */
    public int statusCode() {
        return statusCode;
    }

    /**
     * The response headers.
     *
     * @return the headers
     */
    public Map<String, List<String>> headers() {
        return headers;
    }

    /**
     * First value of a header, matched case-insensitively.
     *
     * @param name the header name
     * @return the value, or null
     */
    public String header(String name) {
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            if (e.getKey() != null && e.getKey().equalsIgnoreCase(name) && !e.getValue().isEmpty()) {
                return e.getValue().get(0);
            }
        }
        return null;
    }

    /**
     * The {@code Content-Type} header, or null.
     *
     * @return the content type
     */
    public String contentType() {
        return header("content-type");
    }

    /**
     * Whether the content type mentions JSON.
     *
     * @return true for a JSON body
     */
    public boolean isJson() {
        String ct = contentType();
        return ct != null && ct.toLowerCase(Locale.ROOT).contains("json");
    }

    /**
     * The body bytes (a copy).
     *
     * @return the bytes
     */
    public byte[] body() {
        return body.clone();
    }

    byte[] bodyUnsafe() {
        return body;
    }

    /**
     * The body decoded as UTF-8.
     *
     * @return the text
     */
    public String bodyAsString() {
        return new String(body, StandardCharsets.UTF_8);
    }
}
