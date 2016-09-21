package de.whiledo.iliasdownloader2.xmlentities.exercise;

import static de.whiledo.iliasdownloader2.util.Functions.cleanFileName;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="File", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlExerciseFile {

	@Attribute(name="size", required=false)
//	@XmlAttribute(name="size")
	private long fileSize;
	
	@Element(name="Filename", required=false)
//	@XmlElement(name="Filename")
	private String fileName;
	
	@Element(name="Content", required=false)
//	@XmlElement(name="Content")
	private String content;
	
	public String getFileNameClean(){
		return cleanFileName(fileName);
	}
}
