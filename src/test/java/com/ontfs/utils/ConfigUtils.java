package com.ontfs.utils;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

public class ConfigUtils {
	private static Logger log = Logger.getLogger(ConfigUtils.class);
	public static String ROOT_PATH = getRootPath();
	public final static String CONFIGFILE = "config.properties";
	public static String CONFIG_FILE_PATH = ROOT_PATH + "src/test/resources/";
	private static File configFile = new File(CONFIG_FILE_PATH + CONFIGFILE);
	private static String FILE_PATH = ROOT_PATH + "files/";
	private static Configuration config = getConfig(configFile);

	private static Configuration getConfig(File file) {
		log.info("config.properties path is:"+ file.getPath());
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class).configure(params.properties().setFile(file));
		try {
			config = builder.getConfiguration();

		} catch (ConfigurationException cex) {
		log.error("ConfigUtils的getConfig throw error",cex);
		}
		return config;
	}

	/**
	 * Get the path to the uploadfile
	 * 
	 * @param filename
	 * @return
	 */
	public static String getUploadFilePath(String filename) {
		return FILE_PATH + filename;
	}

	/*
	 * get attribute of config.properties
	 */
	public static String getConfig(String key) {
		log.info("=========The current method is getConfig");
		return config.getString(key);
	}

	/*
	 * get location such as : D:/eclipse-workspace/AutoTestSave/
	 */
	public static String getRootPath() {
        log.info("=========The current method is getRootPath");
		String prefix = "file:/";
		String suffix = "com/ontfs/utils/ConfigUtils.class";
		String path = ConfigUtils.class.getResource("ConfigUtils.class").toString();
		log.info("path=" + path);
		path = path.replaceFirst(prefix, "");

		path = path.replaceFirst("/target", "");
		path = path.replaceFirst("test-classes/" + suffix, "");

		log.info("path=" + path);
		log.info("is windows :"+isWindows(path));
		return isWindows(path) ? path : ("/" + path);
	}

	public static void updateProperties(String key, String value) throws IOException {

		Properties properties = new Properties();

		InputStream inputStream = new FileInputStream("src/test/resources/config.properties");

		properties.load(inputStream);

		// 先打印出未替换的wechatOAuthTokenUrl属性值
		String keyValue = properties.getProperty(key);// 获取修改前的值(Gets the property value before the modification)
		log.info(keyValue);

		// 使用MessageFormat对字符串进行参数替换
		// Gets the modified property value
		keyValue = MessageFormat.format(keyValue, new Object[] { value });
		log.info(keyValue);

	}

	private static boolean isWindows(String path) {
		return path.contains(":") ? true : false;
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					// log.info(files[i]);
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			log.info("file not exist" + '\n');
		}
	}

	/**
	 * create new file ,Files are deleted and created if they exist
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void createFile(File file) throws IOException {
		if (file.exists() && file.isFile()) {
			file.delete();
		} else {
			file.createNewFile();
		}
	}

	

	/**
	 * public static int getStrFromLog(String filename, String str){ int sum = 0;
	 * try{ File file = new File(filename); BufferedReader br = new
	 * BufferedReader(new FileReader(file)); String line; while(br.readLine() !=
	 * null){ line = br.readLine(); if(line.indexOf(str) !=-1){ sum++; } }
	 * }catch(Exception e){ e.printStackTrace(); } return sum; }
	 * 
	 * 
	 * public static boolean getWinOS(){ Properties props=System.getProperties();
	 * String os = props.getProperty("os.name"); if(os.contains("Windows")){ return
	 * true; }else{ return false; } }
	 * 
	 * 
	 * public static void copyFile(String oldPath, String newPath ){ try { int
	 * bytesum = 0; int byteread = 0; File oldfile = new File(oldPath); if
	 * (oldfile.exists()) { InputStream inStream = new FileInputStream(oldPath);
	 * FileOutputStream fs = new FileOutputStream(newPath); byte[] buffer = new
	 * byte[1444]; int length; while ( (byteread = inStream.read(buffer)) != -1) {
	 * bytesum += byteread; fs.write(buffer, 0, byteread); } inStream.close();
	 * fs.close(); } }catch (Exception e) { e.printStackTrace(); } }
	 * 
	 * 
	 * 
	 * public static String ParseStr(String strsrc, String str){ String substr = "";
	 * substr = strsrc.substring(strsrc.indexOf(str)+1, strsrc.length()-1); return
	 * substr;
	 * 
	 * }
	 **/
	public static void main(String[] args) {
		String tesstr = getConfig("node1.url");
		log.info(getRootPath());
		// String filename = "F:\\twnt\\bug\\tongweb6\\conf\\tongweb.xml";
		// setJSF(filename, "jsf=", "true");

	}

}
