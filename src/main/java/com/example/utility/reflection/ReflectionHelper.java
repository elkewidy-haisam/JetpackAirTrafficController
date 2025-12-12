/*
 * ReflectionHelper.java
 * Part of Jetpack Air Traffic Controller
 *
 * Utility class for safe reflection operations. Centralizes reflection-based method invocations used for radio command execution.
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.utility.reflection;

import java.lang.reflect.Method;

public class ReflectionHelper {

    public static boolean invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        if (target == null) {
            return false;
        }
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            method.invoke(target, arguments);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            return false;
        }
    }

    public static boolean invokeSingleArgMethod(Object target, String methodName, Object argument, Class<?> argumentType) {
        return invokeMethod(target, methodName, new Class<?>[]{argumentType}, new Object[]{argument});
    }

    public static boolean invokeTwoArgMethod(Object target, String methodName,
                                             Object arg1, Class<?> arg1Type,
                                             Object arg2, Class<?> arg2Type) {
        return invokeMethod(target, methodName,
                          new Class<?>[]{arg1Type, arg2Type},
                          new Object[]{arg1, arg2});
    }

    public static boolean invokeThreeArgMethod(Object target, String methodName,
                                               Object arg1, Class<?> arg1Type,
                                               Object arg2, Class<?> arg2Type,
                                               Object arg3, Class<?> arg3Type) {
        return invokeMethod(target, methodName,
                          new Class<?>[]{arg1Type, arg2Type, arg3Type},
                          new Object[]{arg1, arg2, arg3});
    }

    public static boolean hasMethod(Object target, String methodName, Class<?>... parameterTypes) {
        if (target == null) {
            return false;
        }
        try {
            target.getClass().getMethod(methodName, parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}