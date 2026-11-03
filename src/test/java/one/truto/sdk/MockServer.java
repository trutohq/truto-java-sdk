package one.truto.sdk;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** A tiny scripted HTTP server for tests, built on the JDK's HttpServer. */
final class MockServer implements AutoCloseable {
    static final class Reply {
        final int status;
        final Map<String, String> headers = new LinkedHashMap<>();
        final byte[] body;
        long delayMs;

        Reply(int status, String contentType, String body) {
            this.status = status;
            this.body = body == null ? new byte[0] : body.getBytes(StandardCharsets.UTF_8);
            if (contentType != null) {
                headers.put("Content-Type", contentType);
            }
        }

        Reply header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        Reply delay(long ms) {
            this.delayMs = ms;
            return this;
        }
    }

    static final class Recorded {
        final String method;
        final String rawPath;
        final String rawQuery;
        final Map<String, List<String>> headers;
        final String body;

        Recorded(String method, String rawPath, String rawQuery, Map<String, List<String>> headers, String body) {
            this.method = method;
            this.rawPath = rawPath;
            this.rawQuery = rawQuery;
            this.headers = headers;
            this.body = body;
        }

        String header(String name) {
            for (Map.Entry<String, List<String>> e : headers.entrySet()) {
                if (e.getKey().equalsIgnoreCase(name)) {
                    return e.getValue().get(0);
                }
            }
            return null;
        }
    }

    private final HttpServer server;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final ConcurrentLinkedDeque<Reply> replies = new ConcurrentLinkedDeque<>();
    private final List<Recorded> requests = Collections.synchronizedList(new ArrayList<>());

    MockServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/", this::handle);
        server.setExecutor(executor);
        server.start();
    }

    String url() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    MockServer enqueue(Reply r) {
        replies.add(r);
        return this;
    }

    MockServer json(int status, String body) {
        return enqueue(new Reply(status, "application/json; charset=utf-8", body));
    }

    List<Recorded> requests() {
        synchronized (requests) {
            return new ArrayList<>(requests);
        }
    }

    Recorded last() {
        List<Recorded> all = requests();
        return all.get(all.size() - 1);
    }

    private void handle(HttpExchange ex) throws IOException {
        byte[] reqBody;
        try (InputStream in = ex.getRequestBody()) {
            reqBody = in.readAllBytes();
        }
        Map<String, List<String>> headers = new LinkedHashMap<>();
        ex.getRequestHeaders().forEach((k, v) -> headers.put(k, new ArrayList<>(v)));
        requests.add(new Recorded(ex.getRequestMethod(), ex.getRequestURI().getRawPath(),
                ex.getRequestURI().getRawQuery(), headers, new String(reqBody, StandardCharsets.UTF_8)));
        Reply r = replies.poll();
        if (r == null) {
            r = new Reply(500, "text/plain", "no reply queued");
        }
        if (r.delayMs > 0) {
            try {
                Thread.sleep(r.delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        r.headers.forEach((k, v) -> ex.getResponseHeaders().add(k, v));
        if (r.status == 204 || r.body.length == 0) {
            ex.sendResponseHeaders(r.status, -1);
        } else {
            ex.sendResponseHeaders(r.status, r.body.length);
            try (OutputStream out = ex.getResponseBody()) {
                out.write(r.body);
            } catch (IOException ignored) {
                // client went away (timeout tests)
            }
        }
        ex.close();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdownNow();
    }
}
