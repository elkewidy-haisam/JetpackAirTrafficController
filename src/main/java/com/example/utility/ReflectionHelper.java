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

/**
 * Utility class providing reflection-based method invocation capabilities.
 * Allows dynamic method calls on objects at runtime using method names and parameters.
 */
public class ReflectionHelper {
    
    /**
     * Invokes a method on an object using reflection with specified parameters.
     * Looks up method by name and parameter types, then executes it with provided arguments.
     *
     * @param obj Target object to invoke method on
     * @param methodName Name of the method to invoke
     * @param paramTypes Array of parameter types for method signature matching
     * @param args Array of argument values to pass to the method
     * @return Return value from the invoked method (null if void)
     * @throws Exception If method not found, access denied, or invocation fails
     */
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        // Look up method on object's class using name and parameter types
        Method method = obj.getClass().getMethod(methodName, paramTypes);
        // Invoke the method with provided arguments and return the result
        return method.invoke(obj, args);
    }
}
