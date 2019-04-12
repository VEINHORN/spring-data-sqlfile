package com.veinhorn.spring.sqlfile;

import org.junit.Test;
import static org.junit.Assert.*;

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
