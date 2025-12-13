/**
 * Safe wrapper for Java reflection operations with simplified method invocation interface.
 * 
 * Purpose:
 * Provides utility methods for dynamically invoking methods on objects at runtime without compile-time
 * dependencies. Used primarily by the radio communication system to execute commands on flight objects
 * without tight coupling. Encapsulates reflection error handling and provides convenient overloads for
 * common parameter count scenarios (1, 2, or 3 arguments).
 * 
 * Key Responsibilities:
 * - Invoke methods dynamically by name with type-safe parameter passing
 * - Handle reflection exceptions gracefully (catch and log/ignore)
 * - Provide overloaded methods for 1, 2, and 3-argument invocations
 * - Check for method existence before invocation attempts
 * - Support RadioCommandExecutor's dynamic command execution
 * 
 * Interactions:
 * - Used extensively by RadioCommandExecutor for command dispatch
 * - Enables Radio to send instructions to flights without direct coupling
 * - Supports plugin-like architecture for extensible flight behaviors
 * - Alternative to direct method calls for loose coupling
 * 
 * Patterns & Constraints:
 * - Pure utility class with only static methods (stateless)
 * - Thread-safe due to lack of mutable state
 * - No type safety at compile time - relies on runtime checks
 * - Exceptions are propagated to caller for handling
 * - Requires exact method name and parameter type matching
 * - Duck typing approach: if method exists, it will be called
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility;

import java.lang.reflect.Method;

public class ReflectionHelper {
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        Method method = obj.getClass().getMethod(methodName, paramTypes);
        return method.invoke(obj, args);
    }
}
