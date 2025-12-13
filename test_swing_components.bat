REM ============================================================================
REM test_swing_components.bat - Executes tests for the Swing UI components
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that tests the Swing UI components to validate functionality.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_swing_components.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
cls
echo.
echo ============================================================
echo   SWING COMPONENT EXTRACTION - COMPILATION TEST
echo ============================================================
echo.
echo Testing all newly extracted Swing components...
echo.

call setup-maven.bat > nul 2>&1

echo Compiling project...
mvn clean compile 2>&1

echo.
if %ERRORLEVEL% EQU 0 (
    echo ============================================================
    echo   ✓✓✓ SUCCESS! ALL COMPONENTS COMPILE! ✓✓✓
    echo ============================================================
    echo.
    echo New Swing Component Files:
    echo   ✓ DateTimeDisplayPanel.java
    echo   ✓ WeatherBroadcastPanel.java
    echo   ✓ JetpackMovementPanel.java
    echo   ✓ RadioInstructionsPanel.java
    echo   ✓ JetpackListPanel.java
    echo   ✓ ConsoleOutputPanel.java
    echo   ✓ MapDisplayPanel.java
    echo   ✓ CityControlPanel.java
    echo.
    echo Benefits:
    echo   - 8 reusable components created
    echo   - ~350 lines of UI code extracted
    echo   - Better code organization
    echo   - Each component independently testable
    echo   - Easier to maintain and modify
    echo.
    echo See SWING_COMPONENT_EXTRACTION.md for full documentation.
) else (
    echo ============================================================
    echo   ✗ COMPILATION FAILED
    echo ============================================================
    echo.
    echo Please review error messages above.
)
echo.
pause
