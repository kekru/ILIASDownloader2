package de.whiledo.iliasdownloader2.util;


public interface SyncProgressListener {

	void progress(int percent);
	
	void fileLoadStart(FileObject fileObject);
	void fileLoadEnd(FileObject fileObject);
	
}
