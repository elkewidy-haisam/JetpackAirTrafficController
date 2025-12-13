REM ============================================================================
REM check_errors.bat - Checks check errors for errors or validation
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that checks check errors for errors or validation.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   check_errors.bat
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
call setup-maven.bat
mvn clean compile > compile_output.txt 2>&1
type compile_output.txt
