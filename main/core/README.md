# Core Project for CIBuddy

The core module contains all required interfaces to programmatically interact with
the application. Take a look at the exposed packages of this OSGi-Bundle to get
a better impression.

## Package com.cibuddy.core.build.configuration

The configuration package contains the main interface to identify CI servers
configured to be tested and their associated conditions and build indicators (a
build indicator could be a physical lamp or any other gadget). For an example, 
take a look at the com.cibuddy.project.configuration project.

## Package com.cibuddy.core.build.indicator

Indicators as defined in this package are used to visualize the status of a particular
build server (or a set of them). Build indicator could be anything, from a usb light,
some pc attached gadget or even something entirely in software (not sure what the use
would be, but it's possible). Out of the box provided implementations are located in
the com.cibuddy.lights group.

## Package com.cibuddy.core.build.server
A server is an abstract concept for a CI endpoint that is capable of holding information
of various individual build projects. Each server configuration exposes its configured
projects, which in return could be used to track the status. Currently supported 
servers could be found in the com.cibuddy.servers group. At the moment only the jenkins
server is supported. Hudson might work, but it hasn't been tested yet.