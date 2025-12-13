REM ============================================================================
REM rebuild.bat - Clean and rebuild the project from scratch
REM ============================================================================
REM Purpose:
REM   Performs a clean Maven build of the entire Air Traffic Controller project,
REM   removing all compiled artifacts and recompiling from source. Useful for
REM   resolving dependency conflicts or ensuring a fresh build state.
REM
REM Usage:
REM   rebuild.bat
REM
REM Requirements:
REM   - Maven installation (configured via setup-maven.bat)
REM   - Java JDK 11 or higher
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
call setup-maven.bat
mvn clean compile
