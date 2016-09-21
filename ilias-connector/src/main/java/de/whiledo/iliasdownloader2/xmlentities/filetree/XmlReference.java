package de.whiledo.iliasdownloader2.xmlentities.filetree;

import java.util.List;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="References", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlReference {

	@Attribute(name="ref_id", required=false)
//	@XmlAttribute(name="ref_id")
	private long refId;
	
	@Attribute(name="parent_id", required=false)
//	@XmlAttribute(name="parent_id")
	private long parentId;
	
	@Attribute(name="accessInfo", required=false)
//	@XmlAttribute(name="accessInfo")
	private String accessInfo;
	
//	@XmlElementWrapper(name="Path")
//	@XmlElement(name="Element")
	@ElementList(name="Path", required=false)
	private List<XmlPathElement> pathEntries;
	
//	@XmlElement(name="TimeTarget")
	@ElementList(name="TimeTarget", required=false, inline=true)
	private List<XmlTimeTarget> timeTargets;
	
	
}
