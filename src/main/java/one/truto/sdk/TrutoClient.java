package one.truto.sdk;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import one.truto.sdk.models.Ping;
import one.truto.sdk.resources.Alarms;
import one.truto.sdk.resources.Analytics;
import one.truto.sdk.resources.ApiTokens;
import one.truto.sdk.resources.BatchRequests;
import one.truto.sdk.resources.Categories;
import one.truto.sdk.resources.CustomApi;
import one.truto.sdk.resources.DaemonJobCronTriggers;
import one.truto.sdk.resources.DaemonJobRuns;
import one.truto.sdk.resources.DaemonJobs;
import one.truto.sdk.resources.Daemons;
import one.truto.sdk.resources.Datastores;
import one.truto.sdk.resources.DocumentationResource;
import one.truto.sdk.resources.EnvironmentIntegrationWebhooks;
import one.truto.sdk.resources.EnvironmentIntegrations;
import one.truto.sdk.resources.EnvironmentUnifiedModelResourceMethods;
import one.truto.sdk.resources.EnvironmentUnifiedModels;
import one.truto.sdk.resources.Environments;
import one.truto.sdk.resources.IntegratedAccountWebhooks;
import one.truto.sdk.resources.IntegratedAccounts;
import one.truto.sdk.resources.Integrations;
import one.truto.sdk.resources.LinkTokens;
import one.truto.sdk.resources.Logs;
import one.truto.sdk.resources.Mcp;
import one.truto.sdk.resources.ObservabilityDestinations;
import one.truto.sdk.resources.ProxyApi;
import one.truto.sdk.resources.SandboxIntegratedAccounts;
import one.truto.sdk.resources.SessionTokens;
import one.truto.sdk.resources.StaticGates;
import one.truto.sdk.resources.SyncJobCronTriggers;
import one.truto.sdk.resources.SyncJobRunStates;
import one.truto.sdk.resources.SyncJobRuns;
import one.truto.sdk.resources.SyncJobTemplates;
import one.truto.sdk.resources.SyncJobs;
import one.truto.sdk.resources.Teams;
import one.truto.sdk.resources.Tenants;
import one.truto.sdk.resources.UnifiedApi;
import one.truto.sdk.resources.UnifiedModelResourceMethods;
import one.truto.sdk.resources.UnifiedModels;
import one.truto.sdk.resources.Users;
import one.truto.sdk.resources.Webhooks;
import one.truto.sdk.resources.WorkflowRuns;
import one.truto.sdk.resources.Workflows;

/**
 * Client for the Truto REST API. Immutable and thread-safe; create one and share it.
 *
 * <pre>{@code
 * TrutoClient truto = TrutoClient.builder()
 *     .token(System.getenv("TRUTO_API_TOKEN"))
 *     .build();
 * for (IntegratedAccountResponse a : truto.integratedAccounts().list()) {
 *     System.out.println(a.getId());
 * }
 * }</pre>
 */
public final class TrutoClient {
    /** SDK version, sent in the {@code User-Agent} header. */
    public static final String VERSION = "0.1.0";
    /** The default base URL. */
    public static final String DEFAULT_BASE_URL = "https://api.truto.one";
    /** The default request timeout. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    /** The default number of retries. */
    public static final int DEFAULT_MAX_RETRIES = 3;
    /** The default wait for a 429 without a usable {@code Retry-After} header. */
    public static final Duration DEFAULT_RETRY_AFTER = Duration.ofSeconds(10);

    private final ApiClient api;
    private final ApiTokens apiTokens;
    private final Environments environments;
    private final Categories categories;
    private final Teams teams;
    private final Users users;
    private final Tenants tenants;
    private final LinkTokens linkTokens;
    private final SessionTokens sessionTokens;
    private final IntegratedAccounts integratedAccounts;
    private final SandboxIntegratedAccounts sandboxIntegratedAccounts;
    private final Integrations integrations;
    private final EnvironmentIntegrations environmentIntegrations;
    private final UnifiedModels unifiedModels;
    private final EnvironmentUnifiedModels environmentUnifiedModels;
    private final UnifiedModelResourceMethods unifiedModelResourceMethods;
    private final EnvironmentUnifiedModelResourceMethods environmentUnifiedModelResourceMethods;
    private final SyncJobs syncJobs;
    private final SyncJobRuns syncJobRuns;
    private final SyncJobCronTriggers syncJobCronTriggers;
    private final SyncJobTemplates syncJobTemplates;
    private final SyncJobRunStates syncJobRunStates;
    private final Daemons daemons;
    private final DaemonJobs daemonJobs;
    private final DaemonJobRuns daemonJobRuns;
    private final DaemonJobCronTriggers daemonJobCronTriggers;
    private final Alarms alarms;
    private final Workflows workflows;
    private final WorkflowRuns workflowRuns;
    private final Webhooks webhooks;
    private final Datastores datastores;
    private final DocumentationResource documentation;
    private final Logs logs;
    private final StaticGates staticGates;
    private final ObservabilityDestinations observabilityDestinations;
    private final Analytics analytics;
    private final BatchRequests batchRequests;
    private final EnvironmentIntegrationWebhooks environmentIntegrationWebhooks;
    private final IntegratedAccountWebhooks integratedAccountWebhooks;
    private final UnifiedApi unifiedApi;
    private final ProxyApi proxyApi;
    private final CustomApi customApi;
    private final Mcp mcp;

