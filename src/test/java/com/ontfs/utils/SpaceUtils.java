package com.ontfs.utils;

import java.io.File;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SpaceUtils {

	private static Logger log = Logger.getLogger(SpaceUtils.class);

	/**
	 * create space
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject createSpace(String url, Object volume, Object copyNum, Object pdpInterval, Object expiredTime) {
		
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.CREATE_SPACE,volume,copyNum,expiredTime);
		log.info("createSpace bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("创建空间报错", e);
		}
		return object;
	}

	/**
	 * calculate space cost
	 * 
	 * @param urlRoot
	 * @param copynum
	 * @param pdpinterval
	 * @param expiredtime
	 * @return
	 */
	/*public static boolean calculateSpaceCost(String urlRoot, int volume, int copynum, int pdpinterval,
			long expiredtime) {
		// get space info
		JSONObject spaceInfo = getSpaceInfo(urlRoot);
		JSONObject result = (JSONObject) spaceInfo.get(ConstantUtil.RESULT);
		String startTimeStr = result.get(ConstantUtil.TIME_START).toString().substring(0, 19);
		long startTime = CommonUtils.getTimeMillis(startTimeStr) / 1000;
		int currFeeRate=result.getIntValue(ConstantUtil.CURRFEERATE);
		String str = result.get(ConstantUtil.PAY_AMOUNT).toString().split(" ")[0];

		BigDecimal bgPay = new BigDecimal(str);
		BigDecimal bg = new BigDecimal(10);

		long payAmount = bgPay.multiply(bg.pow(9)).longValue();

		long intervalMinute = (CommonUtils.formatTimeStampToHour(expiredtime)
				- CommonUtils.formatTimeStampToHour(startTime)) / 60/60;
		long expiredCost = intervalMinute * copynum * volume/ 256 * currFeeRate;

		log.info("payAmount=" + payAmount);
		log.info("expiredCost=" + expiredCost);
		if (expiredCost == payAmount) {
			return true;
		}
		return false;
	}*/

	/**
	 * get space info
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject getSpaceInfo(String url) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_SPACE_INFO);
		log.info("getSpaceInfo bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("获取空间信息报错", e);
		}
		return object;
	}
	
	/**
	 * delete space
	 * 
	 * @param urlRoot
	 * @return
	 */
	public static JSONObject deleteSpace(String urlRoot) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.DELETE_SPACE);
		
		log.info("deleteSpace bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(urlRoot, bodyParams);
		} catch (UnirestException e) {
			log.error("删除空间信息报错", e);
		}
		return object;
	}

	

	/**
	 * update space
	 * 
	 * @param clientUrl
	 * @param volume
	 * @param expiredTime
	 * @return
	 */
	public static JSONObject updateSpace(String clientUrl, Object volume, Object expiredTime) {
		

		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.UPDATE_SPACE,volume,expiredTime);
		
		log.info("updateSpace bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(clientUrl, bodyParams);
		} catch (UnirestException e) {
			log.error("耿欣空间信息报错", e);
		}
		return object;
	}
	
	/**
	 * verify delete space success
	 * @param url
	 * @return
	 */
	 public static boolean verifydeleteSpaceSuccess(String url) {
		 JSONObject space=getSpaceInfo(url);
		 String object=CommonUtils.getDesc(space);
		 if(object.contains("no space")) {
			 return true;
		 }else {
			 return false;
		 }
	 }
	
}
