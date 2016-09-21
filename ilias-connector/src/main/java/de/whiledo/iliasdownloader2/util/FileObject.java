package de.whiledo.iliasdownloader2.util;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import de.whiledo.iliasdownloader2.xmlentities.exercise.XmlExerciseFile;
import de.whiledo.iliasdownloader2.xmlentities.filetree.XmlObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileObject {

	private long refId;
	private File targetFile;
	private long lastUpdated;
	private long fileSize;
	private XmlObject xmlObject;
	private XmlExerciseFile xmlExerciseFile;
	
	private SyncState syncState;
	private Throwable exception;
	
	public FileObject(long refId, File targetFile, long lastUpdated, long fileSize, XmlObject xmlObject, XmlExerciseFile xmlExerciseFile) {
		this.refId = refId;
		this.targetFile = targetFile;
		this.lastUpdated = lastUpdated;
		this.fileSize = fileSize;
		this.xmlObject = xmlObject;
		this.xmlExerciseFile = xmlExerciseFile;
	}
	
	@Override
	public boolean equals(Object o){//nicht auf Ref Id prüfen, diese muss nicht eindeutig sein, z.B. gibt es zu einer RefId mehrere ExerciseFiles und somit mehrere FileObject
		return o instanceof FileObject && getTargetFile().getAbsolutePath().equals(((FileObject) o).getTargetFile().getAbsolutePath());
	}
	
	@Override
	public int hashCode(){
		return targetFile.hashCode();
	}
	
}
