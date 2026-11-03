import java.time.Duration;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.TrutoClient;
import one.truto.sdk.TrutoException;
import one.truto.sdk.TrutoTimeoutException;
import one.truto.sdk.models.IntegratedAccountResponse;

/** Errors, retries and timeouts. */
public class ErrorHandling {
    public static void main(String[] args) {
        TrutoClient truto = TrutoClient.builder()
                .token(System.getenv("TRUTO_API_TOKEN"))
                .timeout(Duration.ofSeconds(20))
                .maxRetries(5)
                .onRateLimited((seconds, retries) -> System.err.println("rate limited, waiting " + seconds + "s"))
                .build();

        RequestOptions quick = RequestOptions.builder()
                .timeout(Duration.ofSeconds(5))
                .noRetry()
                .onRetry((n, status) -> System.err.println("retry " + n + " after " + status))
                .build();

        try {
            IntegratedAccountResponse a = truto.integratedAccounts().get("does-not-exist", null, quick);
            System.out.println(a);
        } catch (TrutoTimeoutException e) {
            System.err.println("timed out");
        } catch (TrutoException e) {
            if (e.isNotFound()) {
                System.err.println("not found: " + e.getJsonBody());
            } else {
                System.err.println(e.getStatusCode() + " " + e.getStatusText() + ": " + e.getBody());
            }
        }
    }
}
