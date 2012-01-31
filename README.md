# CIBuddy

The CIBuddy project intends to provide a framework to better monitor live your
CI environment status. This is accomplished by several hard and software connectors
providing you with quick feedback about what's going on in your build environment.

## Hardware Lights Integration

The CIBuddy application currently supports two hardware lights. First of all, the
well known Delcom lights and secondly the iBuddy MSN notification figures. Just plug-in
your lights and wire each one with your distinct build configuration. You could use
multiple lights and also mix and match different types of lights to indicate your
build status independently for each project for instance.

## Jenkins Server Integration

The jenkins.status subproject provides capabilities to consume a list of Jenkins
server url's and expose their configuration to the cibuddy application. After exposure,
these servers could be used for build checks.