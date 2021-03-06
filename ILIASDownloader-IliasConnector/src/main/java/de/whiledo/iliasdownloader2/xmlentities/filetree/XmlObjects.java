package de.whiledo.iliasdownloader2.xmlentities.filetree;

import java.util.List;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.simpleframework.xml.ElementList;

/**
 * Example XML:
 * 
 * 
 * <pre>
  &lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;!DOCTYPE Objects PUBLIC "-//ILIAS//DTD ILIAS Repositoryobjects//EN" "http://www.ilias.fh-dortmund.de/ilias/xml/ilias_object_4_0.dtd"&gt;
&lt;!--Export of ILIAS objects--&gt;
&lt;Objects&gt;
	&lt;Object type="crs" obj_id="470037"&gt;
		&lt;Title&gt;TestKurs kekru001&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-01 14:22:40&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-01 14:22:57&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="338991" parent_id="140" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="0" ending_time="0" visibility="0"/&gt;
				&lt;Suggestion starting_time="1412114401" ending_time="1412200500" changeable="0" earliest_start="1412114401" latest_end="1412200500"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="blog" obj_id="470043"&gt;
		&lt;Title&gt;test Blog&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-01 14:25:03&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-01 14:25:03&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="338994" parent_id="338991" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1412166381" ending_time="1412166381" visibility="0"/&gt;
				&lt;Suggestion starting_time="1412166381" ending_time="1412166381" changeable="0" earliest_start="1412166381" latest_end="1412200500"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="wiki" obj_id="470048"&gt;
		&lt;Title&gt;test Wiki&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-01 14:27:27&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-01 14:27:31&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="338998" parent_id="338991" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1412166527" ending_time="1412166527" visibility="0"/&gt;
				&lt;Suggestion starting_time="1412166527" ending_time="1412166527" changeable="0" earliest_start="1412166527" latest_end="1412200500"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="itgr" obj_id="470051"&gt;
		&lt;Title&gt;Test Objektblock&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-01 14:29:22&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-01 14:29:22&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="339001" parent_id="338991" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1412166638" ending_time="1412166638" visibility="0"/&gt;
				&lt;Suggestion starting_time="1412166638" ending_time="1412166638" changeable="0" earliest_start="1412166638" latest_end="1412200500"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="dcl" obj_id="470054"&gt;
		&lt;Title&gt;Test DB&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-01 14:34:53&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-01 14:34:53&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="339004" parent_id="338991" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1412166970" ending_time="1412166970" visibility="0"/&gt;
				&lt;Suggestion starting_time="1412166970" ending_time="1412166970" changeable="0" earliest_start="1412166970" latest_end="1412200500"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="fold" obj_id="470056"&gt;
		&lt;Title&gt;Ordner 1&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-01 14:38:14&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-01 14:38:14&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="339006" parent_id="338991" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1412167170" ending_time="1412167170" visibility="0"/&gt;
				&lt;Suggestion starting_time="1412167170" ending_time="1412167170" changeable="0" earliest_start="1412167170" latest_end="1412200500"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="fold" obj_id="476439"&gt;
		&lt;Title&gt;Ein Ordner/mit Slash&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-18 15:31:28&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-18 15:31:28&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="344300" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1413639197" ending_time="1413639197" visibility="0"/&gt;
				&lt;Suggestion starting_time="1413639197" ending_time="1413639197" changeable="0" earliest_start="1413639197" latest_end="1413669300"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="file" obj_id="476440"&gt;
		&lt;Title&gt;Hallo Welt.txt&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-18 15:32:23&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-18 15:32:23&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;Properties&gt;
			&lt;Property name="fileSize"&gt;10&lt;/Property&gt;
			&lt;Property name="fileExtension"&gt;txt&lt;/Property&gt;
			&lt;Property name="fileVersion"&gt;1&lt;/Property&gt;
		&lt;/Properties&gt;
		&lt;References ref_id="344301" parent_id="344300" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1413639253" ending_time="1413639253" visibility="0"/&gt;
				&lt;Suggestion starting_time="1413639253" ending_time="1413639253" changeable="0" earliest_start="1413639253" latest_end="1413669300"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
				&lt;Element ref_id="344300" type="fold"&gt;Ein Ordner/mit Slash&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="file" obj_id="476441"&gt;
		&lt;Title&gt;Hallo Welt.txt&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-18 15:33:13&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-18 15:33:13&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;Properties&gt;
			&lt;Property name="fileSize"&gt;10&lt;/Property&gt;
			&lt;Property name="fileExtension"&gt;txt&lt;/Property&gt;
			&lt;Property name="fileVersion"&gt;1&lt;/Property&gt;
		&lt;/Properties&gt;
		&lt;References ref_id="344302" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1413639301" ending_time="1413639301" visibility="0"/&gt;
				&lt;Suggestion starting_time="1413639301" ending_time="1413639301" changeable="0" earliest_start="1413639301" latest_end="1413669300"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="fold" obj_id="477726"&gt;
		&lt;Title&gt;/Ein : Ordner:mit Doopel/punkt und Slash&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2014-10-22 21:51:24&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2014-10-22 21:51:24&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="345359" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1414007600" ending_time="1414007600" visibility="0"/&gt;
				&lt;Suggestion starting_time="1414007600" ending_time="1414007600" changeable="0" earliest_start="1414007600" latest_end="1414014900"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="wiki" obj_id="502211"&gt;
		&lt;Title&gt;Ein Wiki&lt;/Title&gt;
		&lt;Description&gt;Hier kann ganz viel rein geschrieben werden, in das Wiki&lt;/Description&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2015-02-23 03:02:45&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2015-02-23 03:08:53&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="364844" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1424657327" ending_time="1424657327" visibility="0"/&gt;
				&lt;Suggestion starting_time="1424657327" ending_time="1424657327" changeable="0" earliest_start="1424657327" latest_end="1424732100"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="exc" obj_id="502212"&gt;
		&lt;Title&gt;Test�bung 1&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2015-02-23 03:03:37&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2015-02-23 03:03:37&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="364845" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1424657362" ending_time="1424657362" visibility="0"/&gt;
				&lt;Suggestion starting_time="1424657362" ending_time="1424657362" changeable="0" earliest_start="1424657362" latest_end="1424732100"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="xpdl" obj_id="502213"&gt;
		&lt;Title&gt;Ein Etherpad&lt;/Title&gt;
		&lt;Description&gt;Eine Beschreibung&lt;/Description&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2015-02-23 03:04:27&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2015-02-23 03:04:27&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="364846" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1424657413" ending_time="1424657413" visibility="0"/&gt;
				&lt;Suggestion starting_time="1424657413" ending_time="1424657413" changeable="0" earliest_start="1424657413" latest_end="1424732100"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="webr" obj_id="502214"&gt;
		&lt;Title&gt;Hier gehts zu whiledo.de&lt;/Title&gt;
		&lt;Description/&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2015-02-23 03:07:11&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2015-02-23 03:07:11&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="364847" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1424657578" ending_time="1424657578" visibility="0"/&gt;
				&lt;Suggestion starting_time="1424657578" ending_time="1424657578" changeable="0" earliest_start="1424657578" latest_end="1424732100"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
	&lt;Object type="blog" obj_id="502215"&gt;
		&lt;Title&gt;Kevins Blog&lt;/Title&gt;
		&lt;Description&gt;Einen Blog gibt es hier auch&lt;/Description&gt;
		&lt;Owner&gt;178471&lt;/Owner&gt;
		&lt;CreateDate&gt;2015-02-23 03:07:49&lt;/CreateDate&gt;
		&lt;LastUpdate&gt;2015-02-23 03:07:49&lt;/LastUpdate&gt;
		&lt;ImportId/&gt;
		&lt;References ref_id="364848" parent_id="339006" accessInfo="granted"&gt;
			&lt;TimeTarget type="0"&gt;
				&lt;Timing starting_time="1424657617" ending_time="1424657617" visibility="0"/&gt;
				&lt;Suggestion starting_time="1424657617" ending_time="1424657617" changeable="0" earliest_start="1424657617" latest_end="1424732100"/&gt;
			&lt;/TimeTarget&gt;
			&lt;Path&gt;
				&lt;Element ref_id="1" type="root"&gt;Magazin&lt;/Element&gt;
				&lt;Element ref_id="140" type="cat"&gt;Sandkasten&lt;/Element&gt;
				&lt;Element ref_id="338991" type="crs"&gt;TestKurs kekru001&lt;/Element&gt;
				&lt;Element ref_id="339006" type="fold"&gt;Ordner 1&lt;/Element&gt;
			&lt;/Path&gt;
		&lt;/References&gt;
	&lt;/Object&gt;
&lt;/Objects&gt;
 * </pre>
 *
 */
//@XmlRootElement(name="Objects")
//@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlObjects {
	
//	@XmlElement(name="Object")	
	@ElementList(name="Objects", inline=true, required=false)
	private List<de.whiledo.iliasdownloader2.xmlentities.filetree.XmlObject> objects;
	
}
