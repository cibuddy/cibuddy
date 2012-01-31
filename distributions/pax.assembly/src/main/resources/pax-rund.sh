#!/bin/sh
#
# Script to run Pax Runner, which starts OSGi frameworks with applications.
#
#

SCRIPTS=`readlink $0`
if [ "${SCRIPTS}" != "" ]
then
  SCRIPTS=`dirname $SCRIPTS`
else
  SCRIPTS=`dirname $0`
fi
if [ "$1" ]
	then
	  case $1 in
        test)	printf "Starting with web server provisioning (not useful right now - no Servlet is implemented just yet)\n"
                cd pax; exec java $JAVA_OPTS -cp .:$SCRIPTS:$SCRIPTS/pax-runner.jar org.ops4j.pax.runner.Run --args="file:runner-grizzly.args" "$@"
                ;;
        
        esac
    else
      printf "Starting ${application.name}\n"
      cd pax; exec java $JAVA_OPTS -cp .:$SCRIPTS:$SCRIPTS/pax-runner.jar org.ops4j.pax.runner.daemon.DaemonLauncher "$@"
fi

