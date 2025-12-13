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

package com.example.utility;

import java.lang.reflect.Method;

public class ReflectionHelper {
    /**
     * Invokes a method on an object using reflection.
     * Dynamically calls a method by name with specified parameter types and arguments.
     * 
     * @param obj Object instance on which to invoke method
     * @param methodName Name of method to invoke
     * @param paramTypes Array of parameter types for method signature
     * @param args Array of argument values to pass to method
     * @return Result returned by invoked method
     * @throws Exception if method not found or invocation fails
     */
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        // Get Method object for specified method name and parameter types
        Method method = obj.getClass().getMethod(methodName, paramTypes);
        // Invoke method on object with provided arguments and return result
        return method.invoke(obj, args);
    }
}
