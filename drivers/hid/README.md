# HID driver bundle

Addressing hardware from within Java is not really great supported. Fortunately there are a couple of fine projects out there, that did all the hard work. The HID driver bundle in CIBuddy is a simple wrapper of the [javahidapi][1] project and contains binaries for Mac OS X (32/64 bit), Linux (32/64 bit) and Windows(32/64 bit) on the x86 architecture. Currently only the OS X version is thoroughly tested and there are known issues with Linux for instance (not detecting the usage count correctly). You might encounter problems on your OS, so any help to sort this out would be greatly appreciated. There is currently no tweaking of the native binaries coming from the javahidapi project whatsoever. You might also want to look at the [libusb][2] and [hidapi][3] project, which is the core of javahidapi for a list of prerequisites.

The code is very thoroughly documented (javadoc) and should serve as an excellent starting point for further investigations. The binary bundle is regularly pushed to the [OSS repository of Sonatype][5] in case of SNAPSHOTS and final versions could be found in [Maven Central][6] once available. 

## Native code and portability

This bundle should attach as soon as your OS seems compatible (matching os properties), but this is not thoroughly tested. Currently the code runs successfully on an 
`OS X 10.7 Lion MacBook Air 13" 1.7GHz i5` for sure. For Linux you require [libusb 1.0][2]!!! For more details take a look at the [libusb project][2].

## Compatibility Matix

The following matix shows what has actually been tested. In case you encounter a problem with either of these, please file an issue. Also notifications about other verified environments would be appreciated!
<table>
	<tr>
		<td>OS</td>
		<td>Version(s)</td>
		<td>Processor</td>
		<td>Architecture</td>
		<td>Verified</td>
		<td>Build ID</td>
		<td>Comment</td>
	</tr>
	<tr>
		<td>OS X</td>
		<td>10.8.1</td>
		<td>i5</td>
		<td>x86 (64bit)</td>
		<td>OK</td>
		<td>20120826</td>
		<td>no issues</td>
	</tr>
	<tr>
		<td>Ubuntu Linux</td>
		<td>11.04</td>
		<td>i7</td>
		<td>x86 (64bit)</td>
		<td>OK</td>
		<td>20120301</td>
		<td>no issues</td>
	</tr>
	<tr>
		<td>Windows</td>
		<td>XP SP3</td>
		<td>Core 2 duo</td>
		<td>x86 (32bit)</td>
		<td>OK</td>
		<td>20120301</td>
		<td>no issues</td>
	</tr>
	<tr>
		<td>Windows</td>
		<td>7 (Ultimate)</td>
		<td>i7</td>
		<td>x86 (32bit)</td>
		<td>OK</td>
		<td>20120301</td>
		<td>no issues</td>
	</tr>
	<tr>
		<td>Windows</td>
		<td>8 (Developer Preview)</td>
		<td>i7</td>
		<td>x86 (32bit)</td>
		<td>OK</td>
		<td>20120301</td>
		<td>no issues</td>
	</tr>
</table>

[1]: http://code.google.com/p/javahidapi/ "JavaHIDAPI project"
[2]: http://www.libusb.org/ "LibUSB project"
[3]: https://github.com/signal11/hidapi "HIDAPI Project at GitHub"
[4]: https://github.com/cibuddy/cibuddy/tree/master/drivers/hid "CIBuddy HID Driver wrapper"
[5]: https://oss.sonatype.org/content/repositories/snapshots/com/cibuddy/hid/ "Sonatype OSS Repository"
[6]: http://search.maven.org/ "Maven Central Artifact Search"
