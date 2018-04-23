package de.whiledo.jtablex;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

import lombok.val;



public class FunctionsX {

	private static volatile int uniqueNumber = 0;

	/**
	 * Gibt jedes Mal eine andere Zahl aus (Die maximale unterschiedliche Anzahl ist die Anzahl der Zahlen von Integer)
	 * @return den neuen int
	 */
	public static synchronized int getUniqueNumber(){
		uniqueNumber++;
		return uniqueNumber;
	}

	public static <T> void setComboBoxLayoutString(JComboBox<? super T> combobox, final ObjectDoInterface<T,String> objectToStringLayout){
		combobox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -5873933275078570244L;

			@SuppressWarnings("unchecked")
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel result = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				result.setOpaque(true);
				if(value != null) {
					result.setText(objectToStringLayout.doSomething((T) value));
				}
				return result;
			}
		});
	}



	public static void doWithAllSubcomponents(Component component, boolean doAlsoWithComponent, ComponentDoInterface componentDoInterface){
		if(doAlsoWithComponent){
			componentDoInterface.doAction(component);
		}

		if(component instanceof Container){

			for(Component subComp : ((Container) component).getComponents()){

				if(subComp instanceof Container){
					doWithAllSubcomponents(subComp, true, componentDoInterface);
				}else{
					componentDoInterface.doAction(subComp);
				}
			}
		}
	}

	public static void doWithAllSubcomponents(Component component, ComponentDoInterface componentDoInterface){
		doWithAllSubcomponents(component, true, componentDoInterface);
	}


	public static <T> DefaultComboBoxModel<T> generateDefaultComboBoxModel(List<T> data){
		DefaultComboBoxModel<T> comboboxmodel = new DefaultComboBoxModel<T>();
		for(T value : data){
			comboboxmodel.addElement(value);
		}
		return comboboxmodel;
	}


	public static <T> List<T> asList(@SuppressWarnings("unchecked") T... objects){
		List<T> list = new LinkedList<T>();
		for(T object : objects){
			list.add(object);
		}
		return list;
	}
	
	public static <T> T[] asArray(@SuppressWarnings("unchecked") T... objects){
		return objects;
	}
	
	
	public static <K, V> Map<K, V> asMap(K[] keys, V[] values){
		if(keys.length != values.length){
			throw new RuntimeException("keys and values do not have the same length");
		}
		
		val result = new HashMap<K, V>();
		for(int i=0; i < keys.length; i++){
			result.put(keys[i], values[i]);
		}
		return result;
	}
	
	public static Map<Object, Object> asMap(Object[]... keyValuePairs){
		return asMap(Object.class, Object.class, keyValuePairs);
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> asMap(Class<K> classKey, Class<V> classValue, Object[]... keyValuePairs){
		val result = new HashMap<K, V>();
		for(val keyValue : keyValuePairs){
			result.put((K) keyValue[0], (V) keyValue[1]);
		}
		return result;
	}

	/**
	 * @param s
	 * @return
	 */
	public static String md5(String s){
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			//new BigInteger(1, digest.digest()).toString(16);
			return md5DigestToString(digest.digest());

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		}
	}

	public static String md5(InputStream is){
		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");

			byte[] buffer = new byte[8192];
			int read = 0;

			while((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			is.close();

			return md5DigestToString(digest.digest());

		}catch(NoSuchAlgorithmException e){
			throw new RuntimeException("MD5 not supported", e);
		}catch(IOException e){
			throw new RuntimeException("IOException while reading", e);
		}
	}
	
	private static String md5DigestToString(byte[] digest) {
		StringBuffer result = new StringBuffer();
		String tmp;
		for(byte b : digest){
			tmp = Integer.toHexString(0xff & b);
			result.append(tmp.length() == 1 ? "0" + tmp : tmp);
		}
		
		return result.toString();
	}



	public static boolean isPrimitiveOrWrapper(Class<?> clazz){
		return clazz.isPrimitive() ||
				clazz.equals(Boolean.class) ||
				clazz.equals(Integer.class) ||
				clazz.equals(Byte.class) ||
				clazz.equals(Short.class) ||
				clazz.equals(Long.class) ||
				clazz.equals(Double.class) ||
				clazz.equals(Float.class) ||
				clazz.equals(Character.class);
	}



	public static boolean isPrimitiveOrString(Field field){
		return FunctionsX.isPrimitiveOrString(field.getType());
	}



	public static boolean isPrimitiveOrString(Class<?> clazz){
		return isPrimitiveOrWrapper(clazz) || clazz.equals(String.class);
	}



	public static Object getDefaultValue(Class<?> clazz){
		return isPrimitiveOrWrapper(clazz) ? (primitiveToWrapperClass(clazz) == Boolean.class ? false : new Byte((byte) 0)) : null;
	}
	
	public static Object[] getDefaultValues(Class<?>... classes){
		Object[] result = new Object[classes.length];
		for(int i=0; i < result.length; i++){
			result[i] = getDefaultValue(classes[i]);
		}
		return result;
	}


	public static Class<?> primitiveToWrapperClass(Class<?> anyClass){
		if(anyClass.isPrimitive()){
			if(anyClass.equals(boolean.class)){
				return Boolean.class;
			}else if(anyClass.equals(int.class)){
				return Integer.class;
			}else if(anyClass.equals(byte.class)){
				return Byte.class;
			}else if(anyClass.equals(long.class)){
				return Long.class;
			}else if(anyClass.equals(short.class)){
				return Short.class;
			}else if(anyClass.equals(double.class)){
				return Double.class;
			}else if(anyClass.equals(float.class)){
				return Float.class;
			}else if(anyClass.equals(char.class)){
				return Character.class;
			}
		}

		return anyClass;
	}

	public static List<Class<?>> primitivesToWrapperClasses(Class<?>... anyClasses){
		val list = new ArrayList<Class<?>>();
		for(val clazz : anyClasses){
			list.add(primitiveToWrapperClass(clazz));
		}
		return list;
	}
	
	
	public static boolean implementsInterface(Class<?> clazz, Class<?> interfaceClass){
		if(clazz.equals(interfaceClass)){
			return true;
		}
		
		for(val implementedInterface : clazz.getInterfaces()){
			if(implementedInterface.equals(interfaceClass) || implementsInterface(implementedInterface, interfaceClass)){
				return true;
			}
		}
		
		return false;
	}

	@SafeVarargs
	public static <T> List<T> newList(List<T> list, T... itemsToAdd){
		list = new LinkedList<T>(list);
		list.addAll(FunctionsX.asList(itemsToAdd));
		return list;
	}

	@SafeVarargs
	public static <T> List<T> concatLists(List<T>...lists) {
		val result = new LinkedList<T>();
		for(val l : lists) {
			result.addAll(l);
		}
		return result;
	}
	
	
	public static Class<?> getGenericClass(Field field){
		return getGenericClasses(field).get(0);
	}
	
	public static List<Class<?>> getGenericClasses(Field field){
		Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
		val result = new LinkedList<Class<?>>();
		for(int i=0; i < actualTypeArguments.length; i++){
			result.add((Class<?>) actualTypeArguments[i]);
		}
		
		return result;
	}
	
	public static boolean isArray(Object obj){
		val c = obj.getClass();
		return obj instanceof Object[] || 
				c.equals(boolean[].class) ||
				c.equals(char[].class) ||
				c.equals(byte[].class) ||
				c.equals(short[].class) ||
				c.equals(int[].class) ||
				c.equals(long[].class) ||
				c.equals(float[].class) ||
				c.equals(double[].class);
	}
	
	public static boolean isArrayOrCollection(Object obj){
		return obj instanceof Collection || isArray(obj);
	}
	
	
	public static Object[] primitiveArrayToWrapperArray(Object primitiveArray){
		Class<?> arrayClass = primitiveArray.getClass();
		
		if(arrayClass.equals(boolean[].class)){
			boolean[] castPrimitiveArray = (boolean[]) primitiveArray;
			Boolean[] wrapperArray = new Boolean[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
			
		}else if(arrayClass.equals(int[].class)){
			int[] castPrimitiveArray = (int[]) primitiveArray;
			Integer[] wrapperArray = new Integer[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
			
		}else if(arrayClass.equals(byte[].class)){
			byte[] castPrimitiveArray = (byte[]) primitiveArray;
			Byte[] wrapperArray = new Byte[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
			
		}else if(arrayClass.equals(long[].class)){
			long[] castPrimitiveArray = (long[]) primitiveArray;
			Long[] wrapperArray = new Long[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
			
		}else if(arrayClass.equals(short[].class)){
			short[] castPrimitiveArray = (short[]) primitiveArray;
			Short[] wrapperArray = new Short[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
			
		}else if(arrayClass.equals(double[].class)){
			double[] castPrimitiveArray = (double[]) primitiveArray;
			Double[] wrapperArray = new Double[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
			
		}else if(arrayClass.equals(float[].class)){
			float[] castPrimitiveArray = (float[]) primitiveArray;
			Float[] wrapperArray = new Float[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
			
		}else if(arrayClass.equals(char[].class)){
			char[] castPrimitiveArray = (char[]) primitiveArray;
			Character[] wrapperArray = new Character[castPrimitiveArray.length];
			
			for(int i=0; i < castPrimitiveArray.length; i++){
				wrapperArray[i] = castPrimitiveArray[i];
			}
			return wrapperArray;
		}
		
		return (Object[]) primitiveArray;
	}


	public static boolean arraysContainOneOrMoreEqualObjects(Object[] firstArray, Object[] secondArray){
		for(val o : firstArray){
			if(arrayContains(secondArray, o)){
				return true;
			}
		}
		return false;
	}

	public static boolean arrayContains(Object[] array, Object object){
		for(val o : array){
			if(o.equals(object)){
				return true;
			}
		}
		return false;
	}


	public static ImageIcon getImage(String dateinameMitPfad) {
		return new ImageIcon(FunctionsX.class.getResource(dateinameMitPfad));
	}

	public static String removeSuffix(String s, String suffix) {
		if(s.endsWith(suffix)){
			s = s.substring(0, s.length() - suffix.length());
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object, Class<T> classToCastTo){
		return (T) object;
	}


	

	public static int bigDecToInt(Object bigDecimal) throws Exception{
		if(bigDecimal == null){
			return 0;
		}

		if(bigDecimal instanceof Number){
			return ((Number) bigDecimal).intValue();
		}else{
			throw new Exception("Das Objekt ist kein Number, sondern \""+(bigDecimal!=null ? bigDecimal.getClass() : "bigDecimal ist null")+"\" ---- toString ergibt: "+(bigDecimal!=null ? bigDecimal.toString() : "bisDecimal ist leider null"));
		}
	}
	
	
	public static File getSystemTempDir(){
		return new File(System.getProperty("java.io.tmpdir"));
	}
	
	public static Icon getSystemIcon(String filename) throws IOException{
		File f = new File(getSystemTempDir().getAbsolutePath() + "/adzubixsystemicon." + getExtension(filename));

		if(!f.exists()){
			f.createNewFile();
			f.deleteOnExit();
		}
		return FileSystemView.getFileSystemView().getSystemIcon(f);
	}
	
	/**
	 * 
	 * @param name
	 * @return Dateinamenextension ohne Punkt
	 */
	public static String getExtension(String name) {

		if (name.lastIndexOf(".") != -1) {
			String extensionPossible = name.substring(name.lastIndexOf(".") + 1, name.length());
			if (extensionPossible.length() > 6) {//TODO hï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½????
				return "";
			} else {
				return extensionPossible;
			}
		} else
			return "";
	}
	
	
	
	
	//------------------------------------------------
	public static <T> boolean trueForAll(Collection<T> objectsToTest, ObjectDoInterface<T, Boolean> testingMethod){
		for(T o : objectsToTest){
			if(!testingMethod.doSomething(o)){
				return false;
			}
		}
		return true;
	}

	public static void copyToClipboard(String s){
		StringSelection selection = new StringSelection(s);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}


	public static File createTempFile(String name, String folder){
		File f = new File(getTempFolderAndCreateIt(folder) + name);
		f.deleteOnExit();
		return f;
	}



	@SuppressWarnings("unchecked")
	public static <T> T[] concatArrays(T[] a, T... b){
		Class<?> arrayClass = a.length > 0 ? a[0].getClass() : (b.length > 0 ? b[0].getClass() : Object.class);
		T[] result = (T[]) Array.newInstance(arrayClass, a.length + b.length);
		for(int i=0; i < result.length; i++){
			result[i] = i < a.length ? a[i] : b[i - a.length] ;
		}
		return result;
	}

	public static String replaceUmlaute(String s){
		return s.replaceAll("Ã¤", "ae").replaceAll("Ã¼", "ue").replaceAll("Ã¶", "oe")
				.replaceAll("Ã„", "Ae").replaceAll("Ãœ", "Ue").replaceAll("Ã–", "oe")
				.replaceAll("ÃŸ", "ss");
	}


	/**
	 * Formatiert ein Datum nach dem Muster "Wochentag, Tag und Uhrzeit", z.B. "Mi, 29.08.2012 19:00".
	 * Die Formatierung von Tag und Uhrzeit wird durch das simpleDateFormat-Objekt bestimmt.
	 * @param date
	 * @param simpleDateFormat
	 * @return formatierte Zeit als String
	 */
	public static String getWochentagAndTime(Date date, SimpleDateFormat simpleDateFormat){
		return getWochentag(date) + ", " + simpleDateFormat.format(date);
	}


	public static String getWochentag(Date date){
		val calendar = Calendar.getInstance();
		calendar.setTime(date);
		return new String[] {"So","Mo","Di","Mi","Do","Fr","Sa"} [calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public static byte[] getBytesFromFile(File file) throws IOException{
		byte[] bytes = null;

		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			in.close();
			throw new IOException("File is too large...fail...");
		}

		bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead=in.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			in.close();
			throw new IOException("fail while reading " + file.getName());
		}

		in.close();

		return bytes;
	}

//	public static void saveBytesInFileAndOpenTemporarily(InputStream bytes, String filename, String folder) throws IOException{
//
//		File f;
//		int i = 0;
//
//		do{
//			f = new File(getTempFolderAndCreateIt(folder) + i + filename);
//			i++;
//		}while(f.exists());
//
//		saveBytesInFile(bytes, f);
//		f.deleteOnExit();
//		Desktop.getDesktop().open(f);
//	}


//	public static Blob inputStreamToBlob(InputStream is){
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		try {
//			IOUtil.copyCompletely(is, out);
//			return new SerialBlob(out.toByteArray());
//		} catch (SQLException | IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	public static String getTempFolderAndCreateIt(String tempDirectory){
		File f = new File(tempDirectory);
		if(!f.exists()){
			f.mkdirs();
		}

		f.deleteOnExit();

		return tempDirectory;
	}

//	public static void saveBytesInFile(InputStream bytes, File destination) throws IOException{
//		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
//		//		out.write(bytes);
//		IOUtil.copyCompletely(bytes, out);
//		out.close();
//	}


	public static Date removeTimeFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
}
