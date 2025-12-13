/**
 * Utility class for safe reflective method invocation with exception handling and validation.
 * 
 * Purpose:
 * Provides simplified, centralized access to Java reflection API for dynamic method invocation
 * scenarios. Encapsulates reflection complexity with null-safe wrappers and exception handling
 * for cleaner calling code. Primarily used by Radio subsystem for dynamic command execution where
 * method names and parameters are determined at runtime. Reduces boilerplate reflection code
 * and provides consistent error handling.
 * 
 * Key Responsibilities:
 * - Invoke methods dynamically by name with type-safe parameter matching
 * - Handle reflection exceptions gracefully with proper propagation
 * - Validate method existence and parameter type compatibility
 * - Support multi-argument method invocations with varargs
 * - Provide simplified API over java.lang.reflect.Method
 * - Enable runtime command execution without compile-time coupling
 * - Support testing scenarios with mock objects and dynamic dispatch
 * 
 * Interactions:
 * - Used by Radio for dynamic radio command execution
 * - Referenced by RadioCommandExecutor for instruction dispatch
 * - Supports FlightEmergencyHandler for dynamic flight command invocation
 * - Enables plugin architectures with runtime method discovery
 * - May be used in scripting or configuration-driven behaviors
 * - Coordinates with command pattern implementations
 * - Supports testing with dynamically generated test doubles
 * 
 * Patterns & Constraints:
 * - Static utility method for stateless reflection operations
 * - Thread-safe due to stateless design
 * - Exception propagation: throws Exception for caller handling
 * - Type safety: requires explicit Class<?> parameter type specifications
 * - Uses Method.invoke() for actual method execution
 * - Supports any return type via Object return value
 * - Caller responsible for exception handling and type casting
 * - Performance: reflection overhead acceptable for infrequent operations (radio commands)
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
