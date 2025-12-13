REM ============================================================================
REM run_with_jogl.bat - Runs the run with jogl application or process
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that runs the run with jogl application or process.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   run_with_jogl.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
REM === Run script for Air Traffic Controller project with JOGL native JARs ===

REM This script assumes you are using the native JARs, not extracted DLLs.
REM Make sure the following JARs are in your lib folder:
REM   jogl-all.jar, gluegen-rt.jar, jogl-all-natives-windows-amd64.jar, gluegen-rt-natives-windows-amd64.jar


setlocal
REM Explicitly add jogl-all.jar and gluegen-rt.jar to the classpath for running
set JOGL_CP=lib\jogl-all.jar;lib\gluegen-rt.jar;out

REM Set JOGL native library path
set NATIVE_PATH=.\lib

REM Add JVM flag for native access
set JVM_OPTS=--enable-native-access=ALL-UNNAMED

"c:\Program Files\Java\jdk-25\bin\java" %JVM_OPTS% -Djava.library.path=%NATIVE_PATH% -cp "%JOGL_CP%" com.example.App %*
endlocal
