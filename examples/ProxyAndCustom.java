import java.nio.file.Files;
import java.nio.file.Paths;
import one.truto.sdk.RawResponse;
import one.truto.sdk.TrutoClient;
import one.truto.sdk.models.TrutoRecord;

/** Proxy and custom API calls. Usage: ProxyAndCustom <integrated-account-id> */
public class ProxyAndCustom {
    public static void main(String[] args) throws Exception {
        TrutoClient truto = TrutoClient.builder().token(System.getenv("TRUTO_API_TOKEN")).build();
        String accountId = args[0];

        // Proxy API: the integration's own resource names and payloads.
        TrutoRecord first = truto.proxyApi().list("contacts", accountId).first().orElse(null);
        System.out.println(first);

        // Async variant.
        truto.proxyApi().getAsync("contacts", accountId, "123", null, null)
                .thenAccept(c -> System.out.println("async: " + c))
                .exceptionally(e -> {
                    System.err.println("async failed: " + e.getCause());
                    return null;
                })
                .join();

        // Custom API returning a file: use the raw variant.
        RawResponse pdf = truto.customApi().requestRaw("GET", "reports/latest.pdf", accountId, null, null, null);
        Files.write(Paths.get("latest.pdf"), pdf.body());
    }
}
