# IBM UrbanCode Deploy Rational ClearQuest Plugin [![Build Status](https://travis-ci.org/IBM-UrbanCode/Rational-ClearQuest-UCD.svg?branch=master)](https://travis-ci.org/IBM-UrbanCode/Rational-ClearQuest-UCD)
---
Note: This is not the plugin distributable! This is the source code. To find the installable plugin, go into the 'Releases' tab, and download a stable version.

### License
This plugin is protected under the [Eclipse Public 1.0 License](http://www.eclipse.org/legal/epl-v10.html)

### Overview

The Rational ClearQuest plug-in contains a single step to add Related Change Requests to records.

### Steps:

    Add Related Change Request


### Compatibility
	The IBM UrbanCode Deploy automation plug-in works with IBM Rational ClearQuest version 7.1.2.1 and OSLC 2.0.
	This plug-in requires version 6.1.1 or later of IBM UrbanCode Deploy.

### Installation
	The packaged zip is located in the dist folder. No special steps are required for installation.
	See Installing plug-ins in UrbanCode Deploy. Download this zip file if you wish to skip the
	manual build step. Otherwise, download the entire Rational-ClearQuest-UCD and
	run the "ant" command in the top level folder. This should compile the code and create
	a new distributable zip within the dist folder. Use this command if you wish to make
	your own changes to the plugin.

### History
    Version 1
        Initial Plug-in releate containing the "Add Related Change Request" step.
