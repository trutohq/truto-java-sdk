package one.truto.sdk.resources;

import one.truto.sdk.ApiClient;
import one.truto.sdk.models.Documentation;
import one.truto.sdk.models.DocumentationCreateRequest;
import one.truto.sdk.models.DocumentationPatchRequest;

/** Documentation records. {@code delete} returns nothing. Path: {@code /documentation}. */
public class DocumentationResource extends CrudResource<Documentation, DocumentationCreateRequest, Documentation, DocumentationPatchRequest, Void> {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public DocumentationResource(ApiClient api) {
        super(api, "documentation", Documentation.class, Documentation.class, Void.class);
    }
}
