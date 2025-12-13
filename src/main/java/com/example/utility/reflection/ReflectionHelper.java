/**
 * ReflectionHelper component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides reflectionhelper functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core reflectionhelper operations
 * - Maintain necessary state for reflectionhelper functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility.reflection;

import java.lang.reflect.Method;

public class ReflectionHelper {

    /**
     * Invokes a method on target object using Java reflection.
     * 
     * @param target Object on which to invoke method
     * @param methodName Name of method to invoke
     * @param parameterTypes Array of parameter types for method signature
     * @param arguments Array of argument values to pass to method
     * @return true if invocation succeeded, false if error occurred
     */
    public static boolean invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        // Validate that target object is not null
        if (target == null) {
            return false;
        }
        try {
            // Look up method by name and parameter types on target's class
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            // Invoke the method with provided arguments
            method.invoke(target, arguments);
            // Return success if no exception thrown
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            // Return failure if method not found or invocation fails
            return false;
        }
    }

    /**
     * Convenience method to invoke a single-argument method.
     * 
     * @param target Object on which to invoke method
     * @param methodName Name of method to invoke
     * @param argument Single argument value
     * @param argumentType Type of the argument
     * @return true if invocation succeeded, false otherwise
     */
    public static boolean invokeSingleArgMethod(Object target, String methodName, Object argument, Class<?> argumentType) {
        // Delegate to general invokeMethod with single-element arrays
        return invokeMethod(target, methodName, new Class<?>[]{argumentType}, new Object[]{argument});
    }

    /**
     * Convenience method to invoke a two-argument method.
     * 
     * @param target Object on which to invoke method
     * @param methodName Name of method to invoke
     * @param arg1 First argument value
     * @param arg1Type Type of first argument
     * @param arg2 Second argument value
     * @param arg2Type Type of second argument
     * @return true if invocation succeeded, false otherwise
     */
    public static boolean invokeTwoArgMethod(Object target, String methodName,
                                             Object arg1, Class<?> arg1Type,
                                             Object arg2, Class<?> arg2Type) {
        // Delegate to general invokeMethod with two-element arrays
        return invokeMethod(target, methodName,
                          new Class<?>[]{arg1Type, arg2Type},
                          new Object[]{arg1, arg2});
    }

    /**
     * Convenience method to invoke a three-argument method.
     * 
     * @param target Object on which to invoke method
     * @param methodName Name of method to invoke
     * @param arg1 First argument value
     * @param arg1Type Type of first argument
     * @param arg2 Second argument value
     * @param arg2Type Type of second argument
     * @param arg3 Third argument value
     * @param arg3Type Type of third argument
     * @return true if invocation succeeded, false otherwise
     */
    public static boolean invokeThreeArgMethod(Object target, String methodName,
                                               Object arg1, Class<?> arg1Type,
                                               Object arg2, Class<?> arg2Type,
                                               Object arg3, Class<?> arg3Type) {
        // Delegate to general invokeMethod with three-element arrays
        return invokeMethod(target, methodName,
                          new Class<?>[]{arg1Type, arg2Type, arg3Type},
                          new Object[]{arg1, arg2, arg3});
    }

    /**
     * Checks if target object has a method with specified signature.
     * 
     * @param target Object to check for method
     * @param methodName Name of method to find
     * @param parameterTypes Parameter types of method signature
     * @return true if method exists, false otherwise
     */
    public static boolean hasMethod(Object target, String methodName, Class<?>... parameterTypes) {
        // Validate that target object is not null
        if (target == null) {
            return false;
        }
        try {
            // Attempt to look up method by name and parameter types
            target.getClass().getMethod(methodName, parameterTypes);
            // Return true if method found without exception
            return true;
        } catch (NoSuchMethodException e) {
            // Return false if method not found
            return false;
        }
    }
}