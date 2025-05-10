package com.reopenai.bookstore.component.reflect;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * Reflect util
 *
 * @author Allen Huang
 */
@Slf4j
public final class XReflectUtil {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String IS = "is";

    /**
     * Converts getter/setter names to property names
     *
     * @param name getter/setter method name
     * @return property name
     */
    public static String methodToProperty(String name) {
        if (name.startsWith(IS)) {
            name = name.substring(2);
        } else if (name.startsWith(GET) || name.startsWith(SET)) {
            name = name.substring(3);
        } else {
            log.warn("{} is not a getter/setter method.", name);
            return null;
        }
        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = XReflectUtil.class.getClassLoader();
            if (null == classLoader) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }

}
