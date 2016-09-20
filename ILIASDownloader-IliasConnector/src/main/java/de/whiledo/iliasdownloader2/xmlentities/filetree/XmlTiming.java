package de.whiledo.iliasdownloader2.xmlentities.filetree;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Timing", strict=false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XmlTiming {

	@Attribute(name="starting_time", required=false)
//	@XmlAttribute(name="starting_time")
	private String timestampStartingTime;
	
//	@XmlAttribute(name="endingTime")
	@Attribute(name="endingTime", required=false)
	private String timestampEndingTime;
	
//	@XmlAttribute(name="visibility")
	@Attribute(name="visibility", required=false)
	private int visibility;
	
	@Attribute(name="changeable", required=false)
//	@XmlAttribute(name="changeable")
	private int changeable;
	
//	@XmlAttribute(name="earliest_start")
	@Attribute(name="earliest_start", required=false)
	private String timestampEarliestStart;
	
//	@XmlAttribute(name="latest_end")
	@Attribute(name="latest_end", required=false)
	private String timestampLatestEnd;
	
}
