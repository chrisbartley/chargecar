# Introduction #
Here we detail how to install and use the testing suite.

# Details #

The software archive contains an Eclipse project.  Those using Eclipse with a Java IDE, or any other Java IDE, can import the project directly into the IDE, which will manage building.

In Eclipse:
  1. Goto File: Import
  1. Select General: Existing projects into workspace
  1. Select the archive file option, and select the software archive you downloaded from the downloads sections
  1. Hit finish
  1. In the Run dialog, create a new run configuration that points to the main function of the Simulator class
  1. In the arguments tab, enter in the software directory of GPX files you wish to test, followed by any policies you wish to test versus the baseline. Be sure not to include a trailing backslash in the directory path, as it will escape the " and break the argument input.

For example:
"C:/Users/astyler/Desktop/My Dropbox/work/ccpdata/testdata\_release\_090110/training" NaiveBufferPolicy SpeedPolicy


Once you got the examples running, try creating your own class in org.chargecar.policies, that extends Policy, make sure it runs, then begin modifying it and trying out your ideas.