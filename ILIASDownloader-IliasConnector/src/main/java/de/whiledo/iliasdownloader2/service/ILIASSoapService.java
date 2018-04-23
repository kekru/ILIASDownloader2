package de.whiledo.iliasdownloader2.service;

import static de.whiledo.iliasdownloader2.util.Functions.parseXmlObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import de.whiledo.iliasdownloader2.exception.IliasAuthenticationException;
import de.whiledo.iliasdownloader2.exception.IliasException;
import de.whiledo.iliasdownloader2.exception.IliasHTTPSException;
import de.whiledo.iliasdownloader2.util.LoginType;
import de.whiledo.iliasdownloader2.util.SOAPResult;
import de.whiledo.iliasdownloader2.util.TwoObjectsX;
import de.whiledo.iliasdownloader2.xmlentities.course.XmlCourse;
import de.whiledo.iliasdownloader2.xmlentities.exercise.XmlExercise;
import de.whiledo.iliasdownloader2.xmlentities.file.XmlFileContent;
import de.whiledo.iliasdownloader2.xmlentities.filetree.XmlObject;
import de.whiledo.iliasdownloader2.xmlentities.filetree.XmlObjects;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class ILIASSoapService {

	@Getter
	private String soapServerURL;

	@Getter
	private String clientName;

	@Getter
	@Setter
	private String sessionId = null;

	@Getter
	private boolean webdavAuthenticationActive = false;

	private long userId = -1;


	public ILIASSoapService(String soapServerURL, String clientName){
		this.soapServerURL = soapServerURL;
		this.clientName = clientName;
	}

	public String getIliasInstallationURL(){
		return IliasUtil.getIliasInstallationURL(soapServerURL);
	}

	public String getURLInIlias(XmlObject xmlObject){
		return getIliasInstallationURL() + "/goto.php?target=" + xmlObject.getType() + "_" + xmlObject.getRefIdOne();
	}

	public InputStream getWebdavFileStream(long refId) {
		try {
			return new BufferedInputStream(new URL(IliasUtil.findWebdavByWebservice(soapServerURL) + "/" + clientName + "/ref_" + refId).openConnection().getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param loginType
	 * @throws IliasAuthenticationException
	 */
	public void login(final String username, final String password, LoginType loginType, final boolean enableWebdavAuthentication){
		webdavAuthenticationActive = false;
		String methodName;
		userId = -1;

		switch(loginType){
		case DEFAULT: methodName = "login"; break;
		case LDAP: methodName = "loginLDAP"; break;
		case CAS: methodName = "loginCAS"; break;
		default: throw new IliasAuthenticationException("loginType was "+loginType);
		}

		SOAPResult soapResult = null;
		try{
			soapResult = sendSoapRequestGetSoapBody(methodName, new TwoObjectsX<String, String>("client", clientName), new TwoObjectsX<String, String>("username", username), new TwoObjectsX<String, String>("password", password));
		}catch(IliasHTTPSException e){
			throw e;
		}catch(Exception e){
			throw new IliasAuthenticationException("Error during login. Maybe wrong Server or wrong client id (not username)", e);
		}
		sessionId = soapResult.getText();// firstChild.getFirstChild().getTextContent();
		String error = soapResult.getError();// firstChild.getChildNodes().getLength() >= 2 ? firstChild.getChildNodes().item(1).getTextContent() : null;

		if(soapResult.isFaultCode() || (error != null && !error.trim().isEmpty())){
			sessionId = null;

			if("Authentication failed.".equals(error)){
				throw new IliasAuthenticationException("Authentication failed. Wrong username/password");
			}else {
				throw new IliasAuthenticationException("Authentication failed: " + error);
			}
		}else{
			if(enableWebdavAuthentication){
				enableWebdavAuthentication(username, password.toCharArray());
			}
		}

	}



	public void enableWebdavAuthentication(final String username, final char[] password) {
		webdavAuthenticationActive = true;

		Authenticator.setDefault(new Authenticator(){
			@Override 
			protected PasswordAuthentication getPasswordAuthentication(){ 
				try {
					if(webdavAuthenticationActive && getRequestingURL().getHost().equals(new URL(soapServerURL).getHost())){
						return new PasswordAuthentication(username, password);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return null;
			} 
		});
	}

	public boolean logout(){
		userId = -1;
		boolean result = Boolean.parseBoolean(sendSoapRequest("logout", new TwoObjectsX<String, String>("sid", sessionId)));
		sessionId = null;
		return result;
	}

	public boolean isLoggedIn(){
		if(sessionId == null){
			return false;
		}

		SOAPResult soapResult = null;
		try{
			soapResult = sendSoapRequestGetSoapBody("getUserIdBySid", new TwoObjectsX<String, String>("sid", sessionId));
		}catch(Exception e){
			return false;
		}

		return !soapResult.isFaultCode();
	}

	public XmlExercise getExercise(long refId){
		String s = sendSoapRequest("getExerciseXML", new TwoObjectsX<String, String>("sid", sessionId), new TwoObjectsX<String, Long>("ref_id", refId), new TwoObjectsX<String, Integer>("attachment_mode", 1));
		return parseXmlObject(s, XmlExercise.class);
	}

	public XmlFileContent getFile(long refId){
		String s = sendSoapRequest("getFileXML", new TwoObjectsX<String, String>("sid", sessionId), new TwoObjectsX<String, Long>("ref_id", refId), new TwoObjectsX<String, Integer>("attachment_mode", 1));

		return parseXmlObject(s, XmlFileContent.class);
	}

	public List<XmlObject> getCourseObjects(long refId){
		return getCourseObjects(refId, getUserId());
	}

	public List<XmlObject> getCourseObjects(long refId, long userId){
		String s = sendSoapRequest("getXMLTree", new TwoObjectsX<String, String>("sid", sessionId), new TwoObjectsX<String, Long>("ref_id", refId), new TwoObjectsX<String, String>("types", ""), new TwoObjectsX<String, Long>("user_id", userId));
		return parseXmlObject(s, XmlObjects.class).getObjects();

	}

	
	public XmlCourse getCourse(long refId){
		String s = sendSoapRequest("getCourseXML", new TwoObjectsX<String, String>("sid", sessionId), new TwoObjectsX<String, Long>("course_id", refId));
		
		if(s == null){
			throw new IliasException("Course with RefId " + refId + " not accessible. Are you still in that course?");
		}

		return parseXmlObject(s, XmlCourse.class);
	}


	public long getRefIdByObjId(long objId){
		return Long.parseLong(sendSoapRequest("getRefIdsByObjId", new TwoObjectsX<String, String>("sid", sessionId), new TwoObjectsX<String, Long>("obj_id", objId)));
	}

	public long getUserId(){
		return userId != -1 ? userId : (userId = Long.parseLong(sendSoapRequest("getUserIdBySid", new TwoObjectsX<String, String>("sid", sessionId))));
	}

	public List<Long> getCourseIds(){
		return getCourseIds(getUserId());
	}

	public List<Long> getCourseIds(long userId){
		val result = new LinkedList<Long>();

		String s = sendSoapRequest("getUserRoles", new TwoObjectsX<String, String>("sid", sessionId), new TwoObjectsX<String, Long>("user_id", userId));
		XmlObjects objects = parseXmlObject(s, XmlObjects.class);

		final String ID_PREFIX = "il_crs_member_";
		for(val x : objects.getObjects()){
			if(x.getTitle().contains(ID_PREFIX)){
				Long id = Long.parseLong(x.getTitle().substring(ID_PREFIX.length()));
				if(id != null){
					result.add(id);
				}
			}
		}

		return result;
	}

	
	private String sendSoapRequest(String soapMethodName, TwoObjectsX<?, ?>... mapNameToValue) {
		return sendSoapRequestGetSoapBody(soapMethodName, mapNameToValue).getText();
	}

	private SOAPResult sendSoapRequestGetSoapBody(String soapMethodName, TwoObjectsX<?, ?>... mapNameToValue) {
		SoapObject soapObject = new SoapObject("http://schemas.xmlsoap.org/soap/envelope/", soapMethodName);

		for(val nameToValue : mapNameToValue){
			PropertyInfo propertyInfo = new PropertyInfo();
			propertyInfo.setName(String.valueOf(nameToValue.getObjectA()));
			propertyInfo.setValue(String.valueOf(nameToValue.getObjectB()));
			propertyInfo.setType(nameToValue.getObjectB().getClass());
			soapObject.addProperty(propertyInfo);
		}

		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.setOutputSoapObject(soapObject);

		SOAPResult result = new SOAPResult();
		result.setFaultCode(false);
		
		HttpTransportSE http = new HttpTransportSE(soapServerURL);
		http.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		try {
			http.call(soapServerURL+"/"+soapMethodName, soapEnvelope);
			
			result.setText(String.valueOf(soapEnvelope.getResponse()));
		} catch(SSLException e){
			IliasUtil.throwDefaultHTTPSException(e);
			
		} catch(SoapFault soapFault){	
			result.setFaultCode(true);
			result.setError(soapFault.getMessage());
			
		} catch (IOException | XmlPullParserException e) {
			throw new RuntimeException(e);
		}
		
		return result;
		

	}


}
