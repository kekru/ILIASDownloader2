package de.whiledo.iliasdownloader2.xmlentities.filetree;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Property", strict=false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XmlProperty {

	@Attribute(name="name", required=false)
//	@XmlAttribute(name="name")
	private String key;
	
//	@XmlValue
	@Text(required=false)
	private String value;
}
