package com.reopenai.bookstore.component.lambda;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lambda utils
 *
 * @author Allen Huang
 */
@Slf4j
public final class XLambdaUtil {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String IS = "is";

    private static final Map<Class<?>, SerializedLambda> SERIALIZED_LAMBDA_CACHE = new ConcurrentHashMap<>();

    /**
     * Retrieves the property name from a lambda expression, where the method must be a getter or setter that complies with the Java Bean specification. Example:
     * <pre>{@code
     *   XLambdaUtil.property(Demo::getUsername)
     * }</pre>
     *
     * @param serializable lambda instance
     * @return property name
     */
    public static <T> String property(XFunction<T, ?> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return methodToProperty(lambda.getImplMethodName());
    }

    /**
     * Converts getter/setter names to property names
     *
     * @param name getter/setter method name
     * @return property name
     */
    private static String methodToProperty(String name) {
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

    private static SerializedLambda resolve(Serializable serializable) {
        return SERIALIZED_LAMBDA_CACHE.computeIfAbsent(serializable.getClass(), clazz -> {
            try {
                Method method = clazz.getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                return (SerializedLambda) method.invoke(serializable);
            } catch (Exception e) {
                log.error("Failed to resolve lambda.", e);
                throw new IllegalArgumentException("Failed to resolve lambda", e);
            }
        });
    }

}
