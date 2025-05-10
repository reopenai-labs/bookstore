package com.reopenai.bookstore.component.lambda;

import com.reopenai.bookstore.component.reflect.XReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lambda utils
 *
 * @author Allen Huang
 */
@Slf4j
public final class XLambdaUtil {

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
        return parseMethodName(serializable);
    }

    /**
     * Gets the method name of this lambda expression.
     *
     * @param serializable lambda instance
     * @return method name
     */
    public static <T> String getMethodName(XFunction<T, ?> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return lambda.getImplMethodName();
    }


    private static <T> Method parseMethod(XFunction<T, ?> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    private static Method parseMethod(SerializedLambda lambda) {
        try {
            String className = lambda.getImplClass().replace('/', '.');
            String methodName = lambda.getImplMethodName();
            ClassLoader classLoader = XReflectUtil.getClassLoader();
            Class<?> implClass = ClassUtils.forName(className, classLoader);
            String methodSignature = lambda.getImplMethodSignature();
            MethodType methodType = MethodType.fromMethodDescriptorString(methodSignature, implClass.getClassLoader());
            Class<?>[] parameterTypes = methodType.parameterArray();
            return implClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String parseMethodName(Serializable serializable) {
        SerializedLambda lambda = resolve(serializable);
        return XReflectUtil.methodToProperty(lambda.getImplMethodName());
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
