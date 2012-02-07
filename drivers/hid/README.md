# HID driver bundle

The HID driver bundle is a simple wrapper of the [javahidapi][1] project and contains
binaries for Mac OS X 10.7 (64bit), Linux (64bit) and Windows(32bit). Currently only
the OS X version is tested and there are known issues. You might encounter problems
on your OS, so any help to sort this out would be greatly appreciated. I solely 
used the [javahidapi][1] project to generate the binaries, so there is no tweaking
whatsoever involved here yet. You might also want to look at the [libusb][2] and 
[hidapi][3] project,
which is the core of javahidapi.

[1]: http://code.google.com/p/javahidapi/ "JavaHIDAPI project"
[2]: http://www.libusb.org/ "LibUSB project"
[3]: https://github.com/signal11/hidapi "HIDAPI Project at GitHub"