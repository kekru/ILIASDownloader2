package de.whiledo.iliasdownloader2.syncrunner.service;

import java.util.HashSet;
import java.util.Set;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
//import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import de.whiledo.iliasdownloader2.util.DownloadMethod;
import de.whiledo.iliasdownloader2.util.LoginType;

//@XmlRootElement(name="iliasdownloader")
@Root(name="iliasdownloader", strict=false)
//@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class IliasProperties {

	@Element(name="server", required=false)
//	@XmlElement(name="server")
	private String iliasServerURL = "";
	
	@Element(name="iliasclient", required=false)
//	@XmlElement(name="iliasclient")
	private String iliasClient = "";
	
	@Element(name="userlogin", required=false)
//	@XmlElement(name="userlogin")
	private String userName = "";
	
	@Element(name="basedir", required=false)
//	@XmlElement(name="basedir")
	private String baseDirectory = "ilias";
	
	@Element(name="maxsize", required=false)
//	@XmlElement(name="maxsize")
	private long maxFileSize = Long.MAX_VALUE;
	
	@Element(name="logintype", required=false)
//	@XmlElement(name="logintype")
	private LoginType loginType = LoginType.LDAP;
	
	@Element(name="downloadmethod", required=false)
//	@XmlElement(name="downloadmethod")
	private DownloadMethod downloadMethod = DownloadMethod.WEBSERVICE;
	
	@Element(name="allowdownload", required=false)
//	@XmlElement(name="allowdownload")
	private boolean allowDownload = true;
	
	@Element(name="usethreads", required=false)
	private boolean useThreads = true;
	
	@Element(name="lookandfeel", required=false)
//	@XmlElement(name="lookandfeel")
	private String lookAndFeel;
	
	@Element(name="shownotifications", required=false)
//	@XmlElement(name="shownotifications")
	private boolean showNotifications = true;
	
	@Element(name="autosyncactive", required=false)
//	@XmlElement(name="autosyncactive")
	private boolean autoSyncActive = false;

	@Element(name="autosyncinterval", required=false)
//	@XmlElement(name="autosyncinterval")
	private int autoSyncIntervalInSeconds = 60 * 60;
	
	@Element(name="syncallcourses", required=false)
//	@XmlElement(name="syncallcourses")
	private boolean syncAll = true;
	
	@ElementList(name="activecourses", required=false, entry="course")
//	@XmlElementWrapper(name="activecourses")
//	@XmlElement(name="course")
	private Set<Long> activeCourses = new HashSet<Long>();
	
	@ElementList(name="blockedfiles", required=false, entry="file")
//	@XmlElementWrapper(name="blockedfiles")
//	@XmlElement(name="file")
	private Set<Long> blockedFiles = new HashSet<Long>();
}

