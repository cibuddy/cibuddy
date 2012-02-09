# PAX based CIBuddy assembly

The assembly contains a ready to run version of CIBuddy. It has all required bundles
contained in the distribution (as well as a test bundle for a build indicator that
doesn't require a hardware lamp). 

## Usage

1. build with Maven (mvn clean install) or download the [binary][1]
2. unpack the binary (zip or tar.gz depending on your OS)
3. cd into the unpacked folder
4. run "bash pax-run.sh" (Unix or Mac) or "pax-run.bat" (Windows)
5. you're done ;-)

## Configuration

The distribution contains a "deploy" folder. This folder could handle two types of
configuration files. First of all the list of jenkins servers. The file is called
`*.jenkins` and a [sample][2] is located in the folder already. The syntax is simply a
properties file with the key being a unique alias for the server and the URI that
points to the Jenkins web ui (the xml api of jenkins must be enabled and not
password protected to gather build information - for now). 

<pre>
localhost.test=http://localhost:8080
</pre>

Second, there is the actual 
setup configuration for your build tests (what should happen if a build has a certain
result?). This is a (rather) simple xml file specifying what happens when. There
is also an [example][3] in the `deploy` folder. The respective [xsd][4] is located in the 
`com.cibuddy.project.configuration` project under `src/main/resources`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<setup  xmlns="http://com.cibuddy.project.configuration/schema/setup/v1-0" 
		xmlns:xs="http://www.w3.org/2001/XMLSchema-instance" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <name>test</name>
    <configuration alias="defaultConfig">
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
            <!-- stop at the first match and execute the indicate action -->
            <action status="blue" condition="all">
                <indicate>success</indicate>
            </action>
            <action status="red" condition="1:*"><!-- at least one -->
                <indicate>failure</indicate>
            </action>
            <action status="yellow" condition="1:*"><!-- at least one -->
                <indicate>warning</indicate>
            </action>
            <action status="default"><!-- if you reach this, indicate the default -->
                <indicate>off</indicate>
            </action>
        </trigger>
    </configuration>
</setup>
```

## Native code and portability

Addressing hardware from within Java is not really great supported. Fortunately there
are a couple of fine projects out there, that did all the hard work. As of time of 
writing, CIBuddy supports the [javahidapi][5] driver, wrapped in a distinct bundle and
OSGi-efied. This bundle should attach as soon as your OS seems compatible, but this is
not thoroughly tested. Currently the code runs successfully on an 
`OS X 10.7 Lion MacBook Air 13" 1.7GHz i5`. For Linux you require [libusb 1.0][6]!!! For 
more details take a look at the [libusb project][6] or the cibuddy [hid driver][7] bundle.

## Compatibility Matix

<table>
	<tr>
		<td>OS</td>
		<td>Version(s)</td>
		<td>Processor</td>
		<td>Architecture</td>
		<td>Verified</td>
		<td>Comment</td>
	</tr>
	<tr>
		<td>OS X</td>
		<td>10.7.3</td>
		<td>i5</td>
		<td>x86_64</td>
		<td>OK</td>
		<td>on one machine I got an error. See <a src="http://code.google.com/p/javahidapi/issues/detail?id=7">this</a> for details.</td>
	</tr>
	<tr>
		<td>Ubuntu Linux</td>
		<td>11.04</td>
		<td>i7</td>
		<td>x86_64</td>
		<td>OK</td>
		<td>no issues</td>
	</tr>
	<tr>
		<td>Windows</td>
		<td>XP SP3</td>
		<td>Core 2 duo</td>
		<td>x86 (32bit)</td>
		<td>Unknown</td>
		<td>next in line for testing</td>
	</tr>
</table>


[1]: https://github.com/cibuddy/cibuddy/downloads "CIBuddy Binary Downloads"
[2]: https://github.com/cibuddy/cibuddy/blob/master/distributions/pax.assembly/src/main/resources/deploy/testLocalhost.jenkins "server configuration file"
[3]: https://github.com/cibuddy/cibuddy/blob/master/distributions/pax.assembly/src/main/resources/deploy/sample.xml "test build configuration file"
[4]: https://github.com/cibuddy/cibuddy/blob/master/main/project.configuration/src/main/resources/configuration-1.0.xsd "CIBuddy project configuration xsd"
[5]: http://code.google.com/p/javahidapi/ "JavaHIDAPI project"
[6]: http://www.libusb.org/ "LibUSB project"
[7]: https://github.com/cibuddy/cibuddy/tree/master/drivers/hid "CIBuddy HID Driver wrapper"
[8]: http://code.google.com/p/javahidapi/issues/detail?id=7 "Compilation error on Mac OS X"
