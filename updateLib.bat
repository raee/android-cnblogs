@echo off
::g:\profile
set libPath=C:\Users\qq798
echo update lib cache, please wait....
echo delete dir %libPath%
rd /S /Q %libPath%\.gradle\caches\modules-2\metadata-2.16\descriptors\com.rae.core
rd /S /Q %libPath%\.gradle\caches\modules-2\files-2.1\com.rae.core
rd /S /Q %libPath%\.gradle\caches\modules-2\metadata-2.16\descriptors\com.github.raee
rd /S /Q %libPath%\.gradle\caches\modules-2\files-2.1\com.github.raee
rd /S /Q %libPath%\.gradle\caches\modules-2\metadata-2.16\descriptors\com.rae.swift
rd /S /Q %libPath%\.gradle\caches\modules-2\files-2.1\com.rae.swift
rd /S /Q %libPath%\.gradle\caches\modules-2\metadata-2.16\descriptors\com.rae.widget
rd /S /Q %libPath%\.gradle\caches\modules-2\files-2.1\com.rae.widget
rd /S /Q %libPath%\.gradle\caches\modules-2\metadata-2.16\descriptors\com.squareup.okhttp3
rd /S /Q %libPath%\.gradle\caches\modules-2\files-2.1\com.squareup.okhttp3
echo success!
::gradlew