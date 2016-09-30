package de.whiledo.iliasdownloader2.xmlentities.course;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Settings", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlSettings {

	@Element(name="ImportantInformation", required=false)
//	@XmlElement(name="ImportantInformation")
	private String importantInformation;
	
	@Element(name="Registration", required=false)
//	@XmlElement(name="Registration")
	private XmlRegistration registration;
}
