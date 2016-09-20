package de.whiledo.iliasdownloader2.xmlentities.course;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlMember {

	@Attribute(name="id", required=false)
//	@XmlAttribute(name="id")
	private String idString;
	
	@Attribute(name="notification", required=false)
//	@XmlAttribute(name="notification")
	private String notification;
	
	@Attribute(name="blocked", required=false)
//	@XmlAttribute(name="blocked")
	private String blocked;
	
	@Attribute(name="passed", required=false)
//	@XmlAttribute(name="passed")
	private String passed;
}
