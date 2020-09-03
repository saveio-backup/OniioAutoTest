package com.ontfs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ontfs.paramBean.requestBean.ReqJson;

public class CommonUtils {
	private static Logger log = Logger.getLogger(CommonUtils.class);

	/**
	 * post method
	 * 
	 * @param url
	 * @param bodyParams
	 * @return
	 * @throws UnirestException
	 */
	static JSONObject postReqHttp(String url, String bodyParams) throws UnirestException {
		log.info("url is :" + url);
		HttpResponse<JsonNode> response = Unirest.post(url).header("Content-Type", "text/plain").body(bodyParams)
				.asJson();

		String res = response.getBody().toString();
		log.info("response.getBody is :" + res);

		return JSONObject.parseObject(res);
	}

	/**
	 * get desc
	 * 
	 * @param obj
	 * @return
	 */
	public static String getDesc(JSONObject obj) {
		return obj.get(ConstantUtil.DESC).toString();

	}

	/**
	 * get Error
	 * 
	 * @param obj
	 * @return
	 */
	public static String getError(JSONObject obj) {
		return obj.get(ConstantUtil.ERROR).toString();

	}

	/**
	 * get Result
	 * 
	 * @param obj
	 * @return
	 */
	public static JSONObject getResult(JSONObject obj) {
		return (JSONObject) obj.get(ConstantUtil.RESULT);

	}

	/**
	 * get method
	 * 
	 * @param url
	 * @return response.getBody().getObject()
	 * @throws UnirestException
	 */
	public static JSONObject getReqHttp(String url) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		String res = response.getBody().toString();
		log.info("response.getBody is :" + res);
		return JSONObject.parseObject(res);
	}

	/**
	 * get method,and return Desc
	 * 
	 * @param url
	 * @return Desc
	 * @throws UnirestException
	 */
	public static String getReqHttpDesc(String url) throws UnirestException {
		JSONObject obj = getReqHttp(url);
		String desc = obj.get("Desc").toString();
		return desc;
	}

	/**
	 * write privateKey to PrivateKey.dat
	 * 
	 * @param str
	 * @param privateKeyPath
	 * @return
	 * @throws IOException
	 */
	public static boolean writeStrToFile(String str, String privateKeyPath) throws IOException {
		File file = new File(privateKeyPath);
		// create file PrivateKey.dat
		ConfigUtils.createFile(file);

		// write privatekey to file
		CommonUtils.writeStringToFile(str, file);
		return true;
	}

	public static void sleep(long time) {
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the string to the file
	 * 
	 * @param str
	 * @param file
	 * @throws IOException
	 */
	public static void writeStringToFile(String str, File file) {
		byte bytes[] = new byte[1024];
		bytes = str.getBytes();
		int b = str.length();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bytes, 0, b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Read the contents of the file and write it to the string
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(String filePath) throws IOException {
		InputStream is = new FileInputStream(filePath);
		String line; // 用来保存每行读取的内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		line = reader.readLine(); // 读取第一行
		StringBuffer buffer = new StringBuffer();
		while (line != null) { // 如果 line 为空说明读完了
			buffer.append(line); // 将读到的内容添加到 buffer 中

			line = reader.readLine(); // 读取下一行
			if (line != null)
				buffer.append("\n"); // 添加换行符

		}
		reader.close();
		is.close();
		return buffer.toString();
	}

	/**
	 * get os info
	 * 
	 * @return
	 */
	public static boolean getWinOS() {
		Properties props = System.getProperties();
		String os = props.getProperty("os.name");
		if (os.contains("Windows")) {
			return true;
		} else {
			return false;
		}
	}

	public static long getTimeMillis(String s) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("格式化日期出错", e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long tim = cal.getTimeInMillis();
		return tim;
	}

	public static int parseInt(double d) {

		String str = String.valueOf(d);// 浮点变量a转换为字符串str
		int idx = str.lastIndexOf(".");// 查找小数点的位置
		String strNum = str.substring(0, idx);// 截取从字符串开始到小数点位置的字符串，就是整数部分
		int num = Integer.valueOf(strNum);// 把整数部分通过Integer.valueof方法转换为数字
		return num;
	}

	public static long formatTimeStampToHour(long timeStamp) {
		return timeStamp - timeStamp % (60 * 60);
	}

	public static String getBodyParams(String methodName, Object... object) {
		ReqJson reqJson = new ReqJson(methodName, object);

		return JSON.toJSONString(reqJson);
	}
}
