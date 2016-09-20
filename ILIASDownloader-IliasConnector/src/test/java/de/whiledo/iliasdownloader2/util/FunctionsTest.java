package de.whiledo.iliasdownloader2.util;

import org.junit.Assert;
import org.junit.Test;

import de.whiledo.iliasdownloader2.util.Functions;

public class FunctionsTest {

	@Test
	public void testCreateTempFileInTempFolder() {
		
//		File f = Functions.createTempFileInTempFolder("hallo");
//		f = Functions.createTempFileInTempFolder("hallo.xml");
//		f = Functions.createTempFileInTempFolder("ha.llo.xml");
//		f = Functions.createTempFileInTempFolder("ha.llo..xml");
		
		
	}
	
	@Test
	public void testCleanFileName(){
		Assert.assertEquals("abcdefghi", Functions.cleanFileName("ab:c*?\"d<>e|\\fg/hi"));
	}

}
