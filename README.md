# Introduction

The Problem: Phoenix Radios Android tablet style radios do not allow for running apps on boot. They also do not allow for running apps on fastboot. 

This app solves that problem. It allows you to launch apps on either boot or fastboot. It also can launch two apps in split-screen mode. Note that you may have to go into developer options and turn on split screen.
*  Note: need to note the actual setting.

# Installation Instructions

NOTE: If you install this app, it does not show up in the list of apps in the launcher that comes with the unit. **During the installation process, be sure to click the "Open" button to run it once. From that point on, it will run in the background**. If you don't start it during the installation process, the only way to start it is with another app that launches apps (like CarWebGuru).

1. Go to [Releases](https://github.com/tabletradio/altlauncher/releases), and expand Assets under the most recent release. 
1. Downioad altlauncher.apk to the Phoenix Radio. Either download it directiy on the radio, or download from elsewhere, and transfer the apk to the radio.
1. Using the File Manager, click on the apk, and follow the prompts to install the app. 
1. Be sure to click "Open" to open the app.

# Using the Launcher

When the Phoenix Radio boots (or fast-boots), it launches the apps it is configured to launch. The primary purpose is to launch two apps in split screen mode. You can select which app to be on the top half of the screen, and the app for the bottom half. Prior to starting those apps, it can launch apps that need to run in the background (like Tasker).  

To get into the app, swipe down from the top of the screen to view notifications, and click on the "Alt Launcher" notification. It has the message "Foreground Service Started". If the notification is not available, launch it using a non-stock launcher - CarWebGuru works.  

At the top of the screen is the list of apps that will start and then be put in the background. They are started in the order shown. To add a new one, click the "Add a new Background App" button, and select the desired app. To remove an app, click on it.

Below the background apps are the two apps to launch in split screen mode. To select the top or bottom app, click on either "None Selected" or the app, and choose the desired app in the popup.  

Clicking the "Launch" button launches the apps just like the system booted.  

Under the 3-dots menu in the top right, you can access the Settings screen.  

The "Seconds to delay after starting each background app" controls how long after launching a background app the altlauncher waits before moving on to the next action. The default length is 4 seconds, and can be changed as needed. Note that 1 is the smallest allowed setting.

If requested, you can click on the "Send logs to developer" button. That gathers logs, and starts your email client to allow you to send the email. Add details about your issue, and send the email. 
