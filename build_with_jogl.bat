REM ============================================================================
REM build_with_jogl.bat - Builds the build with jogl artifacts
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that builds the build with jogl artifacts.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   build_with_jogl.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
REM === Automated build script for Air Traffic Controller project ===
REM Update sources.txt with all Java files (including subpackages)

dir /s /b src\main\java\*.java > sources.txt

REM Compile all Java files with JOGL libraries
REM Explicitly add jogl-all.jar and gluegen-rt.jar to the classpath for compilation
set JOGL_CP=lib\jogl-all.jar;lib\gluegen-rt.jar;out
"c:\Program Files\Java\jdk-25\bin\javac" -cp "%JOGL_CP%" -d out @sources.txt

if %ERRORLEVEL% neq 0 (
    echo Compilation failed. Check errors above.
    exit /b %ERRORLEVEL%
)

echo Compilation successful.

echo To run the application, use:
echo "c:\Program Files\Java\jdk-25\bin\java" -cp "%JOGL_CP%" com.example.App
