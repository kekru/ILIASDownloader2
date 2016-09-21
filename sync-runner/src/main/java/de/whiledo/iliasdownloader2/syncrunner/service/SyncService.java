package de.whiledo.iliasdownloader2.syncrunner.service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Base64InputStream;

import lombok.Getter;
import lombok.Setter;
import de.whiledo.iliasdownloader2.exception.IliasAuthenticationException;
import de.whiledo.iliasdownloader2.service.FileSync;
import de.whiledo.iliasdownloader2.service.ILIASSoapService;
import de.whiledo.iliasdownloader2.util.DownloadMethod;
import de.whiledo.iliasdownloader2.util.ObjectDoInterfaceX;
import de.whiledo.iliasdownloader2.util.SyncProgressListener;

public class SyncService {

	@Getter
	private boolean running = false;

	@Getter
	private FileSync fileSync;

	private IliasSyncListener iliasSyncListener;	
	private SyncProgressListener syncProgressListener;

	@Getter
	private ILIASSoapService iliasSoapService;

	private ObjectDoInterfaceX<Void, IliasProperties> getIliasPropertiesCallback;
	private ObjectDoInterfaceX<Void, String> getPasswordCallback;
	private ObjectDoInterfaceX<Throwable, Void> asyncErrorHandler;

	private Thread autoSyncThread;
	
	@Getter
	@Setter
	private ObjectDoInterfaceX<InputStream, InputStream> base64InputStreamFactory = new ObjectDoInterfaceX<InputStream, InputStream>() {

		@Override
		public InputStream doSomething(InputStream inputStream) {
			return new Base64InputStream(inputStream);
		}
	};

	public SyncService(IliasSyncListener iliasSyncListener, SyncProgressListener syncProgressListener, ObjectDoInterfaceX<Void, IliasProperties> getIliasPropertiesCallback, ObjectDoInterfaceX<Void, String> getPasswordCallback, ObjectDoInterfaceX<Throwable, Void> asyncErrorHandler){
		this.iliasSyncListener = iliasSyncListener;
		this.syncProgressListener = syncProgressListener;
		this.getIliasPropertiesCallback = getIliasPropertiesCallback;
		this.getPasswordCallback = getPasswordCallback;
		this.asyncErrorHandler = asyncErrorHandler;
	}

	public void startIntervalSync(final int startDelayInMilliseconds){

		if(autoSyncThread != null){
			autoSyncThread.interrupt();
		}

		autoSyncThread = new Thread(){

			@Override
			public void run() {

				if(startDelayInMilliseconds > 0){
					try {
						Thread.sleep(startDelayInMilliseconds);
					} catch (InterruptedException e) {
						interrupt();//Exception loescht den Interrupted-Status des Thread, darum nochmal interrupten
					}
				}

				while(!isInterrupted()){

					if(!running){
						try{
							startOrStopSync();
						}catch(Exception e){
							asyncErrorHandler.doSomething(e);
						}
					}

					try {
						Thread.sleep(getIliasPropertiesCallback.doSomething(null).getAutoSyncIntervalInSeconds() * 1000);
					} catch (InterruptedException e) {
						interrupt();//Exception loescht den Interrupted-Status des Thread, darum nochmal interrupten
					}
				}
				
			}
		};

		autoSyncThread.start();
	}

	public void stopIntervalSync(){
		if(autoSyncThread != null){
			autoSyncThread.interrupt();
			autoSyncThread = null;
		}
	}

	public void startOrStopSync(){

		if(running){
			if(fileSync != null){
				fileSync.stop();
				if(iliasSyncListener != null){
					iliasSyncListener.syncStopped();
				}
			}

		}else{
			login(); //wirft Exception, darum ausserhalb des Thread, damit sie ausserhalb gefangen werden kann
			
			if(iliasSyncListener != null){
				iliasSyncListener.syncStarted();
			}

			new Thread(new Runnable() {

				@Override
				public void run() {
					try{
						if(iliasSyncListener != null){					
							Set<Long> set = new HashSet<Long>(iliasSoapService.getCourseIds());
							IliasProperties iliasProperties = getIliasPropertiesCallback.doSomething(null);
							Set<Long> activeCourses = new HashSet<Long>(iliasProperties.getActiveCourses());
							set.addAll(activeCourses);
							iliasSyncListener.syncCoursesFound(iliasProperties.isSyncAll() ? set : activeCourses, set);
						}
						fileSync.syncFiles();

						logoutIfNotRunning();

					}catch(Exception e){
						asyncErrorHandler.doSomething(e);
					}finally{
						running = false;
						
						if(iliasSyncListener != null){
							iliasSyncListener.syncFinished();
						}
					}
				}

			}).start();


		}

		running = !running;
	}

	public void logoutIfLoggedIn() {
		if(iliasSoapService != null && iliasSoapService.isLoggedIn()){
			logout();
		}
	}

	/**
	 * @throws IliasAuthenticationException
	 */
	public void login() {
		IliasProperties iliasProperties = getIliasPropertiesCallback.doSomething(null);

		if(iliasSoapService == null || !iliasSoapService.isLoggedIn()){


			iliasSoapService = new ILIASSoapService(iliasProperties.getIliasServerURL(), iliasProperties.getIliasClient());
			iliasSoapService.login(iliasProperties.getUserName(), getPasswordCallback.doSomething(null), iliasProperties.getLoginType(), iliasProperties.getDownloadMethod().equals(DownloadMethod.WEBDAV));
		}


		fileSync = new FileSync(iliasSoapService, iliasProperties.getBaseDirectory());
		fileSync.setSyncProgressListener(syncProgressListener);
		fileSync.setDownloadFiles(iliasProperties.isAllowDownload());
		fileSync.setMaxSize(iliasProperties.getMaxFileSize());
		fileSync.addToIgnore(iliasProperties.getBlockedFiles());
		fileSync.setCoursesToDownload(iliasProperties.getActiveCourses());
		fileSync.setSyncAll(iliasProperties.isSyncAll());
		fileSync.setUseThreads(iliasProperties.isUseThreads());
		fileSync.setDownloadMethod(iliasProperties.getDownloadMethod());
		fileSync.setBase64InputStreamFactory(base64InputStreamFactory);

	}

	private void logout(){
		if(iliasSoapService != null){
			iliasSoapService.logout();
		}
	}



	public void logoutIfNotRunning() {
		if(!running){
			logout();
		}
	}
}
