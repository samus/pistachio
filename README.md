# Introduction
Pistachio is a Kotlin 1.3.x multiplatform library inspired by the Javascript library Redux but built for the storage and memory needs of a mobile application.  Mobile ports of Redux store the entire application state in memory which is fast and efficient for apps that don't work with much of data.  However apps that consume large amounts of data, whether it be from a web api or elsewhere, quickly require solutions to offload data from the app state or face the wrath of the os's watchdog process.  State management like a pistachio is a tough nut to crack.

## About Pistachio
Pistachio uses the concept of Unidirectional Dataflow.  All modifications to the app's state is done by dispatching `Commands` into the `Store`.  The `Store` organizes a set of `Repository` objects that process incoming commands and perform operations their data.  Registered `StoreViews` are then notified of dataset changes and can update or rebuild as necessary for display in the application's ui.

### Examples
The examples in android-example and native-example-ios demonstrate the core functionality of Pistachio as simply as possible.  However they do not demonstrate the key advantage of using Kotlin multiplatform.  Utilizing Kotlin's multiplatform capabilities the `StoreViews`, `Repository` and `Command` objects can be written one time in common code. The objects are then able to be shared between iOS and Android apps thus requiring business and state management logic to be written only once.

### Repositories & Commands
Repositories make up the foundation of Pistachio.  They process `Commands` and make data modifications based on the command sent to the repository.  Individual commands are dispatched to all registered repositories to be processed as needed.  Some commands may apply to all repositories in an application while others may be specific to an individual repository.  Repositories are free to ignore non-applicable commands.  For instance an AddContactCommand would only be interesting to a Contacts repository and not a Settings repository.  However both may process a SignOutCommand by clearing all of their data.

Pistachio has three bundled Repository implementations suitable for getting started quickly.  The first is a simple in memory store providing similar benefits and drawbacks to a standard Redux implementation.  The second and third are iOS and Android specific stores that serialize the contents of an object to disk.  It is suggested that as the requirements of an application grow, these repositories be replaced with more fault tolerant application specific implementations of the `Repository` interface such as one backed by SQLite.

### StoreViews
StoreViews are the window an application uses to peer into its data.  StoreViews query repositories for the data needing to be represented on a screen.  After a command has dispatched to all repositories by the `Store`, any registered view is then notified of the changes.  The StoreViews then requery the repositories for the data needed and notify the ui that it has changed.  StoreViews are not ViewModels or Presenters nor are they meant to be a replacement for those patterns.  StoreViews focus only on retrieving data from the Store, not displaying it.

### Middleware
The Store's dispatch mechanism is able to be augmented through the use of middleware.  Middleware can be used to preprocess an incoming command before it is dispatched to the repositories.  Common use cases might be logging or providing an asynchronous command execution capability.

# Getting Started
Currently binaries are not published anywhere.  As the project matures this will change.  For now the best way is to pull the pistachio folder into an existing project as a module in a Kotlin Multiplatform Project.

## Using Directly in Swift or Objective-C
It is possible to include a Pistachio framework bundle directly into an iOS application but as demonstrated in the ios example, it can be awkward.  Kotlin/Native uses the Objective-C protocol to communicate with Objective-C and Swift code.  This means that generics behave as Objective-C generics and are type erased at runtime.  Collections of T are translated to Collections of Any when used from Swift.  Kotlin primitive data types are translated implicitly to Swift and Objective-C types but not in every case and can require code such as`KotlinBoolean(x == y)`  Another current limitation of Kotlin/Native is that only one Kotlin/Native framework can be included into an app at a time.  To utilize two or more Kotlin/Native libraries a single framework needs to be built from the libraries klib binary distribution.

To maximize the benefit of using Pistachio it is recommended to include it as part of an application's single Kotlin framework built using Kotlin's multiplatform capability.

## Hacking on the code

 * Make sure you have the Android SDK installed.
 * Open the project in IntelliJ IDEA (2017.3 EAP recommended).
 * Create a file `local.properties` in the root directory of the project, pointing to your Android SDK installation. On Mac OS,
the contents should be `sdk.dir=/Users/<your username>/Library/Android/sdk`. On other OSes, please adjust accordingly.
 * Run `./gradlew build`
