@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
echo Compiling project to identify errors...
call setup-maven.bat > nul 2>&1
mvn clean compile 2>&1 | tee compile_errors.txt
echo.
echo Output saved to compile_errors.txt
