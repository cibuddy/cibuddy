#!/bin/bash
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
                cd pax; java $JAVA_OPTS -cp .:$SCRIPTS:$SCRIPTS/pax-runner.jar org.ops4j.pax.runner.Run --args="file:runner-grizzly.args" $2 $3 $4 $5 $6 $7 $8 $9
                ;;
        debug)	printf "Starting with debug options set. \n"
                cd pax; java $JAVA_OPTS -cp .:$SCRIPTS:$SCRIPTS/pax-runner.jar org.ops4j.pax.runner.Run --args="file:runner-debug.args" $2 $3 $4 $5 $6 $7 $8 $9
                ;;
        esac
    else
      printf "Starting ${application.name}\n"
      cd pax; java $JAVA_OPTS -cp .:$SCRIPTS:$SCRIPTS/pax-runner.jar org.ops4j.pax.runner.Run "$@"
fi
	

