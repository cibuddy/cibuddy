@echo off
SETLOCAL
set _SCRIPTS_=%~dp0
cd pax
echo Welcome to ${application.name}

if "%1%"=="test" goto test
if "%1%"=="mem" goto mem
:: the default runner is requested
goto default

:test
echo starting with web server provisioning (not useful right now - no Servlet is implemented just yet)
CALL java %JAVA_OPTS% -cp ".;%_SCRIPTS_%;%_SCRIPTS_%\pax\pax-runner.jar" org.ops4j.pax.runner.Run --args="file:runner-grizzly.args" %2 %3 %4 %5 %6 %7 %8 %9
goto:eof

:debug
echo starting with remote debugging enabled
CALL java %JAVA_OPTS% -cp ".;%_SCRIPTS_%;%_SCRIPTS_%\pax\pax-runner.jar" org.ops4j.pax.runner.Run --args="file:runner-debug.args" %2 %3 %4 %5 %6 %7 %8 %9
goto:eof

:default
echo starting ${application.name}
CALL java %JAVA_OPTS% -cp ".;%_SCRIPTS_%;%_SCRIPTS_%\pax\pax-runner.jar" org.ops4j.pax.runner.Run %1 %2 %3 %4 %5 %6 %7 %8 %9
goto:eof
