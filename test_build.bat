REM ============================================================================
REM test_build.bat - Executes test build to validate functionality
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that executes test build to validate functionality.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_build.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
cd /d c:\Users\Elkewidy\Desktop\e10b\e10btermproject
call mvn clean compile
