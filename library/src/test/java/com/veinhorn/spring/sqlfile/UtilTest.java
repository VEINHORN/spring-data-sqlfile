package com.veinhorn.spring.sqlfile;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilTest {
    @Test
    public void testGetSimpleClassName() {
        assertEquals(Util.ClassUtil.getSimpleClassName("com.test.User"), "User");
    }

    @Test
    public void testGetPackageName() {
        assertEquals(Util.ClassUtil.getPackageName("com.test.User"), "com.test");
    }
}
