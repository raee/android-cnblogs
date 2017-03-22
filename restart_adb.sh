#! /bin/bash
echo stop server...
adb kill-server
echo start server...
adb start-server
echo list devices
adb devices
adb devices
echo 'DONE!'