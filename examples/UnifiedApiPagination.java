import java.util.List;
import java.util.stream.Collectors;
import one.truto.sdk.Page;
import one.truto.sdk.Pager;
import one.truto.sdk.Query;
import one.truto.sdk.TrutoClient;
import one.truto.sdk.models.TrutoRecord;

/** Pages through CRM contacts with the unified API. Usage: UnifiedApiPagination <integrated-account-id> */
public class UnifiedApiPagination {
    /** Your own type for typed decoding. */
    public static class Contact {
        public String id;
        public String first_name;
        public String last_name;
    }

    public static void main(String[] args) {
        TrutoClient truto = TrutoClient.builder().token(System.getenv("TRUTO_API_TOKEN")).build();
        String accountId = args[0];

        // Page by page.
        Pager<TrutoRecord> pager = truto.unifiedApi().list("crm", "contacts", accountId, Query.of("limit", 50));
        while (pager.hasNextPage()) {
            Page<TrutoRecord> page = pager.nextPage();
            System.out.println("page with " + page.items().size() + " contacts, next cursor " + page.nextCursor());
        }

        // Lazily, item by item, decoded into your own type.
        List<String> names = truto.unifiedApi()
                .list("crm", "contacts", accountId, Query.of("limit", 50), null, Contact.class)
                .stream()
                .limit(200)
                .map(c -> c.first_name + " " + c.last_name)
                .collect(Collectors.toList());
        System.out.println(names);
    }
}
