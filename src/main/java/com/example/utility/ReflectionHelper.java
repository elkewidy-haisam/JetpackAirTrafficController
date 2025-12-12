/**
 * ReflectionHelper.java
 * by Haisam Elkewidy
 *
 * Utility for reflection-based operations in the city simulation.
 */

package com.example.utility;

import java.lang.reflect.Method;

public class ReflectionHelper {
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        Method method = obj.getClass().getMethod(methodName, paramTypes);
        return method.invoke(obj, args);
    }
}