    private TrutoClient(Builder b) {
        String base = b.baseUrl == null ? DEFAULT_BASE_URL : b.baseUrl.trim();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        HttpClient http = b.httpClient != null ? b.httpClient
                : HttpClient.newBuilder()
                        .connectTimeout(b.timeout)
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .build();
        this.api = new ApiClient(base, b.token, http, b.timeout, b.maxRetries, b.defaultRetryAfter, b.onRetry,
                b.onRateLimited);
        this.apiTokens = new ApiTokens(api);
        this.environments = new Environments(api);
        this.categories = new Categories(api);
        this.teams = new Teams(api);
        this.users = new Users(api);
        this.tenants = new Tenants(api);
        this.linkTokens = new LinkTokens(api);
        this.sessionTokens = new SessionTokens(api);
        this.integratedAccounts = new IntegratedAccounts(api);
        this.sandboxIntegratedAccounts = new SandboxIntegratedAccounts(api);
        this.integrations = new Integrations(api);
        this.environmentIntegrations = new EnvironmentIntegrations(api);
        this.unifiedModels = new UnifiedModels(api);
        this.environmentUnifiedModels = new EnvironmentUnifiedModels(api);
        this.unifiedModelResourceMethods = new UnifiedModelResourceMethods(api);
        this.environmentUnifiedModelResourceMethods = new EnvironmentUnifiedModelResourceMethods(api);
        this.syncJobs = new SyncJobs(api);
        this.syncJobRuns = new SyncJobRuns(api);
        this.syncJobCronTriggers = new SyncJobCronTriggers(api);
        this.syncJobTemplates = new SyncJobTemplates(api);
        this.syncJobRunStates = new SyncJobRunStates(api);
        this.daemons = new Daemons(api);
        this.daemonJobs = new DaemonJobs(api);
        this.daemonJobRuns = new DaemonJobRuns(api);
        this.daemonJobCronTriggers = new DaemonJobCronTriggers(api);
        this.alarms = new Alarms(api);
        this.workflows = new Workflows(api);
        this.workflowRuns = new WorkflowRuns(api);
        this.webhooks = new Webhooks(api);
        this.datastores = new Datastores(api);
        this.documentation = new DocumentationResource(api);
        this.logs = new Logs(api);
        this.staticGates = new StaticGates(api);
        this.observabilityDestinations = new ObservabilityDestinations(api);
        this.analytics = new Analytics(api);
        this.batchRequests = new BatchRequests(api);
        this.environmentIntegrationWebhooks = new EnvironmentIntegrationWebhooks(api);
        this.integratedAccountWebhooks = new IntegratedAccountWebhooks(api);
        this.unifiedApi = new UnifiedApi(api);
        this.proxyApi = new ProxyApi(api);
        this.customApi = new CustomApi(api);
        this.mcp = new Mcp(api);
    }

    /**
     * Starts a builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The HTTP layer, for endpoints the SDK does not wrap.
     *
     * @return the HTTP layer
     */
    public ApiClient http() {
        return api;
    }

    /**
     * Health check ({@code GET /ping}).
     *
     * @return the ping response
     */
    public Ping ping() {
        return ping(null, null);
    }

    /**
     * Health check ({@code GET /ping}).
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the ping response
     */
    public Ping ping(Map<String, ?> query, RequestOptions options) {
        return api.request("GET", "ping", query, null, options, Ping.class);
    }

    /**
     * API tokens ({@code /api-token}).
     *
     * @return the resource
     */
    public ApiTokens apiTokens() {
        return apiTokens;
    }

    /**
     * Environments ({@code /environment}).
     *
     * @return the resource
     */
    public Environments environments() {
        return environments;
    }

    /**
     * Integration categories ({@code /category}).
     *
     * @return the resource
     */
    public Categories categories() {
        return categories;
    }

    /**
     * Teams ({@code /team}).
     *
     * @return the resource
     */
    public Teams teams() {
        return teams;
    }

    /**
     * Users ({@code /user}).
     *
     * @return the resource
     */
    public Users users() {
        return users;
    }

    /**
     * Tenants ({@code /tenant}).
     *
     * @return the resource
     */
    public Tenants tenants() {
        return tenants;
    }

