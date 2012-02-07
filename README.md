# CIBuddy

The CIBuddy project intends to provide a framework to better live monitor your
CI environment status. This is accomplished by several hard and software connectors
providing you with quick feedback about what's going on in your build environment.

## Hardware Light Integration

The CIBuddy application currently supports two hardware lights. First of all, the
well known [Delcom Lights][1] and secondly the [i-Buddy][2] MSN notification figures. 
Just plug-in your lights and wire each one with your distinct build configuration. 
You could use multiple lights and also mix and match different types of lights to 
indicate your build status independently for each project for instance. The cool 
thing is that an i-Buddy only costs about 10$, which is a very cool price for the fun.

![The i-Buddy for instance...][3] 

## Jenkins Server Integration

The jenkins.status subproject provides capabilities to consume a list of Jenkins
server url's and expose their configuration to the CIBuddy application. After exposure,
these servers could be used for build checks.

## Getting Started

Currently the project is in the early alpha stage. However, in case you're one of
the brave, you could give it a spin by following the instructions detailed [here][5].
The binaries available could be found [here][6].

## Known Issues

As mentioned before, this is the early alpha phase of the project and very likely,
there will be problems when trying to run the binary. You could find all known issues
in the [Issues Section][6] of this project. In case you encounter a new one, please 
do not hesitate to open an issue here as well.

## References:
* [Delcom Produkt Homepage][1]
* [i-Buddy MSN Notifier][2]
* [CIBuddy installation instructions][5]
* [CIBuddy Binary Downloads][6]
* [CIBuddy Open Issues][7]

[1]: http://www.delcomproducts.com/products_usblmp.asp "Delcom Produkt Homepage"
[2]: http://www.i-buddy.com/ "i-Buddy MSN Notifier"
[3]: https://github.com/glueckkanja/LyncFellow/blob/gh-pages/img/ibuddy-fly.gif?raw=true 
    "Image provided by the https://github.com/glueckkanja/LyncFellow project, also doing cool stuff with the i-buddy"
[4]: http://www.delcomproducts.com/images/Beacon3C.jpg "Delcom GII Beacon Light as supported by CIBuddy"
[5]: https://github.com/cibuddy/cibuddy/tree/master/distributions/pax.assembly "CIBuddy installation instructions"
[6]: https://github.com/cibuddy/cibuddy/downloads "CIBuddy Binary Downloads"
[7]: https://github.com/cibuddy/cibuddy/issues "CIBuddy Open Issues"