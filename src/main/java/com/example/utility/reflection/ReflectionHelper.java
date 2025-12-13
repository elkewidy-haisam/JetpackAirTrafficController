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

/**
 * Utility class providing safe reflection-based method invocation helpers.
 * Encapsulates complex reflection API calls with simplified, exception-safe methods.
 */
public class ReflectionHelper {

    /**
     * Invokes a method on a target object using reflection.
     * Handles exceptions internally and returns success/failure status.
     *
     * @param target The object instance to invoke the method on
     * @param methodName Name of the method to invoke
     * @param parameterTypes Array of parameter types for method signature matching
     * @param arguments Array of argument values to pass to the method
     * @return true if method was invoked successfully, false otherwise
     */
    public static boolean invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        // Guard against null target object
        if (target == null) {
            return false;  // Cannot invoke method on null object
        }
        try {
            // Look up the method by name and parameter types using reflection
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            // Invoke the method with provided arguments
            method.invoke(target, arguments);
            // Return success if no exceptions thrown
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            // Catch all reflection-related exceptions and return failure
            return false;
        }
    }

    /**
     * Convenience method for invoking a method with a single argument.
     * Simplifies common case of one-parameter method calls.
     *
     * @param target The object instance to invoke the method on
     * @param methodName Name of the method to invoke
     * @param argument The single argument value to pass
     * @param argumentType The type of the argument for signature matching
     * @return true if method was invoked successfully, false otherwise
     */
    public static boolean invokeSingleArgMethod(Object target, String methodName, Object argument, Class<?> argumentType) {
        // Delegate to general invokeMethod with single-element arrays
        return invokeMethod(target, methodName, new Class<?>[]{argumentType}, new Object[]{argument});
    }

    /**
     * Convenience method for invoking a method with two arguments.
     * Simplifies common case of two-parameter method calls.
     *
     * @param target The object instance to invoke the method on
     * @param methodName Name of the method to invoke
     * @param arg1 First argument value
     * @param arg1Type Type of first argument for signature matching
     * @param arg2 Second argument value
     * @param arg2Type Type of second argument for signature matching
     * @return true if method was invoked successfully, false otherwise
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
     * Convenience method for invoking a method with three arguments.
     * Simplifies common case of three-parameter method calls.
     *
     * @param target The object instance to invoke the method on
     * @param methodName Name of the method to invoke
     * @param arg1 First argument value
     * @param arg1Type Type of first argument for signature matching
     * @param arg2 Second argument value
     * @param arg2Type Type of second argument for signature matching
     * @param arg3 Third argument value
     * @param arg3Type Type of third argument for signature matching
     * @return true if method was invoked successfully, false otherwise
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
     * Checks if a target object has a method with specified signature.
     * Non-invasive check that doesn't invoke the method.
     *
     * @param target The object instance to check for method existence
     * @param methodName Name of the method to look for
     * @param parameterTypes Variable arguments of parameter types for signature matching
     * @return true if method exists with matching signature, false otherwise
     */
    public static boolean hasMethod(Object target, String methodName, Class<?>... parameterTypes) {
        // Guard against null target object
        if (target == null) {
            return false;  // Null object has no methods
        }
        try {
            // Attempt to look up the method by name and parameter types
            target.getClass().getMethod(methodName, parameterTypes);
            // If lookup succeeds, method exists
            return true;
        } catch (NoSuchMethodException e) {
            // Method doesn't exist with that signature
            return false;
        }
    }
}