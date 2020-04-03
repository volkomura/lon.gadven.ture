
#!/bin/bash
 
echo "Updating the system"
apt update
echo "Success"
 
echo "Installing unzip"
apt install unzip
echo "Success"
 
echo "Installing Java Development Kit";
apt install openjdk-8-jdk
echo "Success"
 
echo "Downloading Android SDK"
wget "https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip"
unzip ./sdk-tools-linux-4333796.zip -d ./sdk
echo "Success"
 
echo "Updating local.properties"
echo "sdk.dir=sdk" > ./local.properties
echo "Success"
 
FILENAME=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)".jks"
ALIAS=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)
 
echo "Generating sign. Filename: $FILENAME; alias: $ALIAS"
keytool -genkey -v -keystore "$FILENAME" -keyalg RSA -keysize 2048 -validity 10000 -alias "$ALIAS"
echo "success"
 
cp "./$FILENAME" ./app
 
echo "Updating gradle.properties"
echo -e "\nMYAPP_RELEASE_STORE_FILE=$FILENAME\nMYAPP_RELEASE_KEY_ALIAS=$ALIAS\nMYAPP_RELEASE_STORE_PASSWORD=password\nMYAPP_RELEASE_KEY_PASSWORD=password" >> ./gradle.properties
echo "Success"
 
./sdk/tools/bin/sdkmanager --licenses
 
chmod 777 ./gradlew
./gradlew assembleRelease
