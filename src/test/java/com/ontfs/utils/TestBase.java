package com.ontfs.utils;

import java.io.File;
//import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import com.alibaba.fastjson.JSONArray;
import com.mashape.unirest.http.exceptions.UnirestException;


public class TestBase {
	protected static Logger log = Logger.getRootLogger();
	protected static String clientUrl = "";
	protected  static String[] serverUrlArray;

	protected static String[] serverAddressArray;
	protected static String ontoUrl = "";
	protected static String p2pListenAddr = "";
	protected static long p2pNetworkId = 0;
	protected static int txTimeout = 0;
	protected static String p2pProtocol="";
	protected static int pdpVersion=0;
	protected static String walletPath="";

	protected static String pwd = "";
	protected static long expiredTime;
	protected static String wallet = "";
	protected static String wallet2 = "";
	protected static int pdpInterval = 0;
	protected static int copyNum = 0;
	protected static boolean inorder = false;
	protected static int maxPeerCnt = 0;
	protected static String downloadFileDirectory = "";
	protected static String decryFileDirectory = "";
	protected static int volume = 0;
	protected static int defaultPerBlockSize = 0;
	protected static int gasPerKbForSaveWithFile = 0;
	protected static int gasPerKbForSaveWithSpace = 0;
	protected static double gasFee = 0;
	protected static int gasLimit = 0;
	protected static int gasPrice = 0;
	protected static double challengeReward;

	protected static String fileUrl = "";
	protected static String uploadFileUrl = "";
	protected static String downloadFileUrl = "";

	protected static String uploadFilePath = "";
	protected static String filesPath = "";

	protected static String getUploadTaskinfoByIdUrl = "";

	public TestBase() {
		super();
	}

/*	public void setUp() {
		init();
	}*/

	public  void init()  {
		//// 自动快速地使用缺省Log4j环境
		BasicConfigurator.configure();
		log.info("==================start init ...");
        clientUrl = ConfigUtils.getConfig("client.url");
        log.info ("clientUrl"+clientUrl);
		String configFilePath = ConfigUtils.CONFIG_FILE_PATH;
		log.info ("configFilePath"+configFilePath);

		serverUrlArray=ConfigUtils.getConfig("server.url").split(",");

		uploadFilePath = ConfigUtils.getConfig("uploadfile.path");
		wallet = ConfigUtils.getConfig("client.address");
		wallet2 = ConfigUtils.getConfig("client2.address");
		expiredTime = getExpiredTime(Calendar.SECOND, 93600) / 1000;
		pdpInterval = Integer.parseInt(ConfigUtils.getConfig("pdpinterval"));
		copyNum = Integer.parseInt(ConfigUtils.getConfig("copynum"));
		inorder = Boolean.parseBoolean(ConfigUtils.getConfig("inorder"));
		maxPeerCnt = Integer.parseInt(ConfigUtils.getConfig("maxPeerCnt"));
		downloadFileDirectory = ConfigUtils.getConfig("downloadFilePath");
		decryFileDirectory = ConfigUtils.getConfig("decryFilePath");
		volume = Integer.parseInt(ConfigUtils.getConfig("volume"));
		serverAddressArray = ConfigUtils.getConfig("server.address").split(",");
		ontoUrl = ConfigUtils.getConfig("onto.url");
		pwd = ConfigUtils.getConfig("pwd");
		defaultPerBlockSize = Integer.parseInt(ConfigUtils.getConfig("defaultPerBlockSize"));
		gasPerKbForSaveWithFile = Integer.parseInt(ConfigUtils.getConfig("gasPerKbForSaveWithFile"));
		gasPerKbForSaveWithSpace = Integer.parseInt(ConfigUtils.getConfig("gasPerKbForSaveWithSpace"));
		gasLimit = Integer.parseInt(ConfigUtils.getConfig("gasLimit"));
		gasPrice = Integer.parseInt(ConfigUtils.getConfig("gasPrice"));
		gasFee = (gasLimit * gasPrice) / Math.pow(10, 9);
		challengeReward = Double.parseDouble(ConfigUtils.getConfig("challengeReward"));
		p2pListenAddr = ConfigUtils.getConfig("p2pListenAddr");
		p2pNetworkId = Long.parseLong(ConfigUtils.getConfig("p2pNetworkId"));
		txTimeout = Integer.parseInt(ConfigUtils.getConfig("txTimeout"));
		p2pProtocol = ConfigUtils.getConfig("p2pProtocol");
		walletPath = ConfigUtils.getConfig("walletPath");
		pdpVersion = Integer.parseInt(ConfigUtils.getConfig("pdpVersion"));
	}

