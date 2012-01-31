@echo off
SETLOCAL
set _SCRIPTS_=%~dp0

echo Welcome to ${application.name}
echo The deamon script currently does not support all features of the plain cmd script. You might have to tweak it first.
java -cp ".;%_SCRIPTS_%;%_SCRIPTS_%\pax-runner.jar" org.ops4j.pax.runner.daemon.DaemonLauncher %*