    /**
     * Link tokens ({@code /link-token}).
     *
     * @return the resource
     */
    public LinkTokens linkTokens() {
        return linkTokens;
    }

    /**
     * Session tokens ({@code /session-token}).
     *
     * @return the resource
     */
    public SessionTokens sessionTokens() {
        return sessionTokens;
    }

    /**
     * Integrated accounts ({@code /integrated-account}).
     *
     * @return the resource
     */
    public IntegratedAccounts integratedAccounts() {
        return integratedAccounts;
    }

    /**
     * Sandbox integrated accounts ({@code /sandbox-integrated-account}).
     *
     * @return the resource
     */
    public SandboxIntegratedAccounts sandboxIntegratedAccounts() {
        return sandboxIntegratedAccounts;
    }

    /**
     * Integrations ({@code /integration}).
     *
     * @return the resource
     */
    public Integrations integrations() {
        return integrations;
    }

    /**
     * Installed integrations ({@code /environment-integration}).
     *
     * @return the resource
     */
    public EnvironmentIntegrations environmentIntegrations() {
        return environmentIntegrations;
    }

    /**
     * Unified models ({@code /unified-model}).
     *
     * @return the resource
     */
    public UnifiedModels unifiedModels() {
        return unifiedModels;
    }

    /**
     * Installed unified models ({@code /environment-unified-model}).
     *
     * @return the resource
     */
    public EnvironmentUnifiedModels environmentUnifiedModels() {
        return environmentUnifiedModels;
    }

    /**
     * Unified model resource methods ({@code /unified-model-resource-method}).
     *
     * @return the resource
     */
    public UnifiedModelResourceMethods unifiedModelResourceMethods() {
        return unifiedModelResourceMethods;
    }

    /**
     * Environment unified model resource methods ({@code /environment-unified-model-resource-method}).
     *
     * @return the resource
     */
    public EnvironmentUnifiedModelResourceMethods environmentUnifiedModelResourceMethods() {
        return environmentUnifiedModelResourceMethods;
    }

    /**
     * Sync jobs ({@code /sync-job}).
     *
     * @return the resource
     */
    public SyncJobs syncJobs() {
        return syncJobs;
    }

    /**
     * Sync job runs ({@code /sync-job-run}).
     *
     * @return the resource
     */
    public SyncJobRuns syncJobRuns() {
        return syncJobRuns;
    }

    /**
     * Sync job cron triggers ({@code /sync-job-cron-trigger}).
     *
     * @return the resource
     */
    public SyncJobCronTriggers syncJobCronTriggers() {
        return syncJobCronTriggers;
    }

    /**
     * Sync job templates ({@code /sync-job-template}).
     *
     * @return the resource
     */
    public SyncJobTemplates syncJobTemplates() {
        return syncJobTemplates;
    }

    /**
     * Sync job run states ({@code /sync-job-run-state}).
     *
     * @return the resource
     */
    public SyncJobRunStates syncJobRunStates() {
        return syncJobRunStates;
    }

    /**
     * Daemons ({@code /daemon}).
     *
     * @return the resource
     */
    public Daemons daemons() {
        return daemons;
    }

    /**
     * Daemon jobs ({@code /daemon-job}).
     *
     * @return the resource
     */
    public DaemonJobs daemonJobs() {
        return daemonJobs;
    }

    /**
     * Daemon job runs ({@code /daemon-job-run}).
     *
     * @return the resource
     */
    public DaemonJobRuns daemonJobRuns() {
        return daemonJobRuns;
    }

    /**
     * Daemon job cron triggers ({@code /daemon-job-cron-trigger}).
     *
     * @return the resource
     */
    public DaemonJobCronTriggers daemonJobCronTriggers() {
        return daemonJobCronTriggers;
    }

    /**
     * Alarms ({@code /alarm}).
     *
     * @return the resource
     */
    public Alarms alarms() {
        return alarms;
    }

    /**
     * Workflows ({@code /workflow}).
     *
     * @return the resource
     */
    public Workflows workflows() {
        return workflows;
    }

    /**
     * Workflow runs ({@code /workflow-run}).
     *
     * @return the resource
     */
    public WorkflowRuns workflowRuns() {
        return workflowRuns;
    }

    /**
     * Webhooks ({@code /webhook}).
     *
     * @return the resource
     */
    public Webhooks webhooks() {
        return webhooks;
    }

    /**
     * Datastores ({@code /datastore}).
     *
     * @return the resource
     */
    public Datastores datastores() {
        return datastores;
    }

    /**
     * Documentation ({@code /documentation}).
     *
     * @return the resource
     */
    public DocumentationResource documentation() {
        return documentation;
    }

    /**
     * Logs ({@code /log}); pass {@code log_type}.
     *
     * @return the resource
     */
    public Logs logs() {
        return logs;
    }

