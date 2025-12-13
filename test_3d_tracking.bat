REM ============================================================================
REM test_3d_tracking.bat - Executes test 3d tracking to validate functionality
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that executes test 3d tracking to validate functionality.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_3d_tracking.bat
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
echo Testing 3D Tracking Window Compilation...
call setup-maven.bat > nul 2>&1
mvn clean compile -q
if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] 3D Tracking Window compiled successfully!
    echo.
) else (
    echo.
    echo [ERROR] Compilation failed. Check errors above.
    echo.
)
pause
