package de.whiledo.iliasdownloader2.xmlentities.exercise;

import java.util.List;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
//import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Example XML:
 * 
 * <pre>
  &lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;!DOCTYPE Exercise PUBLIC "-//ILIAS//DTD ExerciseAdministration//EN" "http://www.ilias.fh-dortmund.de/ilias/xml/ilias_exercise_3_10.dtd"&gt;
&lt;!--Exercise Object--&gt;
&lt;Exercise obj_id="il_3876_exc_502212" owner="il_3876_usr_178471"&gt;
	&lt;Title&gt;Test�bung 1&lt;/Title&gt;
	&lt;Description/&gt;
	&lt;Instruction&gt;&amp;lt;p&amp;gt;Erst musst du Schritt 1 machen, dann Schritt 2&amp;lt;/p&amp;gt;&amp;#13;&amp;#10;&amp;lt;p&amp;gt;��� ein paar Umlauteeeeee���&amp;lt;/p&amp;gt;&lt;/Instruction&gt;
	&lt;DueDate&gt;1456193340&lt;/DueDate&gt;
	&lt;Files&gt;
		&lt;File size="32"&gt;
			&lt;Filename&gt;Arbeitsanweisung.txt&lt;/Filename&gt;
			&lt;Content mode="PLAIN"&gt;RWluZSBBcmJlaXRzYW53ZWlzdW5nIHp1bSBUZXN0ZW4=&lt;/Content&gt;
		&lt;/File&gt;
	&lt;/Files&gt;
	&lt;Members/&gt;
&lt;/Exercise&gt;
 * </pre>
 * 
 *
 */
//@XmlRootElement(name="Exercise")
//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Exercise", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlExercise {
	
	@Element(name="Title", required=false)
//	@XmlElement(name="Title")
	private String title;
	
	@Element(name="Description", required=false)
//	@XmlElement(name="Description")
	private String description;
	
	@Element(name="Instruction", required=false)
//	@XmlElement(name="Instruction")
	private String instruction;
	
	@Element(name="DueDate", required=false)
//	@XmlElement(name="DueDate")
	private String timestampFinishDate;
	
//	@XmlElementWrapper(name="Files")
//	@XmlElement(name="File")
	@ElementList(name="Files", required=false)
	private List<XmlExerciseFile> exerciseFiles;
	
	
	
	
}
