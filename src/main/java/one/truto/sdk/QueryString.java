package one.truto.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Encodes query parameters exactly like {@code qs.stringify(obj, { sort })} in the Truto
 * TypeScript SDK: keys sorted at every level, nested objects as {@code a[b][c]=v}, arrays with
 * indices {@code a[0]=x}, empty arrays and objects dropped, null as {@code key=}, and everything
 * outside the RFC 3986 unreserved set percent-encoded.
 *
 * <p>Sorting uses JavaScript string order (UTF-16 code units), which is also applied to array
 * indices, so an array of eleven or more elements is emitted as {@code [0],[1],[10],[2]...}; the
 * server rebuilds the array by index, so this does not change its meaning.
 */
public final class QueryString {
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private QueryString() {
    }

    /**
     * Encodes a query map. Values may be Strings, Numbers, Booleans, null, Maps, Collections,
     * arrays, Jackson {@link JsonNode}s, or any object Jackson can turn into one of those.
     *
     * @param query the parameters, may be null
     * @return the encoded string without a leading {@code ?} (empty when there is nothing to send)
     */
    public static String encode(Map<String, ?> query) {
        if (query == null || query.isEmpty()) {
            return "";
        }
        List<String> parts = new ArrayList<>();
        walk(query, null, parts);
        return String.join("&", parts);
    }

    private static void walk(Object value, String prefix, List<String> out) {
        Object v = normalize(value);
        if (v instanceof Map) {
            // String.compareTo orders by UTF-16 code unit, the same as JavaScript's < on strings.
            TreeMap<String, Object> sorted = new TreeMap<>();
            for (Map.Entry<?, ?> e : ((Map<?, ?>) v).entrySet()) {
                sorted.put(String.valueOf(e.getKey()), e.getValue());
            }
            for (Map.Entry<String, Object> e : sorted.entrySet()) {
                String k = e.getKey();
                walk(e.getValue(), prefix == null ? k : prefix + "[" + k + "]", out);
            }
        } else if (v instanceof List) {
            List<?> list = (List<?>) v;
            List<String> idx = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                idx.add(Integer.toString(i));
            }
            idx.sort(null);
            for (String i : idx) {
                walk(list.get(Integer.parseInt(i)), prefix + "[" + i + "]", out);
            }
        } else {
            out.add(encodeComponent(prefix) + "=" + (v == null ? "" : encodeComponent(scalar(v))));
        }
    }

    private static Object normalize(Object v) {
        if (v == null || v instanceof String || v instanceof Number || v instanceof Boolean
                || v instanceof Character) {
            return v;
        }
        if (v instanceof Map) {
            return v;
        }
        if (v instanceof Collection) {
            return new ArrayList<Object>((Collection<?>) v);
        }
        if (v.getClass().isArray()) {
            int n = Array.getLength(v);
            List<Object> list = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                list.add(Array.get(v, i));
            }
            return list;
        }
        if (v instanceof JsonNode && ((JsonNode) v).isNull()) {
            return null;
        }
        // JsonNode, models, open enums, dates, ...: let Jackson flatten it to plain Java values.
        Object converted = Json.convert(v, Object.class);
        if (converted != null && !(converted instanceof Map) && !(converted instanceof List)
                && !(converted instanceof String) && !(converted instanceof Number)
                && !(converted instanceof Boolean)) {
            return String.valueOf(converted);
        }
        return converted;
    }

    private static String scalar(Object v) {
        if (v instanceof Number) {
            return formatNumber((Number) v);
        }
        return String.valueOf(v);
    }

    /**
     * Formats a number the way JavaScript's {@code String(number)} does ({@code 50}, {@code 1.5},
     * {@code 1e+21}, {@code 1e-7}).
     *
     * @param n the number
     * @return the text
     */
    static String formatNumber(Number n) {
        if (n instanceof Integer || n instanceof Long || n instanceof Short || n instanceof Byte
                || n instanceof BigInteger) {
            return n.toString();
        }
        BigDecimal bd;
        if (n instanceof BigDecimal) {
            bd = (BigDecimal) n;
        } else {
            double d = n instanceof Float ? Double.parseDouble(Float.toString(n.floatValue())) : n.doubleValue();
            if (Double.isNaN(d)) {
                return "NaN";
            }
            if (Double.isInfinite(d)) {
                return d > 0 ? "Infinity" : "-Infinity";
            }
            if (d == 0) {
                return "0";
            }
            bd = new BigDecimal(Double.toString(d));
        }
        if (bd.signum() == 0) {
            return "0";
        }
        String sign = bd.signum() < 0 ? "-" : "";
        bd = bd.abs().stripTrailingZeros();
        String digits = bd.unscaledValue().toString();
        int k = digits.length();
        int e = k - bd.scale(); // value = 0.digits * 10^e
        StringBuilder sb = new StringBuilder(sign);
        if (k <= e && e <= 21) {
            sb.append(digits);
            for (int i = 0; i < e - k; i++) {
                sb.append('0');
            }
        } else if (0 < e && e <= 21) {
            sb.append(digits, 0, e).append('.').append(digits, e, k);
        } else if (-6 < e && e <= 0) {
            sb.append("0.");
            for (int i = 0; i < -e; i++) {
                sb.append('0');
            }
            sb.append(digits);
        } else {
            int exp = e - 1;
            sb.append(digits.charAt(0));
            if (k > 1) {
                sb.append('.').append(digits, 1, k);
            }
            sb.append('e').append(exp < 0 ? '-' : '+').append(Math.abs(exp));
        }
        return sb.toString();
    }

    /**
     * Percent-encodes a string, keeping only {@code A-Z a-z 0-9 - _ . ~}.
     *
     * @param s the string
     * @return the encoded string
     */
    public static String encodeComponent(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder(bytes.length);
        for (byte b : bytes) {
            int c = b & 0xff;
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '_' || c == '.' || c == '~') {
                sb.append((char) c);
            } else {
                sb.append('%').append(HEX[c >> 4]).append(HEX[c & 0xf]);
            }
        }
        return sb.toString();
    }
}
