package one.truto.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class QueryStringTest {
    @Test
    void matchesEveryFixtureCase() throws Exception {
        List<Map<String, Object>> cases;
        try (InputStream in = getClass().getResourceAsStream("/qs_cases.json")) {
            cases = new ObjectMapper().readValue(in, new TypeReference<List<Map<String, Object>>>() { });
        }
        assertTrue(cases.size() >= 19, "fixture should have all cases");
        for (Map<String, Object> c : cases) {
            @SuppressWarnings("unchecked")
            Map<String, Object> input = (Map<String, Object>) c.get("input");
            assertEquals(c.get("expected"), QueryString.encode(input), String.valueOf(c.get("name")));
        }
    }

    @Test
    void formatsNumbersLikeJavaScript() {
        assertEquals("50", QueryString.formatNumber(50.0));
        assertEquals("1.5", QueryString.formatNumber(1.5));
        assertEquals("-0.5", QueryString.formatNumber(-0.5));
        assertEquals("1e+21", QueryString.formatNumber(1e21));
        assertEquals("1e-7", QueryString.formatNumber(1e-7));
        assertEquals("0.000001", QueryString.formatNumber(1e-6));
        assertEquals("123456789012", QueryString.formatNumber(123456789012L));
        assertEquals("2.5", QueryString.formatNumber(new BigDecimal("2.50")));
    }

    @Test
    void sortsArrayIndicesAsStringsLikeQs() {
        Map<String, Object> q = new LinkedHashMap<>();
        q.put("a", Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"));
        assertEquals("a%5B0%5D=a&a%5B1%5D=b&a%5B10%5D=k&a%5B11%5D=l&a%5B2%5D=c&a%5B3%5D=d&a%5B4%5D=e"
                + "&a%5B5%5D=f&a%5B6%5D=g&a%5B7%5D=h&a%5B8%5D=i&a%5B9%5D=j", QueryString.encode(q));
    }

    @Test
    void acceptsArraysAndQueryBuilder() {
        Query q = Query.of("ids", new String[] {"x", "y"}).with("limit", 10);
        assertEquals("ids%5B0%5D=x&ids%5B1%5D=y&limit=10", q.encode());
        assertEquals("", QueryString.encode(null));
    }
}
