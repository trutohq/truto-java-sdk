import one.truto.sdk.Query;
import one.truto.sdk.TrutoClient;
import one.truto.sdk.models.IntegratedAccountResponse;

/** Lists every integrated account for a tenant. Run with TRUTO_API_TOKEN set. */
public class ListIntegratedAccounts {
    public static void main(String[] args) {
        TrutoClient truto = TrutoClient.builder().token(System.getenv("TRUTO_API_TOKEN")).build();

        for (IntegratedAccountResponse account : truto.integratedAccounts().list(Query.of("limit", 100))) {
            System.out.printf("%s  %s  %s%n", account.getId(), account.getTenantId(), account.getStatus());
        }
    }
}
