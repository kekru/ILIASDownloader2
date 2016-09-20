package de.whiledo.iliasdownloader2.syncrunner.service;

import java.util.Arrays;
import java.util.LinkedList;

import lombok.val;
import de.whiledo.iliasdownloader2.exception.IliasException;
import de.whiledo.iliasdownloader2.service.IliasUtil;
import de.whiledo.iliasdownloader2.util.DownloadMethod;
import de.whiledo.iliasdownloader2.util.LoginType;

public class StartSyncRunner {
	public static final String CREATE_PROPERTIES = "createproperties";
	public static final String PASSWORD = "password";
	public static final String LOOP = "loop";
	private static final String FIND_ILIASCLIENT = "iliasclient";

	public static void main(String[] args) {

		val listArgs = new LinkedList<String>(Arrays.asList(args));

		if(listArgs.contains(PASSWORD)){
			
			boolean runIntervalSync = listArgs.contains(LOOP);

			int indexOfPassword = listArgs.indexOf(PASSWORD);
			if(indexOfPassword >= 0 && listArgs.size() > indexOfPassword +1){
				String password = listArgs.get(indexOfPassword +1);

				if(ServiceFunctions.getPropertiesFile().exists()){

					new ConsoleController(runIntervalSync, password);

				}else{
					System.out.println("No properties File found at: "+ServiceFunctions.getPropertiesFile().getAbsolutePath());
					System.out.println("Visit " + ServiceFunctions.ILIASDOWNLOADER_WEBSITE + " for more information");
					printHowToCreateProperties();
				}

			}else{

				printUsage();
			}
			
		}else if(listArgs.contains(CREATE_PROPERTIES)){

			ServiceFunctions.saveProperties(new IliasProperties());
			System.out.println("created empty properties file at " + ServiceFunctions.getPropertiesFile().getAbsolutePath());
			printHelp();

		}else if(listArgs.contains(FIND_ILIASCLIENT)){
			String iliasServerURL = ServiceFunctions.readProperties().getIliasServerURL();
			try{
				String s = IliasUtil.findClientByLoginPageOrWebserviceURL(iliasServerURL);
				System.out.println("your iliasclient is '"+s+"'");				
			}catch(IliasException e){
				System.out.println("the iliasclient id for '"+iliasServerURL+"' could not be found. Is the server available?");
				System.out.println("you can also open the HTML source code of your Ilias' loginpage and search for 'client_id'");
			}			

		}else{
			printUsage();
			printDivider();
			printHowToCreateProperties();
			printDivider();
			printHelp();
			
		}
	}

	private static void printDivider() {
		System.out.println("------------------");
		System.out.println();
	}

	private static void printHowToCreateProperties() {
		System.out.println("Run the following to create a properties file: java -jar <jarname>.jar "+CREATE_PROPERTIES);
	}

	private static void printUsage() {
		System.out.println("Usage: java -jar <jarname>.jar ["+LOOP+"] "+PASSWORD+" <myPassword>");
		System.out.println("'loop' is optional. Use it to run the sync in intervals as defined in the properties file");
	}
	
	private static void printHelp(){
		System.out.println("properties file settings:");
		System.out.println("open and edit the properties file "+ServiceFunctions.getPropertiesFile().getAbsolutePath());
		System.out.println("\tpossible logintypes are " + Arrays.asList(LoginType.values()));
		System.out.println("\tpossible downloadmethods are " + Arrays.asList(DownloadMethod.values()));
		System.out.println();
		System.out.println("\tto find out the 'iliasclient' open the properties file and fill in the 'server'");
		System.out.println("\te.g. when 'https://www.ilias.fh-dortmund.de/ilias/login.php' is your login page, the server is 'https://www.ilias.fh-dortmund.de/ilias/webservice/soap/server.php'");
		System.out.println("\tso just replace 'login.php' (and the following) with 'webservice/soap/server.php'");
		System.out.println("\tnow run java -jar <jarname>.jar "+FIND_ILIASCLIENT);
	}
}
