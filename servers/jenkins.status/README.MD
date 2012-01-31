# Jenkins Status

This bundle is capable of gathering information about given Jenkins servers.

## Configuration
A simple *.jenkins file with a property list of available Jenkins Servers is 
sufficient for bootstrapping a Jenkins Server list in the system. The file content
must follow the syntax below:
<server alias>=<server url>

The contents of a configuration file could look like this for instance:
localhost.tests=http://localhost:8080
integration.tests=http://example.com/jenkins/

The file will be picked up for a watched directory by the `felix.fileinstall` bundle.
In the current assemblies, this will be the "deploy" folder in the root of the 
assembly. Successfully queried servers will be made available as services in the 
OSGi runtime, using the `com.cibuddy.core.build.servers.IServer` interface. Removal
of the file or updates will be picked-up and the configuration of the IServer 
interfaces will be adapted automatically.

The bundle supports multiple files with different names (but identical file extensions)
and requires unique alias's for the servers in each configuration file. It is 
recommended to use globally unique alias's for all Jenkins servers. 

## Usage
When referring to a particular project, it is recommended to use the alias only and
the project name. In case the alias is not unique, please also provide the type and
file name (with extension) to identify the project you're referring to in the 
configuration of your project setup.