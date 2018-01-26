@echo off
adb shell mkdir /sdcard/Android/data/com.rae.cnblogs/cache
adb push sdk\build\intermediates\bundles\debug\sdk.dex /sdcard/Android/data/com.rae.cnblogs/cache/sdk-hotfix.patch

