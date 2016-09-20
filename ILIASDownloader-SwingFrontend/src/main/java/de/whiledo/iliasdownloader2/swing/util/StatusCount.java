package de.whiledo.iliasdownloader2.swing.util;

import java.util.ArrayList;
import java.util.List;

import de.whiledo.iliasdownloader2.util.FileObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class StatusCount {

	private List<FileObject> updatedFiles = new ArrayList<>();
	private List<FileObject> errorFiles = new ArrayList<>();
	private List<FileObject> ignoredFiles = new ArrayList<>();
	private List<FileObject> oldFiles = new ArrayList<>();
	
	/**
	 * Updated Files und die Updated von den letzten Syncs, bis einmal clearAll() aufgerufen wird
	 */
	@Getter
	private List<FileObject> updatedFilesAll = new ArrayList<>();
	
	public void addUpdated(FileObject fileObject){
		updatedFiles.add(fileObject);
		updatedFilesAll.add(fileObject);
	}
	
	public void addError(FileObject fileObject){
		errorFiles.add(fileObject);
	}
	
	public void addIgnored(FileObject fileObject){
		ignoredFiles.add(fileObject);
	}
	
	public void addOld(FileObject fileObject){
		oldFiles.add(fileObject);
	}
	
	public int updatedCount(){
		return updatedFiles.size();
	}
	
	public int errorCount(){
		return errorFiles.size();
	}
	
	public int ignoredCount(){
		return ignoredFiles.size();
	}
	
	public int oldCount(){
		return oldFiles.size();
	}
	
	public void clearTemp(){
		updatedFiles.clear();
		errorFiles.clear();
		ignoredFiles.clear();
		oldFiles.clear();
	}
	
	public void clearAll(){
		clearTemp();
		updatedFilesAll.clear();
	}

}
