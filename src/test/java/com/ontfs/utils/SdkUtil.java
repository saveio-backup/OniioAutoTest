package com.ontfs.utils;

import org.apache.log4j.Logger;
import org.omg.CORBA.OBJ_ADAPTER;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ontfs.paramBean.requestBean.StartSdkReqBean;
import com.ontfs.startsdk.StartSdkTest;

public class SdkUtil {
   
	private static Logger log = Logger.getLogger(SdkUtil.class);
	
	/**
	 * startSdk
	 * @param clientUrl
	 * @param ontoUrl
	 * @param gasPrice
	 * @param gasLimit
	 * @param pwd
	 * @param p2pListenAddr
	 * @param p2pNetworkId
	 * @param txTimeout
	 * @return
	 */
	public static JSONObject startSdk(String clientUrl,Object chainRpcAddr, Object walletPath, Object gasPrice, Object gasLimit, Object pdpVersion,
			Object walletPwd, Object p2pProtocol, Object p2pListenAddr, Object p2pNetworkId, Object txTimeout) {
		StartSdkReqBean startSdk=new StartSdkReqBean(chainRpcAddr, walletPath, gasPrice, gasLimit, pdpVersion, walletPwd, p2pProtocol, p2pListenAddr, p2pNetworkId, txTimeout);
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.START_SDK, startSdk);
		log.info("startSdk bodyParams is :"+ bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(clientUrl, bodyParams);
		} catch (UnirestException e) {
			log.error("启动客户端抛出异常", e);
		}
		return object;
	}
}
