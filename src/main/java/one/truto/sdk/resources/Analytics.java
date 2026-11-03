package one.truto.sdk.resources;

import java.util.Map;
import one.truto.sdk.ApiClient;
import one.truto.sdk.RequestOptions;
import one.truto.sdk.models.AnalyticsBreakdownResponse;
import one.truto.sdk.models.AnalyticsDashboardResponse;
import one.truto.sdk.models.AnalyticsMetricsResponse;
import one.truto.sdk.models.AnalyticsStatusResponse;
import one.truto.sdk.models.AnalyticsTimeseriesResponse;

/** Usage analytics. Each method sends its query as-is to {@code GET /analytics/<name>}. Path: {@code /analytics}. */
public class Analytics extends BaseResource {
    /**
     * Creates the resource; use {@code TrutoClient} instead.
     *
     * @param api the HTTP layer
     */
    public Analytics(ApiClient api) {
        super(api, "analytics");
    }

    /**
     * Analytics status.
     *
     * @return the response
     */
    public AnalyticsStatusResponse status() {
        return status(null, null);
    }

    /**
     * Analytics status.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public AnalyticsStatusResponse status(Map<String, ?> query, RequestOptions options) {
        return call("GET", at("status"), query, null, options, AnalyticsStatusResponse.class);
    }

    /**
     * Available analytics metrics.
     *
     * @return the response
     */
    public AnalyticsMetricsResponse metrics() {
        return metrics(null, null);
    }

    /**
     * Available analytics metrics.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public AnalyticsMetricsResponse metrics(Map<String, ?> query, RequestOptions options) {
        return call("GET", at("metrics"), query, null, options, AnalyticsMetricsResponse.class);
    }

    /**
     * Dashboard summary.
     *
     * @return the response
     */
    public AnalyticsDashboardResponse dashboard() {
        return dashboard(null, null);
    }

    /**
     * Dashboard summary.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public AnalyticsDashboardResponse dashboard(Map<String, ?> query, RequestOptions options) {
        return call("GET", at("dashboard"), query, null, options, AnalyticsDashboardResponse.class);
    }

    /**
     * Time series for a metric.
     *
     * @return the response
     */
    public AnalyticsTimeseriesResponse timeseries() {
        return timeseries(null, null);
    }

    /**
     * Time series for a metric.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public AnalyticsTimeseriesResponse timeseries(Map<String, ?> query, RequestOptions options) {
        return call("GET", at("timeseries"), query, null, options, AnalyticsTimeseriesResponse.class);
    }

    /**
     * Breakdown of a metric by a dimension.
     *
     * @return the response
     */
    public AnalyticsBreakdownResponse breakdown() {
        return breakdown(null, null);
    }

    /**
     * Breakdown of a metric by a dimension.
     *
     * @param query the query, may be null
     * @param options per-request options, may be null
     * @return the response
     */
    public AnalyticsBreakdownResponse breakdown(Map<String, ?> query, RequestOptions options) {
        return call("GET", at("breakdown"), query, null, options, AnalyticsBreakdownResponse.class);
    }
}