    /**
     * Static gates ({@code /static-gate}).
     *
     * @return the resource
     */
    public StaticGates staticGates() {
        return staticGates;
    }

    /**
     * Observability destinations ({@code /observability-destination}).
     *
     * @return the resource
     */
    public ObservabilityDestinations observabilityDestinations() {
        return observabilityDestinations;
    }

    /**
     * Analytics ({@code /analytics}).
     *
     * @return the resource
     */
    public Analytics analytics() {
        return analytics;
    }

    /**
     * Batch requests ({@code /batch-request}).
     *
     * @return the resource
     */
    public BatchRequests batchRequests() {
        return batchRequests;
    }

    /**
     * Environment integration webhooks ({@code /environment-integration-webhook}).
     *
     * @return the resource
     */
    public EnvironmentIntegrationWebhooks environmentIntegrationWebhooks() {
        return environmentIntegrationWebhooks;
    }

    /**
     * Integrated account webhooks ({@code /integrated-account-webhook}).
     *
     * @return the resource
     */
    public IntegratedAccountWebhooks integratedAccountWebhooks() {
        return integratedAccountWebhooks;
    }

    /**
     * Unified API ({@code /unified}).
     *
     * @return the resource
     */
    public UnifiedApi unifiedApi() {
        return unifiedApi;
    }

    /**
     * Proxy API ({@code /proxy}).
     *
     * @return the resource
     */
    public ProxyApi proxyApi() {
        return proxyApi;
    }

    /**
     * Custom API ({@code /custom}).
     *
     * @return the resource
     */
    public CustomApi customApi() {
        return customApi;
    }

    /**
     * MCP servers ({@code /mcp}).
     *
     * @return the resource
     */
    public Mcp mcp() {
        return mcp;
    }

    /** Builder for {@link TrutoClient}. */
    public static final class Builder {
        private String token;
        private String baseUrl;
        private Duration timeout = DEFAULT_TIMEOUT;
        private int maxRetries = DEFAULT_MAX_RETRIES;
        private Duration defaultRetryAfter = DEFAULT_RETRY_AFTER;
        private HttpClient httpClient;
        private RetryListener onRetry;
        private RateLimitListener onRateLimited;

        private Builder() {
        }

        /**
         * The API token (required).
         *
         * @param value the token
         * @return this builder
         */
        public Builder token(String value) {
            this.token = value;
            return this;
        }

        /**
         * The base URL (default {@code https://api.truto.one}; a trailing slash is removed).
         *
         * @param value the base URL
         * @return this builder
         */
        public Builder baseUrl(String value) {
            this.baseUrl = value;
            return this;
        }

        /**
         * The request timeout (default 30 seconds).
         *
         * @param value the timeout
         * @return this builder
         */
        public Builder timeout(Duration value) {
            Objects.requireNonNull(value, "timeout");
            if (value.isZero() || value.isNegative()) {
                throw new IllegalArgumentException("timeout must be positive");
            }
            this.timeout = value;
            return this;
        }

        /**
         * Maximum retries per request (default 3).
         *
         * @param value the maximum (0 disables retries)
         * @return this builder
         */
        public Builder maxRetries(int value) {
            if (value < 0) {
                throw new IllegalArgumentException("maxRetries must be >= 0");
            }
            this.maxRetries = value;
            return this;
        }

        /**
         * How long to wait after a 429 without a usable {@code Retry-After} header (default 10s).
         *
         * @param value the wait
         * @return this builder
         */
        public Builder defaultRetryAfter(Duration value) {
            Objects.requireNonNull(value, "defaultRetryAfter");
            if (value.isNegative()) {
                throw new IllegalArgumentException("defaultRetryAfter must not be negative");
            }
            this.defaultRetryAfter = value;
            return this;
        }

        /**
         * A custom {@link HttpClient} (proxies, SSL, executors). The client timeout still applies
         * per request.
         *
         * @param value the HTTP client
         * @return this builder
         */
        public Builder httpClient(HttpClient value) {
            this.httpClient = value;
            return this;
        }

        /**
         * Hook called before every retry, unless a request sets its own.
         *
         * @param value the hook
         * @return this builder
         */
        public Builder onRetry(RetryListener value) {
            this.onRetry = value;
            return this;
        }

        /**
         * Hook called before waiting out a 429, unless a request sets its own.
         *
         * @param value the hook
         * @return this builder
         */
        public Builder onRateLimited(RateLimitListener value) {
            this.onRateLimited = value;
            return this;
        }

        /**
         * Builds the client.
         *
         * @return the client
         * @throws IllegalArgumentException if no token was set
         */
        public TrutoClient build() {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("token is required");
            }
            return new TrutoClient(this);
        }
    }
}
