package de.whiledo.iliasdownloader2.xmlentities.course;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * <pre>
  
  	&lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;!DOCTYPE Course PUBLIC "-//ILIAS//DTD Course//EN" "http://www.ilias.fh-dortmund.de/ilias/xml/ilias_course_3_10.dtd"&gt;
&lt;!--Export of ILIAS course 123456789 of installation 9879.--&gt;
&lt;Course exportVersion="2" id="il_3876_crs_97137" showMembers="Yes"&gt;
	&lt;MetaData&gt;
		&lt;General Structure="Hierarchical"&gt;
			&lt;Identifier Catalog="ILIAS" Entry="il__crs_97137"/&gt;
			&lt;Title Language="de"&gt;Programmieren mit Ilias&lt;/Title&gt;
			&lt;Language Language="de"/&gt;
			&lt;Description Language="de"&gt;BEschreibung des Kurses&lt;/Description&gt;
			&lt;Keyword Language="en"/&gt;
		&lt;/General&gt;
		&lt;Lifecycle Status="Draft"&gt;
			&lt;Version Language="en"/&gt;
			&lt;Contribute Role="Author"&gt;
				&lt;Entity&gt;Prof. Dr. Sommer&lt;/Entity&gt;
				&lt;Date/&gt;
			&lt;/Contribute&gt;
		&lt;/Lifecycle&gt;
	&lt;/MetaData&gt;
	&lt;AdvancedMetaData/&gt;
	&lt;Admin id="il_9879_usr_1234" notification="No" passed="No"/&gt;
	&lt;Member id="il_9879_usr_1235" blocked="No" passed="No"/&gt;
	&lt;Member id="il_9879_usr_1236" blocked="No" passed="No"/&gt;
	&lt;Member id="il_9879_usr_1237" blocked="No" passed="No"/&gt;
	&lt;Member id="il_9879_usr_1238" blocked="No" passed="No"/&gt;
	&lt;Member id="il_9879_usr_1239" blocked="No" passed="No"/&gt;
	&lt;Settings&gt;
		&lt;Availability&gt;
			&lt;Unlimited/&gt;
		&lt;/Availability&gt;
		&lt;Syllabus/&gt;
		&lt;ImportantInformation/&gt;
		&lt;Contact&gt;
			&lt;Name/&gt;
			&lt;Responsibility/&gt;
			&lt;Phone/&gt;
			&lt;Email/&gt;
			&lt;Consultation/&gt;
		&lt;/Contact&gt;
		&lt;Registration registrationType="Password" maxMembers="0" notification="Yes" waitingList="Yes"&gt;
			&lt;Unlimited/&gt;
			&lt;Password&gt;xxx&lt;/Password&gt;
		&lt;/Registration&gt;
		&lt;Sort type="Manual"/&gt;
		&lt;Archive Access="Disabled"&gt;
			&lt;Start&gt;1393092506&lt;/Start&gt;
			&lt;End&gt;1481497200&lt;/End&gt;
		&lt;/Archive&gt;
	&lt;/Settings&gt;
&lt;/Course&gt;

   </pre>
 *
 */

//@XmlRootElement(name="Course")
//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Course", strict=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlCourse {
	
//	@XmlElement(name="MetaData")
	@Element(name="MetaData", required=false)
	private XmlMetaData metaData;
	
//	@XmlElement(name="Admin")
//	@Element(name="Admin", required=false)
//	private XmlMember admin;
//	
//	@XmlElement(name="Member")
//	@ElementList(name="Member", required=false, inline=true)
//	private List<XmlMember> member;
	
//	@XmlElement(name="Settings")
	@Element(name="Settings", required=false)
	private XmlSettings settings;
	
}
