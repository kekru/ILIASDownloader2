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
@Root(name="MetaData", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlMetaData {

	@Element(name="General", required=false)
//	@XmlElement(name="General")
	private XmlGeneral general;
}
