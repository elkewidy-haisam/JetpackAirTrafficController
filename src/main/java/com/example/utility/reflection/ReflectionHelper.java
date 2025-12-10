package com.example.utility.reflection;

import java.lang.reflect.Method;

/**
 * ReflectionHelper.java
 * 
 * Utility class for safe reflection operations.
 * Centralizes reflection-based method invocations used for radio command execution.
 */
public class ReflectionHelper {
    
    /**
     * Safely invokes a method on an object using reflection
     * 
     * @param target Target object to invoke method on
     * @param methodName Name of the method to invoke
     * @param parameterTypes Array of parameter types
     * @param arguments Array of arguments to pass to the method
     * @return true if invocation succeeded, false if method doesn't exist or invocation failed
     */
    public static boolean invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        if (target == null) {
            return false;
        }
        
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            method.invoke(target, arguments);
            return true;
        } catch (Exception e) {
            // Method doesn't exist or invocation failed
            return false;
        }
    }
    
    /**
     * Invokes a method with no return value expectation
     * 
     * @param target Target object
     * @param methodName Method name
     * @param argument Single argument
     * @param argumentType Type of the argument
     * @return true if successful
     */
    public static boolean invokeSingleArgMethod(Object target, String methodName, Object argument, Class<?> argumentType) {
        return invokeMethod(target, methodName, new Class<?>[]{argumentType}, new Object[]{argument});
    }
    
    /**
     * Invokes a method with two arguments
     * 
     * @param target Target object
     * @param methodName Method name
     * @param arg1 First argument
     * @param arg1Type First argument type
     * @param arg2 Second argument
     * @param arg2Type Second argument type
     * @return true if successful
     */
    public static boolean invokeTwoArgMethod(Object target, String methodName, 
                                            Object arg1, Class<?> arg1Type,
                                            Object arg2, Class<?> arg2Type) {
        return invokeMethod(target, methodName, 
                          new Class<?>[]{arg1Type, arg2Type}, 
                          new Object[]{arg1, arg2});
    }
    
    /**
     * Invokes a method with three arguments
     * 
     * @param target Target object
     * @param methodName Method name
     * @param arg1 First argument
     * @param arg1Type First argument type
     * @param arg2 Second argument
     * @param arg2Type Second argument type
     * @param arg3 Third argument
     * @param arg3Type Third argument type
     * @return true if successful
     */
    public static boolean invokeThreeArgMethod(Object target, String methodName,
                                              Object arg1, Class<?> arg1Type,
                                              Object arg2, Class<?> arg2Type,
                                              Object arg3, Class<?> arg3Type) {
        return invokeMethod(target, methodName,
                          new Class<?>[]{arg1Type, arg2Type, arg3Type},
                          new Object[]{arg1, arg2, arg3});
    }
    
    /**
     * Checks if an object has a specific method
     * 
     * @param target Target object
     * @param methodName Method name to check for
     * @param parameterTypes Parameter types of the method
     * @return true if method exists, false otherwise
     */
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
