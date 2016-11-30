@echo off
::g:\profile
set libPath=C:\Users\qq798
echo update lib cache, please wait....
echo delete dir %libPath%
rd /S /Q %libPath%\.gradle\caches\modules-2\metadata-2.16\descriptors\com.rae.core
rd /S /Q %libPath%\.gradle\caches\modules-2\metadata-2.16\descriptors\com.shipinhui
rd /S /Q %libPath%\.gradle\caches\modules-2\files-2.1\com.rae.core
rd /S /Q %libPath%\.gradle\caches\modules-2\files-2.1\com.shipinhui
echo success!
::gradlew