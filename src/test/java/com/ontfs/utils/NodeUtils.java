package com.ontfs.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;

public class NodeUtils {
	private static Logger log = Logger.getLogger(NodeUtils.class);

//	public static String[] getAllNodeBalance(String url, String[] address) {
//		String[] addressBalance = new String[address.length];
//		for (int i = 0; i < address.length; i++) {
//			JSONObject balance = getBalance(url, address[i]);
//			String amount = ((JSONObject) balance.get(ConstantUtil.RESULT)).getString(ConstantUtil.ONG);
//			BigDecimal bgAmount = new BigDecimal(amount);
//			BigDecimal bg = new BigDecimal(10);
////			BigDecimal t=bg.pow(-9);
//			BigDecimal doubleAmount = bgAmount.divide(bg.pow(9));
//			addressBalance[i] = address[i] + "-" + doubleAmount;
//		}
//		return addressBalance;
//	}

	/**
	 * get balance
	 * 
	 * @param url
	 * @param address
	 * @return
	 */
/*	public static JSONObject getBalance(String url,String address) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_BALANCE, address);
		log.info("getBalance bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("获取余额信息报错", e);
		}
		return object;
	}*/

	/**
	 * get node list
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject getNodeList(String url, Object...limit) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_NODE_LIST, limit);
		log.info("getNodeList bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("获取server节点信息报错", e);
		}
		return object;
	}

	/**
	 * calculate server profit
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param blockSize
	 * @param gasPerKb
	 * @return
	 */
	public static double calculateOneFileProfitForNode(String clientUrl, String fileHash, int blockSize, int gasPerKb) {
		JSONObject fileObject = FileUtils.getFileInfo(clientUrl,fileHash);
		JSONObject fileinfo = fileObject.getJSONObject(ConstantUtil.RESULT);

		long currentTime = new Date().getTime() / 1000;
		String startTimeStr = fileinfo.get(ConstantUtil.TIME_START).toString().substring(0, 19);
		String endTimeStr = fileinfo.get(ConstantUtil.TIME_EXPIRED).toString().substring(0, 19);
		long startTime = CommonUtils.getTimeMillis(startTimeStr) / 1000;
		long endTime = CommonUtils.getTimeMillis(endTimeStr) / 1000;

		if (currentTime < endTime) {
			endTime = currentTime;
		}

		String str = fileinfo.get(ConstantUtil.PAY_AMOUNT).toString().split(" ")[0];
		int blockCount = (Integer) fileinfo.get(ConstantUtil.FILE_BLOCK_COUNT);

		long intervalMinute = (CommonUtils.formatTimeStampToHour(endTime)
				- CommonUtils.formatTimeStampToHour(startTime)) / 60;
		long fileModePerServerProfit = intervalMinute * blockCount * fileinfo.getIntValue(ConstantUtil.CURRFEERATE);
		return fileModePerServerProfit;

	}

	/**
	 * get server profit
	 * 
	 * @param clientUrl
	 * @return
	 */
	public static Map<String, Double> getNodeProfit(String clientUrl) {
		Map<String, Double> nodeProfit = new HashedMap();
		JSONArray nodeArray = NodeUtils.getNodeList(clientUrl,50).getJSONArray(ConstantUtil.RESULT);
		for (int i = 0; i < nodeArray.size(); i++) {
			JSONObject node = nodeArray.getJSONObject(i);
			nodeProfit.put(node.getString(ConstantUtil.NODE_ADDR), node.getDouble(ConstantUtil.PROFIT));

		}

		return nodeProfit;
	}

	/**
	 * Calculate the profit of all file for the node
	 * 
	 * @param clientUrl
	 * @param serverProfit
	 * @param blockSize
	 * @param gasPerKb
	 * @return
	 */
//	public static Map<String, Double> calculateAllFileProfitForNode(String clientUrl, Map<String, Double> serverProfit,
//			int blockSize, int gasPerKb) {
//
//		JSONObject FileListObject = FileUtils.getFileList(clientUrl);
//		JSONArray fileArray = FileListObject.getJSONArray(ConstantUtil.RESULT);
//		if(fileArray!=null) {
//		for (int i = 0; i < fileArray.size(); i++) {
//			double profit = calculateOneFileProfitForNode(clientUrl, fileArray.getString(i), blockSize, gasPerKb);
//			JSONArray pdpinfo = FileUtils.getPdpinfoList(clientUrl, fileArray.getString(i))
//					.getJSONArray(ConstantUtil.RESULT);
//			for (int j = 0; j < pdpinfo.size(); j++) {
//				String nodeAddr = pdpinfo.getJSONObject(j).getString(ConstantUtil.NODE_ADDR);
//				double value = serverProfit.get(nodeAddr);
//				serverProfit.replace(nodeAddr, value + profit);
//			}
//		}
//		}
//		return serverProfit;
//	}

	/**
	 * verify Server Profit Correct
	 * 
	 * @param expiredServerProfit
	 * @param postServerProfit
	 * @return
	 */
	public static boolean verifyServerProfitCorrect(Map<String, Double> expiredServerProfit,
			Map<String, Double> postServerProfit) {
		for (Map.Entry<String, Double> entry : expiredServerProfit.entrySet()) {
			String nodeAddr = entry.getKey();
			double expirePrifit = entry.getValue();
			double postProfit = postServerProfit.get(nodeAddr);
			if (expirePrifit > postProfit) {
				return false;
			}
		}
		return true;
	}

	public static JSONObject queryNode(String clientUrl, String nodeAddr) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.QUERY_NODE, nodeAddr);
		log.info("queryNode bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(clientUrl, bodyParams);
		} catch (UnirestException e) {
			log.error("查询节点信息抛出异常", e);
		}
		return object;
	}

	/**
	 * detail service
	 * @param clientUrl
	 * @return
	 */
	public static JSONObject detailService(String clientUrl) {
		
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.DETAIL_SERVICE);
		log.info("detailService bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(clientUrl, bodyParams);
		} catch (UnirestException e) {
			log.error("查询节点详细服务抛出异常", e);
		}
		return object;
	}

}
