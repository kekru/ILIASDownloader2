package de.whiledo.iliasdownloader2.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.parsers.SAXParserFactory;
//import javax.xml.transform.sax.SAXSource;
import org.simpleframework.xml.core.Persister;

public class Functions {

	private static final SimpleDateFormat simpleDateFormatIlias = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static final SimpleDateFormat simpleDateFormatReadable = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	public static Date iliasXmlStringToDate(String dateString){
		try {
			return simpleDateFormatIlias.parse(dateString);
		} catch (Exception e) {
			return new Date(System.currentTimeMillis());
		}
	}

	public static String formatDate(long date){
		return formatDate(new Date(date));
	}

	public static String formatDate(Date d){
		if(d == null){
			return "";
		}

		return simpleDateFormatReadable.format(d);
	}

	public static String cleanFileName(String name){
		return name.replaceAll("[:*?\"<>|\\\\/]","");
	}

	public static <T> T parseXmlObject(String input, Class<T> targetClass){
		try {
			return parseXmlObject(new ByteArrayInputStream(input.getBytes("UTF-8")), targetClass);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean useJAX = false;

	public static <T> T parseXmlObject(InputStream input, Class<T> targetClass){

//		if(useJAX){
//			try {
//				//			return (T) JAXBContext.newInstance(targetClass).createUnmarshaller().unmarshal(input);
//				JAXBContext jc = JAXBContext.newInstance(targetClass);
//
//				SAXParserFactory spf = SAXParserFactory.newInstance();
//				spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
//				spf.setFeature("http://xml.org/sax/features/validation", false);	        
//
//				SAXSource source = new SAXSource(spf.newSAXParser().getXMLReader(), new InputSource(input));
//
//				Unmarshaller unmarshaller = jc.createUnmarshaller();
//				return (T) unmarshaller.unmarshal(source);
//
//			} catch(javax.xml.bind.UnmarshalException e) {
//				throw new IliasException("Invalid Response", e);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}else{

			try {
				return new Persister().read(targetClass, input);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
//		}
	}

	
	public static void writeXmlObject(Object jaxbElement, File output){

//		if(useJAX){
//			try {			
//				Marshaller marshaller = JAXBContext.newInstance(jaxbElement.getClass()).createMarshaller();
//
//				String encoding = "UTF-8";
//				//			XMLStreamWriter xsw = XMLOutputFactory.newFactory().createXMLStreamWriter(new BufferedOutputStream(new FileOutputStream(output)), encoding);
//
//				//			xsw.writeStartDocument(encoding, "1.0");
//				//			marshaller.setListener(new MyMarshallerListener(xsw));
//				//			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
//				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
//				//			marshaller.marshal(jaxbElement, xsw);
//				marshaller.marshal(jaxbElement, output);
//				//			xsw.close();
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//
//		}else{

			try {
				new Persister().write(jaxbElement, output);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
//		}
	}


	public static File createTempFileInTempFolder(String fileName){
		String name = new File(fileName).getName();
		int indexOfDot = name.lastIndexOf(".");
		String prefix, suffix;

		if(indexOfDot != -1){
			prefix = name.substring(0, indexOfDot);
			suffix = name.substring(indexOfDot);
		}else{
			prefix = name;
			suffix = "";
		}

		try {
			File f = File.createTempFile(prefix, suffix);
			f.deleteOnExit();
			return f;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
