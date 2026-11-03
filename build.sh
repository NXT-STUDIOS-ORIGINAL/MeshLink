#!/bin/bash
set -e
cd /home/user
chmod +x gradlew
./gradlew assembleDebug --no-daemon --stacktrace 2>&1