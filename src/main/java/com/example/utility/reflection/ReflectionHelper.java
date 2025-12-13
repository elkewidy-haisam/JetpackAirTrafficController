/**
 * Duplicate/alias of com.example.utility.ReflectionHelper providing reflection utilities.
 * 
 * Purpose:
 * Subpackage version of ReflectionHelper with identical functionality. Provides safe method invocation via
 * reflection. May be consolidated with parent package version in future refactoring.
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility.reflection;

import java.lang.reflect.Method;

public class ReflectionHelper {

    /**
     * Invokes a method on target object using reflection with specified parameters.
     * Returns true if successful, false if method not found or invocation fails.
     * 
     * @param target the object to invoke method on
     * @param methodName name of method to invoke
     * @param parameterTypes array of parameter type classes
     * @param arguments array of actual argument values
     * @return true if invocation succeeded, false otherwise
     */
    public static boolean invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        if (target == null) {  // Validate target is not null
            return false;  // Cannot invoke on null target
        }
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);  // Find method by name and types
            method.invoke(target, arguments);  // Invoke method with arguments
            return true;  // Success - method invoked
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            return false;  // Failed - method not found or invocation error
        }
    }

    /**
     * Convenience method to invoke single-argument method.
     * Simplifies calling invokeMethod for common single-parameter case.
     * 
     * @param target the object to invoke method on
     * @param methodName name of method to invoke
     * @param argument the single argument value
     * @param argumentType the type of the argument
     * @return true if invocation succeeded, false otherwise
     */
    public static boolean invokeSingleArgMethod(Object target, String methodName, Object argument, Class<?> argumentType) {
        return invokeMethod(target, methodName, new Class<?>[]{argumentType}, new Object[]{argument});
    }

    /**
     * Convenience method to invoke two-argument method.
     * Simplifies calling invokeMethod for common two-parameter case.
     * 
     * @param target the object to invoke method on
     * @param methodName name of method to invoke
     * @param arg1 first argument value
     * @param arg1Type first argument type
     * @param arg2 second argument value
     * @param arg2Type second argument type
     * @return true if invocation succeeded, false otherwise
     */
    public static boolean invokeTwoArgMethod(Object target, String methodName,
                                             Object arg1, Class<?> arg1Type,
                                             Object arg2, Class<?> arg2Type) {
        return invokeMethod(target, methodName,
                          new Class<?>[]{arg1Type, arg2Type},  // Build parameter type array
                          new Object[]{arg1, arg2});            // Build argument value array
    }

    /**
     * Convenience method to invoke three-argument method.
     * Simplifies calling invokeMethod for common three-parameter case.
     * 
     * @param target the object to invoke method on
     * @param methodName name of method to invoke
     * @param arg1 first argument value
     * @param arg1Type first argument type
     * @param arg2 second argument value
     * @param arg2Type second argument type
     * @param arg3 third argument value
     * @param arg3Type third argument type
     * @return true if invocation succeeded, false otherwise
     */
    public static boolean invokeThreeArgMethod(Object target, String methodName,
                                               Object arg1, Class<?> arg1Type,
                                               Object arg2, Class<?> arg2Type,
                                               Object arg3, Class<?> arg3Type) {
        return invokeMethod(target, methodName,
                          new Class<?>[]{arg1Type, arg2Type, arg3Type},  // Build parameter type array
                          new Object[]{arg1, arg2, arg3});                // Build argument value array
    }

    /**
     * Checks if target object has a method with specified name and parameter types.
     * Useful for checking method availability before attempting invocation.
     * 
     * @param target the object to check for method
     * @param methodName name of method to look for
     * @param parameterTypes parameter types of the method
     * @return true if method exists, false otherwise
     */
    public static boolean hasMethod(Object target, String methodName, Class<?>... parameterTypes) {
        if (target == null) {  // Validate target is not null
            return false;  // Null target has no methods
        }
        try {
            target.getClass().getMethod(methodName, parameterTypes);  // Try to find method
            return true;  // Method found
        } catch (NoSuchMethodException e) {
            return false;  // Method not found
        }
    }
}