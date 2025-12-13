REM ============================================================================
REM test_jogl.bat - Executes test jogl to validate functionality
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that executes test jogl to validate functionality.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_jogl.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
echo Testing JOGL 3D Rendering System...
cd /d "%~dp0"

echo.
echo Step 1: Compiling with JOGL dependencies...
call mvn compile -q

if %errorlevel% neq 0 (
    echo COMPILATION FAILED
    pause
    exit /b 1
)

echo.
echo Step 2: Checking JOGL classes...
if exist "target\classes\com\example\ui\utility\JOGLRenderer3D.class" (
    echo [OK] JOGLRenderer3D.class found
) else (
    echo [FAIL] JOGLRenderer3D.class not found
)

if exist "target\classes\com\example\ui\panels\JOGL3DPanel.class" (
    echo [OK] JOGL3DPanel.class found
) else (
    echo [FAIL] JOGL3DPanel.class not found
)

echo.
echo Step 3: Checking JOGL dependencies...
call mvn dependency:tree -Dincludes=org.jogamp.jogl 2>nul | findstr "jogl-all-main"
if %errorlevel% equ 0 (
    echo [OK] JOGL dependencies resolved
) else (
    echo [WARN] JOGL dependencies may need download
)

echo.
echo ========================================
echo JOGL 3D SYSTEM READY
echo ========================================
echo.
echo To run: mvn exec:java -Dexec.mainClass="com.example.App"
echo Then click Track on any jetpack to see JOGL rendering
echo.
pause
