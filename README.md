# CIBuddy ![Build Status](https://secure.travis-ci.org/cibuddy/cibuddy.png)

The CIBuddy project intends to provide a framework to better live monitor your
continuous environment. This is accomplished by several hard and software connectors providing you with quick visual feedback about what's going on in your build environment.

## Hardware Light Integration

The CIBuddy application currently supports two hardware lights. First of all, the well known [Delcom Lights][1] and secondly the [i-Buddy][2] MSN notification figures. Just plug-in your lights and wire each one with your distinct build configuration. You could use multiple lights and also mix and match different types of lights to indicate your build status independently for each project for instance. The cool thing is that an i-Buddy only costs about [15 Euro][8], which is a very cool price for the fun. Below is a picture from our deployment. You could see 3 indicators (a delcom light, a devel i-Buddy and the regular i-Buddy) indicating the build status of various build branches.

![9] 

## Jenkins Server Integration

The jenkins.status subproject provides capabilities to consume a list of Jenkins
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
cibuddy:test
```
If you have an i-Buddy or a Delcom Light, just connect it to your computer.

To quickly test if your setup is working, copy the two files located in your `sample` folder into the `deploy` folder. After less than a minute, your light should indicate the current build status. In order for this to work, you need internet access of course. 

## Known Issues

As mentioned before, this is the early alpha phase of the project and very likely, there will be problems when trying to run the binary. You could find all known issues in the [Issues Section][6] of this project. In case you encounter a new one, please do not hesitate to open an issue here as well.

## Travis-CI integration
[![Build Status](https://secure.travis-ci.org/cibuddy/cibuddy.png)](http://travis-ci.org/cibuddy/cibuddy)


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