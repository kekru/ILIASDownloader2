package de.whiledo.iliasdownloader2.service;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.whiledo.iliasdownloader2.service.IliasUtil;

public class IliasUtilTest {


	@Test
	@Ignore
	public void testSSL() throws MalformedURLException, IOException {
		Assert.assertTrue("Bitte mit Java 1.7 Testen :)", System.getProperty("java.version").startsWith("1.7"));	
		
		String loginURL = "https://www.ilias.fh-dortmund.de/ilias/login.php?target=&soap_pw=&ext_uid=&cookies=nocookies&client_id=ilias-fhdo&lang=de";
		InputStream inputStream = new URL(loginURL).openConnection().getInputStream();
		BufferedReader b = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		String s = "";
		boolean foundText = false;
		while((s = b.readLine()) != null){
			if(s.contains("ILIAS-Anmeldeseite")){
				foundText = true;
			}
		}
		
		Assert.assertTrue(foundText);
		
		String prefix = "ilClientId=";
		String postfix = ";";
		Pattern p = Pattern.compile(prefix+"[^"+postfix+"]*"+postfix);
		
		List<String> cookies = new URL(loginURL).openConnection().getHeaderFields().get("Set-Cookie");
		String clientId = "";
		
		for(String cookie : cookies){
			Matcher m = p.matcher(cookie);

			if(m.find()){
				s = m.group(0);
				clientId = cookie.substring(0, cookie.lastIndexOf(postfix)).substring(prefix.length());
			}

		}
		
		Assert.assertEquals("ilias-fhdo", clientId);

	}
	
	@Test
	public void testGetIliasClientId(){
		String s = IliasUtil.findClientByLoginPageOrWebserviceURL("https://www.ilias.fh-dortmund.de/ilias/login.php");
		assertEquals("ilias-fhdo", s);
	}
}
