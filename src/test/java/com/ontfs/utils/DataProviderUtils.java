package com.ontfs.utils;

import org.testng.annotations.DataProvider;

public class DataProviderUtils {

	/**
	 * Test invalid taskid、filaHash
	 * 
	 * @return
	 */
	@DataProvider(name = "invalidString")
	public  Object[][] invalidString() {
		return new Object[][] { { " " }, { "" }, { "qweqweweqw" }, { "/!#^&*$\"#" }, { "花花" } };

	}

	/**
	 * test filePath
	 * 
	 * @return
	 */
	@DataProvider(name = "invaidStringFilePath")
	public  Object[][] invaidStringFilePath() {
		return new Object[][] { { " " }, { "" }, { "qweqweweqw" }, { "/!#^&*$\"#" }, { "缓缓" }, { "/home/ont/ontfs/" },
				{ "/home/ont/ontfs/log.exe" } };

	}

	/**
	 * sed to test data type errors
	 * @return
	 */
	@DataProvider(name = "invalidNumber")
	public  Object[][] invalidNumber() {
		return new Object[][] { { 12 } ,{-1}};
	}

	@DataProvider(name ="descIsNull")
	public  Object[][] descIsNull(){
		return new Object[][] { { "" } ,{" "}};
	}
	
	@DataProvider(name = "invalidTime")
	public static Object[][] invalidTime() {
		return new Object[][] { { -12 }, { 0 }, { 1.2 }, { System.currentTimeMillis() / 1000 - 1000 },
				{ System.currentTimeMillis() / 1000 + 3600 }, { System.currentTimeMillis() } };

	}

	@DataProvider(name = "invalidCopyNum")
	public static Object[][] invalidCopyNum() {
		return new Object[][] { { -1 }, { 0 }, { 0.1 }, { 11 } };
	}
}
