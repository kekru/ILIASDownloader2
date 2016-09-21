[![Build Status](https://travis-ci.org/kekru/ILIASDownloader2.svg?branch=master)](https://travis-ci.org/kekru/ILIASDownloader2)  
# ILIASDownloader2
Tool to download files from the ILIAS platform.  
The swing GUI has got german texts, but the code is in english.  

The project is divided into three parts.
+ IliasConnector  
  base classes to find and download files from an ILIAS server
+ SyncRunner (depends on IliasConnector)  
  simple command line app using the base classes
+ SwingFrontend (depends on SyncRunner)  
  Swing GUI

# Runnable App  
You find the runnable java app [here](http://whiledo.de/index.php?p=iliasdownloader2) (german).  
To use ILIASDownloader2 on commandline only, run `java -jar iliasdownloader.jar help` for more information.  
There is also an Android app [here](https://play.google.com/store/apps/details?id=wennierfiete.iliasdownloader) (german).  

# Create runnable App  
Be sure that you have installed Maven.  
Then run `mvn clean install -P build-runnable-swing` in the root directory of this project. You'll find the runnable Jar file in ILIASDownloader-SwingFrontend/target.  

If you only want the SyncRunner-module to be runnable, run `mvn clean install -P build-runnable-syncrunner`.  

# Getting started

Add the libraries to you Gradle/Maven dependencies or download manually from the [repo](https://whiledo.de/maven/repo/de/whiledo/iliasdownloader/).

## Gradle
```groovy
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    // required for 3rd party dependencies
    maven { url 'https://oss.sonatype.org/content/repositories/ksoap2-android-releases' }
}

dependencies {
    compile 'com.github.krekru:ILIASDownloader2:master-SNAPSHOT'
}
```

## Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    <!-- required for 3rd party dependencies -->
    <repository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/content/repositories/ksoap2-android-releases/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.krekru.ILIASDownloader2</groupId>
        <artifactId>sync-runner</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>
</dependencies>
```

First of all you could copy [ConsoleController.java](https://github.com/kekru/ILIASDownloader2/blob/ff8dc846110db888d8fd6e90ca2e7bb6925a39f1/ILIASDownloader-SyncRunner/src/main/java/de/whiledo/iliasdownloader2/syncrunner/service/ConsoleController.java) and replace the `System.out.println()` lines with your custom callback functions.

If you write a mobile app, you should disable the default download of files  
`iliasProperties.setAllowDownload(false);`  

Then you run `syncService.startOrStopSync();` to search for files and `fileLoadEnd(FileObject)` will be called for each file.  

To download a file, run `syncService.getFileSync().loadFileOrExerciseIgoreDownloadFileFlag(fileObject.getXmlObject(), DownloadMethod.WEBSERVICE);`.  
If you want to use `DownloadMethod.WEBDAV`, call the following first:

```java
if (!syncService.getIliasSoapService().isWebdavAuthenticationActive()) {
	syncService.getIliasSoapService().enableWebdavAuthentication("login", "pw");
}
``` 

# Help for configuration  
In [IliasProperties.java](https://github.com/kekru/ILIASDownloader2/blob/ff8dc846110db888d8fd6e90ca2e7bb6925a39f1/ILIASDownloader-SyncRunner/src/main/java/de/whiledo/iliasdownloader2/syncrunner/service/IliasProperties.java) you'll find `iliasServerURL` and `iliasClient`.  
You can find out `iliasClient` by calling `IliasUtil.findClientByLoginPageOrWebserviceURL(iliasServerURL)`.  

# Central classes  
[ILIASSoapService.java](https://github.com/kekru/ILIASDownloader2/blob/ff8dc846110db888d8fd6e90ca2e7bb6925a39f1/ILIASDownloader-IliasConnector/src/main/java/de/whiledo/iliasdownloader2/service/ILIASSoapService.java) communicates with the ILIAS Server. There you find the SOAP Webservice calls.  

[FileSync.java](https://github.com/kekru/ILIASDownloader2/blob/ff8dc846110db888d8fd6e90ca2e7bb6925a39f1/ILIASDownloader-IliasConnector/src/main/java/de/whiledo/iliasdownloader2/service/FileSync.java) collects the files on the ILIAS server and downloads them. You'll find both in

```xml
<dependency>
	<groupId>de.whiledo.iliasdownloader</groupId>
	<artifactId>ILIASDownloader-IliasConnector</artifactId>
	<version>0.0.1-RELEASE</version>
</dependency>
```  

# How to go on?  
Just look into [Maincontroller.java](https://github.com/kekru/ILIASDownloader2/blob/343d5cebbfd835c7fc2cd1c4efe1d14fca3f0fa4/ILIASDownloader-SwingFrontend/src/main/java/de/whiledo/iliasdownloader2/swing/service/MainController.java). This is the main class of the swing GUI and here you will find all functions that exists in the project.

```xml
<dependency>
	<groupId>de.whiledo.iliasdownloader</groupId>
	<artifactId>ILIASDownloader-SwingFrontend</artifactId>
	<version>0.0.12-RELEASE</version>
</dependency>
```  
