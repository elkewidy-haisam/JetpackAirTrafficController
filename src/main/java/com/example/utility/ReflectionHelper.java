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
     * Dynamically invokes a method on an object using Java reflection.
     * Useful for plugin systems, generic frameworks, or accessing methods
     * whose names are not known at compile time.
     * 
     * @param obj The target object on which to invoke the method
     * @param methodName Name of the method to invoke
     * @param paramTypes Array of parameter types for method signature matching
     * @param args Array of argument values to pass to the method
     * @return The return value from the invoked method (may be null for void methods)
     * @throws Exception If method doesn't exist, access is denied, or invocation fails
     */
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        // Use reflection to locate method by name and parameter signature
        // This searches public methods in the object's class and superclasses
        Method method = obj.getClass().getMethod(methodName, paramTypes);
        
        // Invoke the method on the target object with provided arguments
        // Returns the method's return value, or null for void methods
        // Throws exceptions if invocation fails or method throws an exception
        return method.invoke(obj, args);
    }
}
