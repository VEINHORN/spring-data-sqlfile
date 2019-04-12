package com.veinhorn.spring.sqlfile;

public class Util {
    public static class ClassUtil {
        public static String getSimpleClassName(String fullName) {
            return fullName.substring(fullName.lastIndexOf(".") + 1);
        }

        public static String getPackageName(String fullClassName) {
            return fullClassName.substring(0, fullClassName.lastIndexOf("."));
        }
    }
}
