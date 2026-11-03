package one.truto.sdk.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.LinkedHashMap;
import java.util.Map;
import one.truto.sdk.Json;

/**
 * A dynamic JSON object, used for unified, proxy and custom API payloads and for resources the
 * OpenAPI spec does not describe. Values are plain Java JSON values (String, Number, Boolean,
 * null, {@code Map<String, Object>}, {@code List<Object>}).
 *
 * <p>Hand-written (not generated). Called {@code Record} in the contract; renamed here so it does
 * not clash with {@code java.lang.Record} on Java 16+.
 */
public class TrutoRecord extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /** Creates an empty record. */
    public TrutoRecord() {
        super();
    }

    /**
     * Creates a record holding a copy of {@code values}.
     *
     * @param values the entries
     */
    public TrutoRecord(Map<String, ?> values) {
        super(values);
    }

    /**
     * Creates a record holding a copy of {@code values}.
     *
     * @param values the entries
     * @return the record
     */
    public static TrutoRecord of(Map<String, ?> values) {
        return new TrutoRecord(values);
    }

    /**
     * Sets a key and returns this record, for building request bodies fluently.
     *
     * @param key the key
     * @param value the value
     * @return this
     */
    public TrutoRecord with(String key, Object value) {
        put(key, value);
        return this;
    }

    /**
     * The value at {@code key} as a String ({@code null} when absent or null; non-strings via
     * {@code toString()}).
     *
     * @param key the key
     * @return the value
     */
    public String getString(String key) {
        Object v = get(key);
        return v == null ? null : v.toString();
    }

    /**
     * Decodes this record into your own type.
     *
     * @param type the target class
     * @param <T> the target type
     * @return the decoded value
     * @throws one.truto.sdk.TrutoDecodeException if the record does not fit
     */
    public <T> T as(Class<T> type) {
        return Json.convert(this, type);
    }

    /**
     * Decodes this record into a generic type.
     *
     * @param type the target type
     * @param <T> the target type
     * @return the decoded value
     * @throws one.truto.sdk.TrutoDecodeException if the record does not fit
     */
    public <T> T as(TypeReference<T> type) {
        return Json.convert(this, type);
    }

    /**
     * The record as a Jackson tree.
     *
     * @return the tree
     */
    public JsonNode toJsonNode() {
        return Json.toTree(this);
    }

    /**
     * The record as JSON text.
     *
     * @return the JSON
     */
    public String toJson() {
        return Json.toJsonString(this);
    }
}
