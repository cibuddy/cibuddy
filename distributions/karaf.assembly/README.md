# The Karaf based CIBuddy application

This project creates the binaries that could be used for all kinds of installation scenario's. It is based on the [Karaf OSGi][1] framework an utilizes a couple of its features for convenient usage like a shell extension.

## Installation

There are two ways of installing the application. One is a more [interactive approach][2], that uses a shell. The other one installs the application as a [system service][3]. For simplicity only the first one is explained here. For the second one please refer to the [karaf manual][3].

1. Unpack the `karaf.assembly-*.zip` or `karaf.assembly-*.tar.gz` file depending on your OS. 
2. Change into the bin folder (like: `cd ./bin`)
3. Start the application by calling `bash ./karaf` on Linux/ Mac or `karaf.bat` on Windows.
4. To stop the application hit `ctrl` + `D`

## Configuration

The distribution contains a "deploy" folder. This folder could handle two types of
configuration files. First of all the list of jenkins servers. The file is called
`*.jenkins` and a [sample][4] is located in the folder already. The syntax is simply a
properties file with the key being a unique alias for the server and the URI that
points to the Jenkins web ui (the xml api of jenkins must be enabled and not
password protected to gather build information - for now). 

<pre>
localhost.test=http://localhost:8080
</pre>

Second, there is the actual 
setup configuration for your build tests (what should happen if a build has a certain
result?). This is a (rather) simple xml file specifying what happens when. There
is also a [sample][5] file in the `deploy` folder. The respective [xsd][6] is located in the 
`com.cibuddy.project.configuration` project under `src/main/resources`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<setup  xmlns="http://com.cibuddy.project.configuration/schema/setup/v1-0" 
		xmlns:xs="http://www.w3.org/2001/XMLSchema-instance" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <name>SampleConfigurationFile</name>
    <configuration alias="aSimpleConfigurationForLocalhost">
        <projects>
            <project>
                <name>cibuddy</name>
                <projectURL>http://localhost:8080/job/cibuddy%20masterbuild/</projectURL>
            </project>
        </projects>
        <trigger>
            <statusIndicator>
                <id>0</id>
            </statusIndicator>
            <action condition="all">
                <status>blue</status>
                <status>blue_anime</status>
                <indicate>success</indicate>
            </action>
            <action condition="1:*"><!-- at least one -->
                <status>red</status>
                <status>red_anime</status>
                <indicate>failure</indicate>
            </action>
            <action condition="1:*"><!-- at least one -->
                <status>yellow</status>
                <status>yellow_anime</status>
                <indicate>warning</indicate>
            </action>
            <action condition="default"><!-- catch all -->
                <indicate>off</indicate>
            </action>
        </trigger>
    </configuration>

</setup>
```

## Some Help

The application offers a couple of commands that allow for easier testing before deploying a concrete configuration. For instance, you could list all connected `indicators` (lights) and test them to see if they are actually working. You could also investigate your server configurations and see if the projects you're interested in are registered and known to the system. The easiest way to find out more, just type `cibuddy` and hit `tab`. This will bring up the list with all command exposed by the application specific to CIBuddy. 

### CIBuddy custom commands
This is an incomplete list of available commands in the karaf assembly.
<table>
	<tr>
		<td><b>Command</b></td>
		<td><b>Description</b></td>
	</tr>
	<tr>
		<td><code>cibuddy:indicator-list</code></td>
		<td>Displays a list of all available indicators.</td>
	</tr>
	<tr>
		<td><code>cibuddy:indicator-test</code></td>
		<td>Expects an action and the index of the indicator to use. A sample would look like this: <code>cibuddy:indicator-test success 0</code>. The id is the same as it would be used for a concrete build test configuration. A command completer helps with identifying the possible actions (just press <code>tab</code>).</td>
	</tr>
	<tr>
		<td><code>cibuddy:server-details</code></td>
		<td>Displays a list of all projects associated with a given server. The server given is the id displayed in the server-list command.</td>
	</tr>
	<tr>
		<td><code>cibuddy:server-list</code></td>
		<td>Displays a list of all configured servers.</td>
	</tr>
</table>

[1]: http://karaf.apache.org/ "Karaf Homepage"
[2]: http://karaf.apache.org/manual/latest-2.2.x/users-guide/start-stop.html "Starting/ Stopping of Karaf"
[3]: http://karaf.apache.org/manual/latest-2.2.x/users-guide/wrapper.html "Service integration of Karaf"
[4]: https://github.com/cibuddy/cibuddy/blob/master/distributions/pax.assembly/src/main/resources/deploy/testLocalhost.jenkins "server configuration file"
[5]: https://github.com/cibuddy/cibuddy/blob/master/distributions/pax.assembly/src/main/resources/deploy/sample.xml "test build configuration file"
[6]: https://github.com/cibuddy/cibuddy/blob/master/main/project.configuration/src/main/resources/configuration-1.0.xsd "CIBuddy project configuration xsd"

