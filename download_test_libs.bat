@echo off
REM Download JUnit and Hamcrest libraries for testing

echo Downloading JUnit 4.13.2...
mkdir lib 2>nul
curl -L -o lib\junit-4.13.2.jar https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar

echo Downloading Hamcrest Core 1.3...
curl -L -o lib\hamcrest-core-1.3.jar https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar

echo.
echo Libraries downloaded successfully!
echo.
echo You can now run tests with:
echo   run_tests.bat
pause
