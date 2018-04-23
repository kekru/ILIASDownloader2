package de.whiledo.iliasdownloader2.swing.service;

import java.util.Arrays;

import de.whiledo.iliasdownloader2.syncrunner.service.StartSyncRunner;

public class StartGui {

	public static final String NO_UPDATER = "noupdate";

	public static void main(String[] args) throws Exception {

		boolean containsNoUpdater = Arrays.asList(args).contains(NO_UPDATER);

		if(!containsNoUpdater && args.length > 0){
			StartSyncRunner.main(args);

		}else{

			if(!containsNoUpdater){
				try{
//					update
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			new MainController();
		}

	}

}

