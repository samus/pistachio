#!/bin/sh
./gradlew :native:build
cd native/build/konan/bin
rm -rf Pistachio.framework
mkdir Pistachio.framework
lipo ./ios_arm64/Pistachio.framework/Pistachio ./ios_x64/Pistachio.framework/Pistachio -create -output Pistachio.framework/Pistachio
cp -r ./ios_arm64/Pistachio.framework/Headers ./Pistachio.framework
cp -r ./ios_arm64/Pistachio.framework/Modules ./Pistachio.framework
cp ./ios_arm64/Pistachio.framework/Info.plist ./Pistachio.framework

cd ../../../../