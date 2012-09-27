![10]

The CIBuddy project intends to provide a framework to better live monitor your
continuous integration environment. This is accomplished by several hard and software connectors providing you with quick visual feedback about what's going on in your build environment. [![Build Status](https://secure.travis-ci.org/cibuddy/cibuddy.png)](http://travis-ci.org/cibuddy/cibuddy)

## Hardware Light Integration

The CIBuddy application currently supports two eXtreme Feedback Devices (hardware lights). First of all, the well known [Delcom Lights][1] and secondly the [i-Buddy][2] MSN notification figures. Just plug-in your device(s) and wire each one with your distinct build configuration. You could use multiple lights and also mix and match different types of lights to indicate your build status independently for each project for instance. The cool thing is that an i-Buddy costs just about [8,50 Euro][8], which is an almost unbeatable price. Below is a picture from our deployment. You could see 3 indicators (a delcom light, a devel i-Buddy and the regular i-Buddy) indicating the build status of various build branches.

![9] 

## Jenkins-CI and Travis-CI Server Integration

The jenkins and travis subprojects provide capabilities to consume lists of
server url's and expose their configuration to the CIBuddy application. After exposure, these servers could be used for build checks.

## Getting Started

To get started, just grab the latest binary [here][6]. Unzip or un-tar the binary, change to the bin directory and start the application with `karaf` or `karaf.bat` (windows). The output should look like the following:

```
titan:bin mirkojahn$ ./karaf
        __ __                  ____      
       / //_/____ __________ _/ __/      
      / ,<  / __ `/ ___/ __ `/ /_        
     / /| |/ /_/ / /  / /_/ / __/        
    /_/ |_|\__,_/_/   \__,_/_/         

  Apache Karaf (2.2.9)

Hit '<tab>' for a list of available commands
and '[cmd] --help' for help on a specific command.
Hit '<ctrl-d>' or 'osgi:shutdown' to shutdown Karaf.

karaf@CIBuddy>
```
In case you don't have a hardware light, you could as well use the provided demo light (a simple swing application indicating the status as a colored circle). To install the demo light, just enter:
```
feature:install cibuddy-swing-xfd
```
If you have an i-Buddy or a Delcom Light, just connect it to your computer. You could check if this worked, simply enter the following line:
```
cib:list-efds
```
The output should look similar to the one below, however, depending on your connected devices, details might vary of course. The example sports 2 devices, one for a Delcom USB Light (Generation II) and an i-Buddy (with blue and green wings - Generation II).
```
karaf@CIBuddy> cib:list-efds 
eXtreme Feedback Device : [0] com.cibuddy.delcom.lights.impl:G2
eXtreme Feedback Device : [1] com.cibuddy.ibuddy.impl:iBuddyG2
```
Testing the connected devices is also pretty straigth forward. Again you could use the command line. This time use the following command to let CIBuddy go through each connected device and trigger the various states they might have to indicate:
```
karaf@CIBuddy> cib:test-efd 
Disclaimer: Not all devices support all actions!
Testing eXtreme Feedback Device: [0] com.cibuddy.delcom.lights.impl:G2
com.cibuddy.delcom.lights.impl.DelcomLightHandle@48b9e55c
Indicating SUCCESS
Indicating WARNING
Indicating BUILDING
Indicating FAILURE
Indicating OFF
Testing eXtreme Feedback Device: [0] com.cibuddy.ibuddy.impl:iBuddyG2
com.cibuddy.ibuddy.impl.IBuddyLightHandle@1970b890
Indicating SUCCESS
Indicating WARNING
Indicating BUILDING
Indicating FAILURE
Indicating OFF
```
Now, in order to access the status of a build, we have to introduce the CI environments to CIBuddy, by configuring the server addresses to check. This could be done by copying *.jenkins or *.travis files into the deploy folder or with an exiting feature (for known OSS Server) through the console:
```
feature:install cibuddy-oss-conf
```
Finally to wire everything together with a real configuration, you could either adapt one of the samples located in the `sample` folder and copy the file then into the `deploy` folder or you could start with the basic cibuddy test environment configuration prepackage as a feature by executing this:
```
feature:install cibuddy-test-conf
```
Shortly after the deployment your light should indicate the current build status. In order for this to work, you need internet access of course. 


>   **Please note:** features could easily be uninstalled by using the same command with `uninstall` instead of `install`.

## Known Issues

The project is still in alpha phase and very likely, there will be problems when trying to run the application. You could find all known issues in the [Issues Section][6] of this project. In case you encounter a new one, please do not hesitate to open an issue here as well.

## Current Build Status
[![Build Status](https://secure.travis-ci.org/cibuddy/cibuddy.png)](http://travis-ci.org/cibuddy/cibuddy) by Travis-CI - thanks for the support guys, you rock!

## Further Reading
The official website http://www.cibuddy.com will host more information about the project, it's progress, installation and trouble shooting guides as well as a FAQ. However, this is still work in progress. The website is also hosted in cibuddy and contributions on either project are always welcome.

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
[4]: https://github.com/glueckkanja/LyncFellow/blob/gh-pages/img/ibuddy-package.jpg?raw=true 
    "Image provided by the https://github.com/glueckkanja/LyncFellow project, also doing cool stuff with the i-buddy"
[5]: https://github.com/cibuddy/cibuddy/tree/master/distributions/karaf.assembly "CIBuddy installation instructions"
[6]: https://github.com/cibuddy/cibuddy/downloads "CIBuddy Binary Downloads"
[7]: https://github.com/cibuddy/cibuddy/issues "CIBuddy Open Issues"
[8]: http://www.idealo.de/preisvergleich/OffersOfProduct/1608018_-usb-messenger-i-buddy.html "Idealo Preisvergleich - i-buddy"
[9]: http://cibuddy.com/cibuddy/pics/cibuddy_groupOfIndicators_small.jpg "CIBuddies in Action - all good :-)"
[10]: https://raw.github.com/cibuddy/cibuddy.github.com/master/cibuddy_logo.png "CIBuddy Logo"