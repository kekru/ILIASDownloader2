# ILIASDownloader2
Tool to download files from the ILIAS platform.

# Runnable App  
You find the runnable java app [here](http://whiledo.de/index.php?p=iliasdownloader2) (german)

# Getting startet

Dependencies
<project>
	...
	<dependencies>
		<dependency>
			<groupId>de.whiledo</groupId>
			<artifactId>ILIASDownloader-SyncRunner</artifactId>
			<version>0.0.12-RELEASE</version>
		</dependency>
	</dependencies>

	<repositories>
		<repository>
			<id>whiledode-snapshots</id>
			<url>https://whiledo.de/maven/repo/</url>
			<snapshots><enabled>true</enabled></snapshots>
		</repository>
	</repositories>
</project>

Schnell starten
Am bestenzuerstden ConsoleController kopieren und die ganzen System.out.println() durch die richtigen Callbackfunktionen ersetzen.
Das Handy will man ja vermutlich nicht mit allen Dateien zumüllen, daher könntest du vorher den
Download abschalten:iliasProperties.setAllowDownload(false);Bei syncService..startOrStopSync(); geht’sdann los und für jede Datei wird einmal fileLoadEnd(FileObject) aufgerufen.
Um die Dateien einzeln herunterzuladen, geht dann das:syncService.getFileSync().loadFileOrExerciseIgoreDownloadFileFlag(fileObject.getXmlObject(), DownloadMethod.WEBSERVICE);
Oder mit DownloadMethod.WEBDAV, aber dann vorher noch:if(!syncService.getIliasSoapService().isWebdavAuthenticationActive()){syncService.getIliasSoapService().enableWebdavAuthentication(„login“,“pw“);}
Hilfe bei der Konfiguration:In den IliasProperties gibt es die iliasServerURLund den iliasClient. Der iliasClient lässt sich über IliasUtil.findClientByLoginPageOrWebserviceURL(iliasServerURL) ermittelnGrundlegende Klassen
Die Kommunikation mit ILIAS macht der ILIASSoapService.Hier sind die Webserviceaufrufe.Die Klasse FileSync ist für das Durchlaufen der Ilias-Dateien und das Herunterladen zuständig.Beide sind in

<dependency>
	<groupId>de.whiledo.iliasdownloader</groupId>
	<artifactId>ILIASDownloader-IliasConnector</artifactId>
	<version>0.0.1-RELEASE</version>
</dependency>

Und sonst so?Für alles Weitere, einfach mal MainController.javadurchwühlen, da werden alle Funktionen benutzt, die es so gibt

<dependency>
	<groupId>de.whiledo.iliasdownloader</groupId>
	<artifactId>ILIASDownloader-SwingFrontend</artifactId>
	<version>0.0.12-RELEASE</version>
</dependency>
