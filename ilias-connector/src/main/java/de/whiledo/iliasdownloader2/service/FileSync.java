package de.whiledo.iliasdownloader2.service;

import de.whiledo.iliasdownloader2.exception.IliasException;
import de.whiledo.iliasdownloader2.util.*;
import de.whiledo.iliasdownloader2.xmlentities.exercise.XmlExercise;
import de.whiledo.iliasdownloader2.xmlentities.filetree.XmlObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class FileSync {

	@Setter
	private String baseDirectory;

	@Setter
	private ILIASSoapService iliasSoapService;

	@Getter
	@Setter
	private boolean useThreads = true;

	@Setter
	private SyncProgressListener syncProgressListener;

	@Setter
	private long maxSize = Long.MAX_VALUE;

	private Map<String, Integer> foundFiles;
	private Set<Long> ignoreFiles;

	@Getter
	@Setter
	private boolean syncAll = false;

	private int fileCount = 0;
	private int checkedFiles = 0;

	private boolean stopped;

	@Getter
	@Setter
	private DownloadMethod downloadMethod = DownloadMethod.WEBSERVICE;

	/**
	 * RefIds of Courses to download from
	 */
	@Getter
	@Setter
	private Collection<Long> coursesToDownload = new LinkedList<Long>();

	@Getter
	@Setter
	private boolean downloadFiles = true;
	
	@Getter
	@Setter
	private ObjectDoInterfaceX<InputStream, InputStream> base64InputStreamFactory = new ObjectDoInterfaceX<InputStream, InputStream>() {

		@Override
		public InputStream doSomething(InputStream inputStream) {
			return new Base64InputStream(inputStream);
		}
	};

	public FileSync(ILIASSoapService iliasSoapService, String baseDirectory, SyncProgressListener syncProgressListener){
		this(iliasSoapService, baseDirectory);
		this.syncProgressListener = syncProgressListener;
	}

	public FileSync(ILIASSoapService iliasSoapService, String baseDirectory){
		this.iliasSoapService = iliasSoapService;
		this.baseDirectory = baseDirectory;
		foundFiles = new HashMap<String, Integer>();
		ignoreFiles = new HashSet<Long>();
	}

	public void addToIgnore(long refId){
		ignoreFiles.add(refId);
	}

	public void addToIgnore(Collection<Long> refIds){
		ignoreFiles.addAll(refIds);
	}

	public void stop(){
		stopped = true;
	}


	public void syncFiles(){

		stopped = false;

		fileCount = 0;
		checkedFiles = 0;

		if(!iliasSoapService.isLoggedIn()){
			throw new IliasException("You need to login first via iliasSoapService.login(...)");
		}

		Set<Long> allCourseIds = new HashSet<Long>(iliasSoapService.getCourseIds());
		allCourseIds.addAll(coursesToDownload);//hier k�nnen auch ids drin sein, die iliasSoapService.getCourseIds() nicht liefert

		Collection<Long> courseIds = syncAll ? allCourseIds : coursesToDownload;
		//		useThreads = false;
		if(!courseIds.isEmpty()){
			//(Math.min(courseIds.size(), 5))
			ExecutorService taskExecutor = useThreads ? Executors.newCachedThreadPool() : null;//Runtime.getRuntime().availableProcessors()

			//			List<Thread> threads = new LinkedList<Thread>();
			for(final val courseId : courseIds){

				if(stopped){
					return;
				}

				if(useThreads){

					taskExecutor.execute(new Runnable() {

						@Override
						public void run() {
							superSync(courseId);
						}
					});

					//					Thread t = new Thread(new Runnable() {
					//
					//						@Override
					//						public void run() {
					//							superSync(courseId);
					//						}
					//					});
					//					t.start();
					//					threads.add(t);

				}else{
					superSync(courseId);
				}
			}

			//			for(val t : threads){
			//				try {
			//					t.join();
			//				} catch (InterruptedException e) {
			//					e.printStackTrace();
			//				}
			//			}

			if(useThreads){
				taskExecutor.shutdown();
				try {
					taskExecutor.awaitTermination(10, TimeUnit.DAYS);
				} catch (InterruptedException e) {
					System.err.println("Thread interrupted");
				}
			}
		}
	}

	private File updateFileName(File file){
		synchronized (foundFiles) {
			String filename = file.getAbsolutePath();
			Integer count = foundFiles.get(filename);
			if(count == null){
				count = 0;
			}
			count++;

			foundFiles.put(filename, count);

			if(count == 1){
				return new File(filename);
			}

			int lastIndexOf = filename.lastIndexOf(".");
			String suffix = lastIndexOf == -1 ? "" : filename.substring(lastIndexOf);

			String pathWithoutSuffix = filename;

			if(filename.lastIndexOf(File.separator) < lastIndexOf){
				//Die Datei hat einen Datei-suffix
				pathWithoutSuffix = filename.substring(0, lastIndexOf);
			}

			return new File(pathWithoutSuffix + "-" + count + suffix);
		}
	}

	private void superSync(final long courseId){
		val courseObjects = iliasSoapService.getCourseObjects(courseId);

		if(courseObjects != null){
			fileCount += courseObjects.size();

			for(val f : courseObjects){

				if(stopped){
					return;
				}

				if(f != null){

					if(downloadFiles && (f.isFolder() || f.isCourse())){
						try{
							createFolder(f, true).setLastModified(f.getUpdatedDate().getTime());							
						}catch(java.lang.IllegalArgumentException e){
							//manchmal ist die Zeit nicht g�ltig, weil z.B. negativ
						}
					}

					loadFileOrExercise(f);					

				}
				checkedFiles++;
				if(syncProgressListener != null){
					syncProgressListener.progress((int) ((((double)checkedFiles/(double)fileCount) * 100d)));
				}
			}
		}
	}

	private void loadFileOrExercise(XmlObject f){
		loadFileOrExercise(f, downloadFiles, downloadMethod);
	}

	public void loadFileOrExerciseIgoreDownloadFileFlag(XmlObject f, DownloadMethod downloadMethod){
		loadFileOrExercise(f, true, downloadMethod);
	}

	private void loadFileOrExercise(XmlObject f, boolean downloadAllowed, DownloadMethod downloadMethod){
		if(f.isFile()){
			loadFile(f, downloadAllowed, downloadMethod);
		}

		if(f.isExercise()){
			loadExercise(f, downloadAllowed, downloadMethod);
		}
	}


	private void loadExercise(XmlObject f, boolean downloadAllowed, DownloadMethod downloadMethod) {
		XmlExercise exercise = iliasSoapService.getExercise(f.getRefIdOne());
		File folder = createFolder(f, downloadAllowed);
		folder.setLastModified(f.getUpdatedDate().getTime());
		
		if(exercise.getExerciseFiles() != null){
			for(val exerciseFile : exercise.getExerciseFiles()){

				File targetFile = updateFileName(new File(folder.getAbsolutePath() + "/" + exerciseFile.getFileNameClean()));

				final String base64Content = exerciseFile.getContent();
				saveBase64StringToFile(new FileObject(f.getRefIdOne(), targetFile, f.getUpdatedDate().getTime(), exerciseFile.getFileSize(), f, exerciseFile), downloadAllowed, downloadMethod, new ObjectDoInterfaceX<Void, String>() {

					public String doSomething(Void object) {
						return base64Content;
					}
				});
			}
		}
	}



	private void loadFile(XmlObject f, boolean downloadAllowed, DownloadMethod downloadMethod) {
		File file = createFolder(f, downloadAllowed);
		file = updateFileName(new File(file.getAbsolutePath() + "/" + f.getFileNameClean()));

		final long refId = f.getRefIdOne();
		saveBase64StringToFile(new FileObject(f.getRefIdOne(), file, f.getUpdatedDate().getTime(), f.getFileSize(), f, null), downloadAllowed, downloadMethod, new ObjectDoInterfaceX<Void, String>() {

			public String doSomething(Void object) {
				return iliasSoapService.getFile(refId).getContent();//in diese Callback Methode ausgelagert, wird nur aufgerufen, wenn die Datei nicht schon lokal existiert
			}
		});

	}

	public File loadFileOrExerciseTemp(FileObject f){

		boolean useWebdav = downloadMethod.equals(DownloadMethod.WEBDAV);

		String content = null;

		if(!useWebdav){
			if(f.getXmlObject().isFile()){
				content = iliasSoapService.getFile(f.getRefId()).getContent();

			}else if(f.getXmlObject().isExercise()){
				content = f.getXmlExerciseFile().getContent();

			}else{
				throw new IliasException("Unsupported type: "+f.getXmlObject().getType() + " in File " + f.getTargetFile().getAbsolutePath());
			}
		}

		File result = Functions.createTempFileInTempFolder(f.getTargetFile().getAbsolutePath());
		try {
			OutputStream out = new FileOutputStream(result);
			InputStream input;
			if(useWebdav){
				input = iliasSoapService.getWebdavFileStream(f.getRefId());
			}else{
				input = base64InputStreamFactory.doSomething((new ByteArrayInputStream(content.getBytes())));
			}

			IOUtils.copy(input, out);
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
			result.deleteOnExit();

			return result;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void saveBase64StringToFile(FileObject fileObject, boolean downloadAllowed, DownloadMethod downloadMethod, ObjectDoInterfaceX<Void, String> getBase64Data) {
		File targetFile = fileObject.getTargetFile();

		//Existiert die Datei schon?
		if(!targetFile.exists() || targetFile.length() != fileObject.getFileSize()){

			//Soll die Datei nicht heruntergeladen werden?
			if(ignoreFiles.contains(fileObject.getRefId())){
				fileObject.setSyncState(SyncState.IGNORED_IGNORE_LIST);

			}else if(fileObject.getFileSize() > maxSize){
				fileObject.setSyncState(SyncState.IGNORED_GREATER_MAX_SIZE);

			}else if(!downloadAllowed){
				fileObject.setSyncState(SyncState.IGNORED_NO_DOWNLOAD_ALLOWED);

			}else {	

				fileObject.setSyncState(SyncState.LOADING);
				if(syncProgressListener != null){
					syncProgressListener.fileLoadStart(fileObject);
				}

				try {

					FileOutputStream outputStream = new FileOutputStream(targetFile);
					InputStream input;

					if(downloadMethod.equals(DownloadMethod.WEBDAV)){
						input = iliasSoapService.getWebdavFileStream(fileObject.getRefId());
					}else{
						input = base64InputStreamFactory.doSomething(new ByteArrayInputStream(getBase64Data.doSomething(null).getBytes()));
					}

					IOUtils.copy(input, outputStream);
					//					outputStream.close(); //Wichtig, sonst geht setLastModified nicht
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(outputStream);
					targetFile.setLastModified(fileObject.getLastUpdated());

					if(new File(targetFile.getAbsolutePath()).length() == fileObject.getFileSize()){
						fileObject.setSyncState(SyncState.UPDATED);
					}else{
						fileObject.setSyncState(SyncState.CORRUPT);
					}

				}catch (Exception e) {
					fileObject.setSyncState(SyncState.ERROR);
					fileObject.setException(new IliasException(e));

				}

			}
		}else{
			fileObject.setSyncState(SyncState.ALREADY_UP_TO_DATE);
		}

		if(syncProgressListener != null){
			syncProgressListener.fileLoadEnd(fileObject);
		}
	}


	private File createFolder(XmlObject f, boolean makeDirectories) {
		File file = new File(baseDirectory + "/" + f.getPath());
		if(makeDirectories && !file.exists()){
			file.mkdirs();
		}
		return file;
	}

	//	private void syncCourse(final ILIASSoapService iliasSoapService, final long courseId) {
	//		String courseName = cleanFileName(iliasSoapService.getCourse(courseId).getMetaData().getGeneral().getTitle());
	//		syncFilesRecursive(courseId, baseDirectory + "/" + courseName);
	//	}
	//
	//
	//
	//	private void syncFilesRecursive(long refId, String directory) {
	//		val folders = iliasSoapService.getFolderTree(refId, userId);
	//		if(folders != null){
	//			for(val f : folders){
	//				if(f != null){
	//					val dir = new File(directory);
	//					if(!dir.exists()){
	//						dir.mkdirs();
	//					}
	//
	//					if(f.isFolder()){
	//						syncFilesRecursive(f.getRefIdOne(), directory + "/" + cleanFileName(f.getTitle()));
	//
	//					}else if(f.isFile()){
	//						val file = new File(directory + "/" + cleanFileName(f.getTitle()));
	//						if(!file.exists() || file.length() != f.getFileSize()){
	//							System.out.println("load: " + file.getAbsolutePath());
	//							try {
	//								IOUtils.copy(iliasSoapService.getFileStream(f.getRefIdOne()), new FileOutputStream(file));
	//								file.setLastModified(f.getUpdatedDate().getTime());
	//							} catch (FileNotFoundException e) {
	//								e.printStackTrace();
	//							} catch (IOException e) {
	//								System.err.println("Error loading: " + file);
	//								e.printStackTrace();
	//							}
	//
	//							if(new File(file.getAbsolutePath()).length() != f.getFileSize()){
	//								System.err.println("not loaded correctly: " + file.getAbsolutePath());
	//							}
	//						}else{
	//							System.out.println("already up to date: "+file.getAbsolutePath());
	//						}
	//
	//					}
	//				}
	//			}
	//		}
	//	}
}
