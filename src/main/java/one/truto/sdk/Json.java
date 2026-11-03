package one.truto.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

/**
 * JSON helpers shared by the client and the generated models.
 *
 * <p>Decoding is lenient: unknown properties are ignored and missing properties are left
 * unset.
 */
public final class Json {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    private Json() {
    }

    /**
     * Returns a copy of the ObjectMapper the SDK uses, for callers that want the same settings.
     *
     * @return a new, independently configurable mapper
     */
    public static ObjectMapper mapper() {
        return MAPPER.copy();
    }

    static ObjectMapper shared() {
        return MAPPER;
    }

    /**
     * Serializes a value to a JSON string.
     *
     * @param value any value Jackson can serialize
     * @return the JSON text
     * @throws TrutoDecodeException if the value cannot be serialized
     */
    public static String toJsonString(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new TrutoDecodeException("could not serialize " + value.getClass().getName(), e);
        }
    }

    /**
     * Serializes a value to UTF-8 JSON bytes.
     *
     * @param value any value Jackson can serialize
     * @return the JSON bytes
     * @throws TrutoDecodeException if the value cannot be serialized
     */
    public static byte[] toJsonBytes(Object value) {
        try {
            return MAPPER.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new TrutoDecodeException("could not serialize " + value.getClass().getName(), e);
        }
    }

    /**
     * Converts a value to a JSON tree.
     *
     * @param value any value Jackson can serialize
     * @return the tree
     */
    public static JsonNode toTree(Object value) {
        return MAPPER.valueToTree(value);
    }

    /**
     * Decodes a JSON tree into a type.
     *
     * @param node the tree
     * @param type the target class
     * @param <T> the target type
     * @return the decoded value
     * @throws TrutoDecodeException if the tree does not fit the type
     */
    public static <T> T fromTree(JsonNode node, Class<T> type) {
        return fromTree(node, MAPPER.constructType(type));
    }

    /**
     * Decodes a JSON tree into a generic type.
     *
     * @param node the tree
     * @param type the target type
     * @param <T> the target type
     * @return the decoded value
     * @throws TrutoDecodeException if the tree does not fit the type
     */
    public static <T> T fromTree(JsonNode node, TypeReference<T> type) {
        return fromTree(node, MAPPER.getTypeFactory().constructType(type));
    }

    static <T> T fromTree(JsonNode node, JavaType type) {
        try {
            return MAPPER.treeToValue(node, type);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            throw new TrutoDecodeException("could not decode JSON into " + type, e);
        }
    }

    /**
     * Converts any value (a Map, a model, a JsonNode) into another type via JSON.
     *
     * @param value the source value
     * @param type the target class
     * @param <T> the target type
     * @return the converted value
     * @throws TrutoDecodeException if the value does not fit the type
     */
    public static <T> T convert(Object value, Class<T> type) {
        try {
            return MAPPER.convertValue(value, type);
        } catch (IllegalArgumentException e) {
            throw new TrutoDecodeException("could not convert value into " + type.getName(), e);
        }
    }

    /**
     * Converts any value into a generic type via JSON.
     *
     * @param value the source value
     * @param type the target type
     * @param <T> the target type
     * @return the converted value
     * @throws TrutoDecodeException if the value does not fit the type
     */
    public static <T> T convert(Object value, TypeReference<T> type) {
        try {
            return MAPPER.convertValue(value, type);
        } catch (IllegalArgumentException e) {
            throw new TrutoDecodeException("could not convert value into " + type.getType(), e);
        }
    }

    static JsonNode readTree(byte[] bytes) {
        try {
            return MAPPER.readTree(bytes);
        } catch (IOException e) {
            throw new TrutoDecodeException("response body is not valid JSON", e);
        }
    }

    static <T> T read(byte[] bytes, JavaType type) {
        try {
            return MAPPER.readValue(bytes, type);
        } catch (IOException e) {
            throw new TrutoDecodeException("could not decode response into " + type, e);
        }
    }
}
