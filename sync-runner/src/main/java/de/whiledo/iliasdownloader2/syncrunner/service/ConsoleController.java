package de.whiledo.iliasdownloader2.syncrunner.service;

import java.io.File;
import java.util.Collection;

import de.whiledo.iliasdownloader2.util.FileObject;
import de.whiledo.iliasdownloader2.util.ObjectDoInterfaceX;
import de.whiledo.iliasdownloader2.util.SyncProgressListener;

public class ConsoleController implements SyncProgressListener, IliasSyncListener {

	private IliasProperties iliasProperties = ServiceFunctions.readProperties();

//		public static void main(String[] args) {
//	
////			JPasswordField f = new JPasswordField();
////			if(JOptionPane.showConfirmDialog(null, f, "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
////				new ConsoleController(false, new String(f.getPassword()));
////			}
//			
//			new ConsoleController(false, XX.password);
//		}

	public ConsoleController(boolean runIntervalSync, final String password){

		SyncService s = new SyncService(this, this, new ObjectDoInterfaceX<Void, IliasProperties>() {

			@Override
			public IliasProperties doSomething(Void object) {
				return iliasProperties;
			}
		}, 
		new ObjectDoInterfaceX<Void, String>() {

			@Override
			public String doSomething(Void object) {
				return password;
			}
		},
		new ObjectDoInterfaceX<Throwable, Void>() {

			@Override
			public Void doSomething(Throwable e) {
				e.printStackTrace();
				return null;
			}
		});

//				Functions.useJAX = false;
//				s.login();
//				for(val id : s.getIliasSoapService().getCourseIds()){
//					System.out.println(s.getIliasSoapService().getCourseObjects(id));
//				}
//				System.out.println(s.getIliasSoapService().getCourseObjects(74865));
//				System.out.println(s.getIliasSoapService().getCourseObjects(338991));
//				System.out.println(s.getIliasSoapService().getCourseObjects(23561));
//				System.out.println(s.getIliasSoapService().getCourseObjects(316472));

		if(runIntervalSync){
			s.startIntervalSync(0);
		}else{
			s.startOrStopSync();
		}
	}

	@Override
	public void syncStarted() {
		System.out.println("Sync started to base directory " + new File(iliasProperties.getBaseDirectory()).getAbsolutePath());
	}

	@Override
	public void syncFinished() {
		System.out.println("Sync Finished");		
	}

	@Override
	public void syncStopped() {
		System.out.println("Sync Stopped");
	}

	@Override
	public void syncCoursesFound(Collection<Long> activeCourseIds, Collection<Long> allCourseIds) {
		System.out.println("Sync " + activeCourseIds.size() + " of " + allCourseIds.size() + " courses. Courses are: " + activeCourseIds);
	}

	@Override
	public void progress(int percent) {

	}

	@Override
	public void fileLoadStart(FileObject fileObject) {

	}

	@Override
	public void fileLoadEnd(FileObject fileObject) {
		System.out.println(fileObject.getSyncState().name() + " id: " + fileObject.getRefId() + " file: " + fileObject.getTargetFile().getAbsolutePath());		
	}

}