	private static long getExpiredTime(int filed, int amount) {
		Calendar tomorrow = Calendar.getInstance();
//		tomorrow.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		tomorrow.add(filed, amount);
		Date time = tomorrow.getTime();
		return time.getTime();
	}

	@DataProvider(name = "errorData")
	public Object[][] errorData() {

		return new Object[][] { { "" }, { " " }, { pwd + "123" }, null };
	}

	@DataProvider(name = "nameIsNull")
	public Object[][] nameIsNull() {

		return new Object[][] { { "" }, { " " } };
	}

	@DataProvider(name = "pwdIsNull")
	public Object[][] pwdIsNull() {

		return new Object[][] { { "" } };
	}

	@DataProvider(name = "walletIsWrong")
	public Object[][] walletIsWrong() {

		return new Object[][] { { "" }, { "123" }, { "  " } };
	}

	@DataProvider(name = "pwdIsWrong")
	public Object[][] pwdIsWrong() {

		return new Object[][] { { "" }, { pwd + "123" } };
	}

	@DataProvider(name = "filePathIsWrong")
	public Object[][] filePathIsWrong() {

		return new Object[][] { { "" }, { "D:\\wallet.dat" }, { "D:\\test" }, { "/home/test" } };
	}

//	@DataProvider(name = "getFilesPath", parallel = true)
	public Object[][] getFilesPath(String filePath) {
//		自动获取已知文件夹下的文件路径，前提是自动化程序与文件夹在同一机器，但是jenkins（10.0.1.128）与seeker（10.0.1.129）不再一个机器上，所以以下代码不能使用
		File uploadFile = new File(filePath);

		if (uploadFile.isDirectory()) {
			String[] files = uploadFile.list();
			Object[][] objArray = new Object[files.length][1];
			for (int i = 0; i < files.length; i++) {
				log.info(files[i]);
				objArray[i][0] = filePath + "/" + files[i];
			}
			return objArray;
		} else {
			return new Object[][] {};
		}
	}

	@DataProvider(name = "getFilesName", parallel = true)
	public static Object[][] getFilesName() {
		// 将自动获取改为写死的路径

		
		  return new Object[][] { { "1.png" }, { "2.png" }, {
		  "2019-11-21_15.10.56_LOG.log" }, { "go1.13.4.linux-amd64.tar.gz" }, {
		  "init.sh" }, { "jdk-8u212-linux-x64.tar.gz" }, { "tcnative-1.dll" }, {
		  "Untitled-1.json" }, { "uploadJson.txt" }, { "vcredist_x64.exe" }, {
		  "VMware-VMvisor-Installer-6.7.0.update01-10302608.x86_64.iso" }, {
		  "wallet.dat" }, { "soft.zip" } };
		 
		/*
		 * File filePath = new File(uploadFilePath);
		 * 
		 * if (filePath.isDirectory()) { File[] files = filePath.listFiles(); Object[][]
		 * objArray = new Object[files.length][1]; for (int i = 0; i < files.length;
		 * i++) { log.info("上传文件："+files[i].getName()); objArray[i][0] =
		 * files[i].getName(); } return objArray; } else { return new Object[][] {}; }
		 */
	
	}

	/**
	 * get uploaded files hash
	 * 
	 * @return
	 * @throws UnirestException
	 */
	@DataProvider(name = "getdownloadFilesStr", parallel = true)
	public Object[][] getdownloadFilesStr() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		JSONArray fileHashList = FileUtils.getDwonloadFile(clientUrl, copyNum);
		Object[][] fileurls = null;
		if (fileHashList != null) {
			int length = fileHashList.size();
			fileurls = new Object[length][1];
			for (int i = 0; i < length; i++) {
				String hash = (String) fileHashList.get(i);
				log.info(fileHashList.get(i));
				fileurls[i][0] = hash;
				log.info("批量下载文件：" + hash);
			}
		}

		return fileurls;

	}

	protected void sleep(long time) {
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFileAndTaskAndSpace(String url) {
		TaskUtils.deleteAllTasks(url);

		// verify delete all task success
		Assert.assertTrue(TaskUtils.verifyDeleteTasksSuccess(url));
	    FileUtils.deleteAllFiles(url);

		// verify delete All files success
		Assert.assertTrue(FileUtils.verifyfDeleteAllFilesSuccess(url));
		
		JSONObject deletespace=SpaceUtils.deleteSpace(url);

		Assert.assertTrue(SpaceUtils.verifydeleteSpaceSuccess(url));
	}

	public static void main(String[] args) {
		log.info(ConfigUtils.getConfig("account.name"));
	}

}
