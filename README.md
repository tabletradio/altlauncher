# Introduction

The Problem: Phoenix Radios Android tablet style radios do not allow for running apps on boot or fastboot.

This app solves that problem. It allows you to launch apps and services on either boot or fastboot. It also launches apps in split-screen mode.

# Installation

***NOTE:*** *The Alt Launcher does not show up in the launcher that comes with the Phoenix radios. **During the installation process, be sure to click the "Open" button to run it once. From that point on, it will run in the background**. If you don't start it during the installation process, the only way to start it is with another app that launches apps (like CarWebGuru).*  

## Recommended Instructions
1. Install from the [F-Droid](https://f-droid.org/en/packages/com.szchoiceway.aios.bridge/) App store.
1. After installation completes, launch the app by:
    1. Within F-Droid, click on Settings
    1. Then click "Manage installed apps"
    1. Then click on "Alt Launcher"
    1. Then click the "Open" button.

## Alternate Instructions
1. Go to [Releases](https://github.com/tabletradio/altlauncher/releases), and expand Assets under the most recent release. 
1. Downioad altlauncher.apk to the Phoenix Radio. Either download it directiy on the radio, or download from elsewhere, and transfer the apk to the radio.
1. Using the File Manager, click on the apk, and follow the prompts to install the app. 
1. **Be sure to click `Open` to open the app.**

# Using the Launcher

## Purpose
When the Phoenix Radio boots (or fast-boots), the Alt Launcher starts the apps it is configured to start. The primary purpose is to launch two apps in split screen mode. You can select which app to be on the top half of the screen, and the app for the bottom half. Prior to starting those apps, it can launch services or apps that need to run in the background.  

## Accessing the app
To get into the Alt Launcher, swipe down from the top of the screen to view notifications, and click on the "Alt Launcher" notification. It has the message "Foreground Service Started". If the notification is not available, start the Alt Launcher using a non-stock launcher like CarWebGuru.  

In the main scren, the Alt Launcher displays the services, background apps, and top and bottom apps it is configured to start on booot or fast boot.

## Background Apps
At the top of the main screen is the list of apps that will start and then be put in the background. They are started in the order shown. To add a new one, click the "Add a new Background App" button, and select the desired app. To remove an app from the list, click on that app.

## Services
Below the background apps is the list of services that will be started. They are started in the order shown. To add a new one, click the "Add New Service to Start" button, and select the desired service. To remove a service, click on the service.

## Top and Bottom Apps
Below the background apps are the two apps to launch in split screen mode. To select the top or bottom app, click on either "None Selected" or the app shown in either the top or bottom sections, and choose the desired app in the popup.  

## Testing
Clicking the "Launch" button starts the apps just like it does when the system boots.  

## Settings
Under the 3-dots menu in the top right, you can access the Settings screen.  

The "Seconds to delay after starting each background app" controls how long after launching a background app the Alt Launcher waits before moving on to the next action. The default length is 4 seconds, and can be changed as needed. Note that 1 is the smallest allowed setting.

The "Seconds to delay after starting each service" controls how long after launching a service the Alt Launcher waits before moving on to the next action.

The "Seconds to delay after fast boot..." setting controls how long the Alt Launcher waits after being notified that fast boot has completed prior to starting apps.

The "Seconds to delay after normal boot..." setting controls how long the Alt Launcher waits after being notified that boot has completed prior to starting apps.

Note that the Alt Launcher does not get notified immediately after the normal boot completes, it may be 30-60 seconds later.

If requested, you can click on the "Send logs to developer" button. That gathers logs, and starts your email client to allow you to send the email. Add details about your issue, and send the email. 

## Permissions
The app requires the following permissions:
1. RECEIVE_BOOT_COMPLETED - needed to be notified after cold boot so it can launch the apps.
1. FOREGROUND_SERVICE - The default launcher on Phoenix Radios hides the application icon for the Alt Launcher. This means that by default, there is no way to get into the app to update configuration. The foreground service provides a consistent way to get back into the app.

## Tasker integration
To start Tasker so that it starts performing actions after boot, do the following:
1. Select the following Tasker service to be launched: `com.joaomgcd.taskerm.plugin.ServiceRequestQuery`
   1. There is generally no need to launch the Tasker app itself.
1. In Tasker, if you set up a profile with `Profile->Event->Tasker->Monitor Start`, that activates when Tasker starts up.
