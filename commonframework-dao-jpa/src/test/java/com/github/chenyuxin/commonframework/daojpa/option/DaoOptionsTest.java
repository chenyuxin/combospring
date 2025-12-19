package com.github.chenyuxin.commonframework.daojpa.option;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class DaoOptionsTest {

    @Test
    public void testBuilder() {
        DaoOptions options = DaoOptions.builder()
                .dataSourceName("ds1")
                .eq("key", "value")
                .throwRuntimeException()
                .build();

        assertEquals("ds1", options.getDataSourceName());
        assertEquals("value", options.getParamMap().get("key"));
        assertTrue(options.isThrowException());
    }

    @Test
    public void testVarArgs() {
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "v1");

        DaoOptions options = new DaoOptions("ds2", map, DaoEnumOptions.RuntimeException);

        assertEquals("ds2", options.getDataSourceName());
        assertEquals("v1", options.getParamMap().get("k1"));
        assertTrue(options.isThrowException());
    }

    @Test
    public void testNestedDaoOptions() {
        DaoOptions inner = DaoOptions.builder()
                .dataSourceName("innerDS")
                .eq("innerK", "innerV")
                .build();

        DaoOptions outer = new DaoOptions("outerDS", inner);

        // Strategy: Last one wins or merge?
        // In the implementation:
        // 1. "outerDS" sets dataSourceName to "outerDS".
        // 2. inner sets dataSourceName to "innerDS" (if not default).
        // Let's verify the behavior. Current logic: loop processes arguments in order.
        // "outerDS" comes first, then inner. inner will overwrite if it has a
        // non-default DS.

        assertEquals("innerDS", outer.getDataSourceName());
        assertEquals("innerV", outer.getParamMap().get("innerK"));
    }

    @Test
    public void testRecursiveArray() {
        DaoOptions options = new DaoOptions(new Object[] { "ds3", DaoEnumOptions.RuntimeException });
        assertEquals("ds3", options.getDataSourceName());
        assertTrue(options.isThrowException());
    }
}
