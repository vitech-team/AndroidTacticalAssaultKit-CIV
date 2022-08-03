#!/bin/bash -x

git clean -f -d

echo STEP1
pushd .
cd scripts
bash ./prebuild.sh
popd
cd takthirdparty
rm -rf distfiles ; \
for i in builds/* ; do \
    pushd $i ; \
    (find * -type d -not -name "include" -not -path "include/*" -not -name "java" -not -name "lib" -not -path "lib/ogdi" -print0 | xargs -0 -I {} rm -rf {}) ; \
    (find . -maxdepth 1 -type f -print0 | xargs -0 -I {} rm -f {}) ; \
    (cd lib && (find . -maxdepth 1 -type f -not -name "*.so" -print0 | xargs -0 -I {} rm -f {})) ; \
    popd ; \
done


echo STEP2
cd /
wget --quiet --show-progress --tries=0 --output-document=cmake-3.14.7.tar.gz https://cmake.org/files/v3.14/cmake-3.14.7-Linux-x86_64.tar.gz;
gzip -d cmake-3.14.7.tar.gz;
tar xf cmake-3.14.7.tar;
rm -rf cmake-3.14.7.tar;




echo STEP4
rm -rf $HOME/secrets
mkdir $HOME/secrets
echo "$ANDROID_KEYSTORE_FILE" > $HOME/secrets/sdk_keystore.base64
base64 -d $HOME/secrets/sdk_keystore.base64 > $HOME/secrets/sdk_keystore
cd /ATAK/atak
rm -rf local.properties
echo "takDebugKeyFile=$HOME/secrets/sdk_keystore" >> local.properties
echo "takDebugKeyFilePassword=$ANDROID_KEYSTORE_STORE_PASSWORD" >> local.properties
echo "takDebugKeyAlias=$ANDROID_KEYSTORE_ALIAS" >> local.properties
echo "takDebugKeyPassword=$ANDROID_KEYSTORE_KEY_PASSWORD" >> local.properties
echo "takReleaseKeyFile=$HOME/secrets/sdk_keystore" >> local.properties
echo "takReleaseKeyFilePassword=$ANDROID_KEYSTORE_STORE_PASSWORD" >> local.properties
echo "takReleaseKeyAlias=$ANDROID_KEYSTORE_ALIAS" >> local.properties
echo "takReleaseKeyPassword=$ANDROID_KEYSTORE_KEY_PASSWORD" >> local.properties

echo STEP5
cd /ATAK/atak
echo "cmake.dir=$CMAKE_HOME" >> local.properties
rm -rf ATAK/app/build
bash ./gradlew assembleCivRelease



echo STEP6
cd /ATAK
rm -rf takthirdparty
rm -rf takengine
rm -rf pluginsdk/
unzip -qq pluginsdk.zip
cp ./atak/ATAK/app/build/outputs/apk/civ/sdk/*-sdk.apk ./pluginsdk/atak.apk
cp ./atak/ATAK/app/build/libs/main.jar ./pluginsdk
cp ./atak/ATAK/app/src/main/assets/support/license/LICENSE.txt ./pluginsdk
cp -r ./plugin-examples ./pluginsdk/
echo "ATAK CIV SDK `git rev-parse --short HEAD` (`git show -s --format=%ct`)" > pluginsdk/VERSION.txt
rm -rf javadoc
mkdir -p tmp/src
cp -R ./atak/ATAK/app/src/common/java/* tmp/src
cp -R ./atak/ATAK/app/src/main/java/* tmp/src
cp -R ./atak/ATAKMapEngine/lib/src/main/java/* tmp/src
pushd .
cd tmp/src && javadoc -linkoffline http://d.android.com/reference file://$ANDROID_HOME/docs/reference -d ../javadoc -classpath .:$ANDROID_HOME/platforms/android-${ANDROID_COMPILE_SDK}/android.jar -subpackages . &> /dev/null || echo "success by failure"
popd
pushd .
cd tmp/javadoc && jar cvf ../../pluginsdk/atak-javadoc.jar .
popd
pushd .
cd atak-gradle-takdev
chmod 755 gradlew
dos2unix gradlew
./gradlew jar
cp ./build/libs/atak-gradle-takdev-*.jar ../pluginsdk/atak-gradle-takdev.jar
popd
mv pluginsdk atak-civ
zip -qq -r atak-civ-sdk-${{ github.event.release.tag_name }}.zip atak-civ
rm -rf atak-civ