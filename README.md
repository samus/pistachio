### Building the code

 * Make sure you have the Android SDK installed
 * Open the project in IntelliJ IDEA (2017.3 EAP recommended)
 * Create a file `local.properties` in the root directory of the project, pointing to your Android SDK installation. On Mac OS,
the contents should be `sdk.dir=/Users/<your username>/Library/Android/sdk`. On other OSes, please adjust accordingly.
 * Run `./gradlew build`