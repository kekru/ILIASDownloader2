package de.whiledo.iliasdownloader2.syncrunner.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.CodeSource;

import de.whiledo.iliasdownloader2.util.Functions;

public class ServiceFunctions {

	private static final String ILIASDOWNLOADER_CONFIG_FILE;
	public static final String ILIASDOWNLOADER_WEBSITE = "https://whiledo.de/?p=iliasdownloader2";
	public static final String ILIASDOWNLOADER_WEBSITE_LICENSE = "https://github.com/kekru/ILIASDownloader2/blob/master/LICENSE";
	public static final String ILIASDOWNLOADER_ANDROID_WEBSITE = "https://play.google.com/store/apps/details?id=wennierfiete.iliasdownloader";

	static {
		String s = "iliasdownloader.xml";
		try {
			CodeSource codeSource = ServiceFunctions.class.getProtectionDomain().getCodeSource();//Nullpointer Exception in Android
			File jarFile;
			jarFile = new File(codeSource.getLocation().toURI().getPath());
			s = jarFile.getParentFile().getPath() + File.separator + s;
		} catch (Exception e) {

		}

		ILIASDOWNLOADER_CONFIG_FILE = s;
	}

	public static IliasProperties readProperties() {
		File configFile = new File(ILIASDOWNLOADER_CONFIG_FILE);
		if(configFile.exists()){
			try {
				return Functions.parseXmlObject(new FileInputStream(configFile), IliasProperties.class);
			} catch (FileNotFoundException e) {
				return new IliasProperties();
			}
		}
		return new IliasProperties();
	}

	public static void saveProperties(IliasProperties iliasProperties){
		Functions.writeXmlObject(iliasProperties, new File(ILIASDOWNLOADER_CONFIG_FILE));
	}

	public static File getPropertiesFile(){
		return new File(ILIASDOWNLOADER_CONFIG_FILE);
	}
}
