package de.whiledo.iliasdownloader2.xmlentities.file;

import java.util.List;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
//import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Example XML:
 * <pre>
  &lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;!DOCTYPE File PUBLIC "-//ILIAS//DTD FileAdministration//EN" "http://www.ilias.fh-dortmund.de/ilias/xml/ilias_file_3_8.dtd"&gt;
&lt;!--Exercise Object--&gt;
&lt;File obj_id="il_3876_file_497091" version="1" size="1976780" type="application/pdf"&gt;
	&lt;Filename&gt;Abgabe Praktikumsphase 2.pdf&lt;/Filename&gt;
	&lt;Title&gt;Abgabe Praktikumsphase 2.pdf&lt;/Title&gt;
	&lt;Description/&gt;
	&lt;Rating&gt;0&lt;/Rating&gt;
	&lt;Content mode="PLAIN"&gt;xxx&lt;/Content&gt;
	&lt;Versions&gt;
		&lt;Version id="1" date="1420652266" usr_id="il_3876_usr_36323"/&gt;
	&lt;/Versions&gt;
&lt;/File&gt;
  
  </pre>
 *
 */

//@XmlRootElement(name="File")
//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="File", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlFileContent {
	
	@Attribute(name="obj_id", required=false)
//	@XmlAttribute(name="obj_id")
	private String objId;
	
	@Attribute(name="version", required=false)
//	@XmlAttribute(name="version")
	private int version;
	
	@Attribute(name="size", required=false)
//	@XmlAttribute(name="size")
	private long size;
	
	@Attribute(name="type", required=false)
//	@XmlAttribute(name="type")
	private String type;
	
	@Element(name="Filename", required=false)
//	@XmlElement(name="Filename")
	private String filename;
	
	@Element(name="Title", required=false)
//	@XmlElement(name="Title")
	private String title;
	
	@Element(name="Description", required=false)
//	@XmlElement(name="Description")
	private String description;
	
	@Element(name="Rating", required=false)
//	@XmlElement(name="Rating")
	private int rating;
	
	@Element(name="Content", required=false)
//	@XmlElement(name="Content")
	private String content;
	
	@ElementList(name="Versions", required=false)
//	@XmlElementWrapper(name="Versions")
//	@XmlElement(name="Version")
	private List<XmlVersion> versions;	
	
}
