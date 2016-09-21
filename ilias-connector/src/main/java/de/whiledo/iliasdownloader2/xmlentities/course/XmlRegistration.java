package de.whiledo.iliasdownloader2.xmlentities.course;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Registration", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlRegistration {

	@Attribute(name="registrationType", required=false)
//	@XmlAttribute(name="registrationType")
	private String registrationType;
	
	@Attribute(name="maxMembers", required=false)
//	@XmlAttribute(name="maxMembers")
	private int maxMembers;
	
	@Attribute(name="notification", required=false)
//	@XmlAttribute(name="notification")
	private String notification;
	
	@Attribute(name="waitingList", required=false)
//	@XmlAttribute(name="waitingList")
	private String waitingList;
	
	@Element(name="Unlimited", required=false)
//	@XmlElement(name="Unlimited")
	private String unlimited;
	
	@Element(name="Password", required=false)
//	@XmlElement(name="Password")
	private String password;
	
}
