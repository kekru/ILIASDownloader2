package de.whiledo.iliasdownloader2.xmlentities.file;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Version", strict=false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XmlVersion {

	@Attribute(name="id", required=false)
//	@XmlAttribute(name="id")
	private String id;
	
	@Attribute(name="date", required=false)
//	@XmlAttribute(name="date")
	private long timestamp;
	
	@Attribute(name="usr_id", required=false)
//	@XmlAttribute(name="usr_id")
	private String usrId;
	
}