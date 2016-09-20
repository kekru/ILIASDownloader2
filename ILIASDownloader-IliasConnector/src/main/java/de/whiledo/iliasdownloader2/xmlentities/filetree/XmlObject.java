package de.whiledo.iliasdownloader2.xmlentities.filetree;

import static de.whiledo.iliasdownloader2.util.Functions.cleanFileName;
import static de.whiledo.iliasdownloader2.util.Functions.iliasXmlStringToDate;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
//import javax.xml.bind.annotation.XmlTransient;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import de.whiledo.iliasdownloader2.util.IliasConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

//@XmlAccessorType(XmlAccessType.FIELD)
@Root(name="Object", strict=false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XmlObject {

	@Attribute(name="type", required=false)
//	@XmlAttribute(name="type")
	private String type;

	@Attribute(name="obj_id", required=false)
//	@XmlAttribute(name="obj_id")
	private long objId;

	@Element(name="Title", required=false)
//	@XmlElement(name="Title")
	private String title;

	@Element(name="Description", required=false)
//	@XmlElement(name="Description")
	private String description;

	@Element(name="Owner", required=false)
//	@XmlElement(name="Owner")
	private long owner;

	@Element(name="CreateDate", required=false)
//	@XmlElement(name="CreateDate")
	private String createdDate;

	@Element(name="LastUpdate", required=false)
//	@XmlElement(name="LastUpdate")
	private String updatedDate; 

	@Element(name="ImportId", required=false)
//	@XmlElement(name="ImportId")
	private String importId; 

//	@XmlElementWrapper(name="Properties")
//	@XmlElement(name="Property")
	@ElementList(name="Properties", required=false)
	private List<XmlProperty> properties;

//	@XmlElement(name="References")
	@ElementList(name="References", inline=true, required=false)
	private List<XmlReference> references;

//	@XmlTransient
	@Transient
	private List<XmlObject> children;

	public Date getCreatedDate(){
		return iliasXmlStringToDate(createdDate);
	}

	public Date getUpdatedDate(){
		return iliasXmlStringToDate(updatedDate);
	}

	public long getRefIdOne(){
		return !references.isEmpty() ? references.get(0).getRefId() : -1;
	}

	public List<Long> getRefIds(){
		val result = new LinkedList<Long>();
		for(val x : references){
			result.add(x.getRefId());
		}
		return result;
	}

	public boolean isFolder(){
		return getType().equals(IliasConstants.FOLDER);
	}

	public boolean isFile(){
		return getType().equals(IliasConstants.FILE);
	}

	public boolean isCourse(){
		return getType().equals(IliasConstants.COURSE);
	}

	public boolean isBlog(){
		return getType().equals(IliasConstants.BLOG);
	}

	public boolean isWiki(){
		return getType().equals(IliasConstants.WIKI);
	}

	public boolean isObjectBlock(){
		return getType().equals(IliasConstants.OBJECTBLOCK);
	}

	public boolean isDatabase(){
		return getType().equals(IliasConstants.DATABASE);
	}

	public boolean isExercise(){
		return getType().equals(IliasConstants.EXERCISE);
	}

	public boolean isEtherpad(){
		return getType().equals(IliasConstants.ETHERPAD);
	}

	public boolean isWeblink(){
		return getType().equals(IliasConstants.WEBLINK);
	}

	/**
	 * Returns the Path without the Filename
	 * @return
	 */
	public String getPath(){
		return getPath(false);
	}

	public String getPathComplete(){
		return getPath(true);
	}

	private String getPath(boolean fullpath){
		val pathList = references.get(0).getPathEntries();
		String s = "";
		if(fullpath || !isCourse()){
			for(int i=pathList.size() -1; i >= 0; i--){
				XmlPathElement pathElement = pathList.get(i);			
				s = cleanFileName(pathElement.getName()) + "/" + s;

				if(!fullpath && (pathElement.getType().equals(IliasConstants.COURSE))){
					break;
				}
			}
		}

		s = s + (isFolderType() ? "/" + cleanFileName(getTitle()) : "");
		return s.startsWith("/") ? s.substring(1) : s;
	}

	public boolean isFolderType(){
		return isFolder() || isExercise() || isCourse();
	}

	public String getFileNameClean(){
		return cleanFileName(getTitle());
	}

	public String getCourseName(){
		if(isCourse()){
			return getTitle();
		}
		
		for(XmlPathElement p : getReferences().get(0).getPathEntries()){
			if(p.getType().equals(IliasConstants.COURSE)){
				return p.getName();
			}
		}
		
		return "";
	}

	private String getPropertyValue(String name){
		for(val p : properties){
			if(p.getKey().equals(name)){
				return p.getValue();
			}
		}

		throw new RuntimeException("No Property with name: "+ name + " found in "+XmlObject.class.getName() + " " + this);
	}

	/**
	 * FileSize in bytes
	 * 
	 * @return
	 */
	public long getFileSize(){
		return Long.parseLong(getPropertyValue(IliasConstants.FILE_SIZE));
	}

	public String getFileExtension(){
		return getPropertyValue(IliasConstants.FILE_EXTENSION);
	}

	public int getFileVersion(){
		return Integer.parseInt(getPropertyValue(IliasConstants.FILE_VERSION));
	}

}
