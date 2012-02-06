# PAX based CIBuddy assembly

The assembly contains a ready to run version of CIBuddy. It has all required bundles
contained in the distribution (as well as a test bundle for a build indicator that
doesn't require a hardware lamp). 

## Usage

- build with Maven (mvn clean install) or download the binary
- unpack the binary (zip or tar.gz depending on your OS)
- cd into the unpacked folder
- run "bash pax-run.sh" (Unix or Mac) or "pax-run.bat" (Windows)
- you're done.

## Configuration

The distribution contains a "deploy" folder. This folder could handle two types of
configuration files. First of all the list of jenkins servers. The file is called
*.jenkins and a sample is located in the folder already. The syntax is simply a
properties file with the key being a unique alias for the server and the URI that
points to the Jenkins web ui (the xml api of jenkins must be enabled and not
password protected to gather build information). Second, there is the actual 
setup configuration for your build tests (what should happen if a build has a certain
result?). This is a (rather) simple xml file specifying what happens when. There
is also an example in the deploy folder. The respective xsd is located in the 
com.cibuddy.project.configuration project under src/main/resources.


