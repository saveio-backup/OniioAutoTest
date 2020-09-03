package com.ontfs.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ChallengeUtils {
	private static Logger log = Logger.getLogger(ChallengeUtils.class);

	/**
	 * get the filehash and node address of the challenge
	 * 
	 * @param url
	 * @param copynum
	 * @return
	 */
	public static List<String> getChellangeAddress(String url, int copyNum) {
		// get file list
		JSONObject fileList = FileUtils.getFileList(url);
		Object resultObj = fileList.get(ConstantUtil.RESULT);
		List<String> strList = new ArrayList<String>();
		if (resultObj != null) {
			// Query whether the number of PDP submissions is equal to copynum
			JSONArray fileHashArray = (JSONArray) resultObj;
//JSONArray uploadTaskArray=TaskUtils.getAllUploadTaskList(url).getJSONArray("Result");

			for (int i = 0; i < fileHashArray.size(); i++) {
				getAddressFromPdpInfo(url, fileHashArray.getString(i), strList, copyNum);
			}

		}
		return strList;
	}

	/**
	 * get filehash and nodeAddress from pdpinfo
	 * 
	 * @param url
	 * @param fileHash
	 * @param address
	 * @return
	 */
	public static List<String> getAddressFromPdpInfo(String url, String fileHash, List<String> address, int copyNum) {
		JSONObject pdpinfo = FileUtils.getPdpinfoList(url, fileHash);
		Object resultArray = pdpinfo.get(ConstantUtil.RESULT);

		if (((JSONArray) resultArray).size()<=copyNum
				&& ((JSONArray) resultArray).size() >0) {
			JSONArray array = (JSONArray) resultArray;
			for (int i = 0; i < array.size(); i++) {
				String serverAddress = array.getJSONObject(i).getString(ConstantUtil.NODE_ADDR);
				address.add(fileHash + "-" + serverAddress);
			}
		}
		return address;

	}

	/**
	 * send challenge to server
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param serverAddress
	 * @return
	 */
	public static JSONObject challenge(String clientUrl, Object fileHash, Object serverAddress) {
		
		String bodyParams=CommonUtils.getBodyParams(ConstantUtil.CHALLENGE, fileHash,serverAddress);
		log.info("challenge bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(clientUrl, bodyParams);
		} catch (UnirestException e) {
			log.error("发起挑战报错", e);
		}
		return object;
	}

	/**
	 * get challenge list
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @return
	 */
	public static JSONObject getChallengeList(String clientUrl, Object fileHash) {
		String bodyParams=CommonUtils.getBodyParams(ConstantUtil.GET_CHALLENGE_LIST, fileHash);
		log.info("getChallengeList bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(clientUrl, bodyParams);
		} catch (UnirestException e) {
			log.error("获取挑战报错", e);
		}
		return object;
	}

	/**
	 * verify node profit
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param nodeAddr
	 * @param preNodelist
	 * @return
	 */
	/*public static Map<String, BigDecimal> calcuteChallengeNodeReward(String clientUrl, String fileHash, String nodeAddr,
			Map<String, BigDecimal> nodeChallengeReward, double challengeReward, double gasFee) {

		// calcute challenge reward
		BigDecimal reward = calcuteFileReward(clientUrl, fileHash, challengeReward, gasFee);
		JSONObject challengeInfo = getChallenge(clientUrl, fileHash, nodeAddr);
		int challengeState = challengeInfo.getIntValue(ConstantUtil.STATE);
		if (challengeState == 3) {
			BigDecimal value = nodeChallengeReward.get(nodeAddr);
			nodeChallengeReward.replace(nodeAddr, value, reward.add(value));
		} *//*
			 * else { Assert.assertEquals(challengeInfo.getDoubleValue(ConstantUtil.REWARD),
			 * reward.multiply(new BigDecimal(Math.pow(10, 9)))); }
			 *//*
		return nodeChallengeReward;
	}*/

	/**
	 * get the challenge by fileHash and nodeAddr
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param nodeAddr
	 * @return
	 */
	private static JSONObject getChallenge(String clientUrl, String fileHash, String nodeAddr) {
		JSONObject object = getChallengeList(clientUrl, fileHash);
		JSONArray challengeArray = object.getJSONArray(ConstantUtil.RESULT);
		for (int i = 0; i < challengeArray.size(); i++) {
			JSONObject challenge = (JSONObject) challengeArray.get(i);

			if (fileHash.equals(challenge.getString(ConstantUtil.FILEHASH))
					&& nodeAddr.equals(challenge.getString(ConstantUtil.NODE_ADDR))) {
				return challenge;
			}
		}
		return null;
	}

	/**
	 * Verify the success of the challenge
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param nodeAddr
	 * @return
	 */
	public static boolean verifyChallengeSuccess(String clientUrl, String fileHash, String nodeAddr) {
		JSONObject challengeList = getChallengeList(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(challengeList), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(challengeList), ConstantUtil.SUCCESS);
		JSONArray resultArray = challengeList.getJSONArray(ConstantUtil.RESULT);
		if (resultArray.size() > 0) {
			for (int i = 0; i < resultArray.size(); i++) {
				JSONObject object = (JSONObject) resultArray.get(i);
				int state = object.getIntValue(ConstantUtil.STATE);
				if (fileHash.equals(object.getString(ConstantUtil.FILEHASH))
						&& nodeAddr.equals(object.getString(ConstantUtil.NODE_ADDR))) {
					if (state == 3) {
						return true;
					} else if (state == 1) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							log.error("休眠抛出异常", e);
						}
						return verifyChallengeSuccess(clientUrl, fileHash, nodeAddr);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Calculate client challenge cost
	 * 
	 * @param clientUrl
	 * @param ontoUrl
	 * @param fileHash
	 * @param nodeAddr
	 * @param preClientBalance
	 * @return
	 */
	/*public static BigDecimal calcuteClientCostWhenChallengeSuccess(String clientUrl, String fileHash, double challengeReward, double gasFee) {
		BigDecimal reward = calcuteFileReward(clientUrl, fileHash,challengeReward , gasFee);
		BigDecimal result = reward.add(new BigDecimal(Double.toString(gasFee)));
		log.info("奖励金加上手续费=" + result);
		log.info("filehash=" + fileHash);
		return result;

	}*/

	/**
	 * calcute challenge reward
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @return
	 */
	/*public static BigDecimal calcuteFileReward(String clientUrl, String fileHash,double challengeReward,double gasFee) {
		JSONObject fileInfo = FileUtils.getFileInfo(clientUrl, fileHash);
		JSONObject result = fileInfo.getJSONObject(ConstantUtil.RESULT);

		BigDecimal reward = new BigDecimal(Double.toString(challengeReward)).add(new BigDecimal(Double.toString(gasFee)));
	
		log.info("reward=" + reward.doubleValue());
		return reward;
	}*/

	/**
	 * get judge Address
	 * 
	 * @param clientUrl
	 * @return
	 */
	public static List<String> getJudgeAddress(String clientUrl, int copyNum) {
		// get getChallenge Address
		List<String> addressList = getChellangeAddress(clientUrl, copyNum);
		List<String> judgeAddressList = new ArrayList<String>();
		if (addressList.size() > 0) {
			for (int i = 0; i < addressList.size(); i++) {
				String addressStr = addressList.get(i);
				String fileHash = addressStr.split("-")[0];
				if (!judgeAddressList.toString().contains(fileHash)) {
					// get challenge list
					JSONObject object = getChallengeList(clientUrl, fileHash);
					JSONArray array = object.getJSONArray(ConstantUtil.RESULT);
					if(array!= null) {
					for (int j = 0; j < array.size(); j++) {
						JSONObject challengeInfo = array.getJSONObject(j);
						if (challengeInfo.getInteger(ConstantUtil.STATE) == 2) {
							String nodeAddr = challengeInfo.getString(ConstantUtil.NODE_ADDR);
							judgeAddressList.add(fileHash + "-" + nodeAddr);
						}
					}
					}
				}
			}
		}
		return judgeAddressList;
	}

	/**
	 * judge
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param nodeAddr
	 * @return
	 */
	public static JSONObject judge(String clientUrl, Object fileHash, Object nodeAddr) {
		
		String bodyParams=CommonUtils.getBodyParams(ConstantUtil.JUDGE, fileHash,nodeAddr);
		log.info("judge bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(clientUrl, bodyParams);
		} catch (UnirestException e) {
			log.error("请求合约判定报错", e);
		}
		return object;
	}

	/**
	 * verify judge success
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param nodeAddr
	 * @return
	 */
	public static boolean verifyJudgeSuccess(String clientUrl, String fileHash, String nodeAddr) {
		// get challenge list of the fileHash
		JSONObject object = getChallengeList(clientUrl, fileHash);
		JSONArray array = object.getJSONArray(ConstantUtil.RESULT);
		for (int i = 0; i < array.size(); i++) {
			JSONObject challenge = array.getJSONObject(i);
			if (fileHash.equals(challenge.getString(ConstantUtil.FILEHASH))
					&& nodeAddr.equals(challenge.getString(ConstantUtil.NODE_ADDR))) {
				if (challenge.getInteger(ConstantUtil.STATE) == 0) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * verify node punish in judge
	 * 
	 * @param clientUrl
	 * @param fileHash
	 * @param nodeAddr
	 * @param preNodeArray
	 * @param reward
	 * @return
	 */

	/*public static Map<String, BigDecimal> calcuteJudgeNodePunish(String clientUrl, String fileHash, String nodeAddr,
			Map<String, BigDecimal> nodeJudgePunish, int blockSize, int gasPerKb, double gasFee) {
//		BigDecimal reward = calcuteFileReward(clientUrl, fileHash, blockSize, gasPerKb, gasFee);
//		double value = nodeJudgePunish.get(nodeAddr);
//		nodeJudgePunish.replace(nodeAddr, value, reward.add(new BigDecimal(Double.toString(value))).doubleValue());

		JSONObject fileInfo = FileUtils.getFileInfo(clientUrl, fileHash);
		JSONObject result = fileInfo.getJSONObject(ConstantUtil.RESULT);
		String payAmountStr = result.get(ConstantUtil.PAY_AMOUNT).toString().split(" ")[0];
		BigDecimal value = nodeJudgePunish.get(nodeAddr);
		nodeJudgePunish.replace(nodeAddr,value,value.add(new BigDecimal(payAmountStr).add(new BigDecimal(Double.toString(gasFee)).multiply(new BigDecimal(2)))));
		
		return nodeJudgePunish;
	}*/

	/**
	 * verify client cost in judge
	 * 
	 * @param ontoUrl
	 * @param clientAddr
	 * @param preClientBalance
	 * @param reward
	 * @return
	 */
	/*public static BigDecimal calcuteJudgeClientCost(String clientUrl, String fileHash, double challengeReward,
			double gasFee) {
		BigDecimal reward = calcuteFileReward(clientUrl, fileHash, challengeReward , gasFee);
		JSONObject fileInfo = FileUtils.getFileInfo(clientUrl, fileHash);
		JSONObject result = fileInfo.getJSONObject(ConstantUtil.RESULT);
		String payAmountStr = result.get(ConstantUtil.PAY_AMOUNT).toString().split(" ")[0];
		
		return reward.add(new BigDecimal(payAmountStr)).add(new BigDecimal(Double.toString(gasFee)));
	}*/

	/**
	 * calcute Client Challenge Cost
	 * @param clientUrl
	 * @param serverAdd
	 * @param fileHash
	 * @param clientChallengeCost
	 * @param defaultPerBlockSize
	 * @param gasPerKbForSaveWithFile
	 * @param gasFee
	 * @return
	 */
	/*public static BigDecimal calcuteClientChallengeCost(String clientUrl, String serverAdd, String fileHash,
		double	challengeReward, double gasFee) {
		JSONObject challengeList = getChallengeList(clientUrl, fileHash);
		JSONArray resultArray = challengeList.getJSONArray(ConstantUtil.RESULT);
		if (resultArray.size() > 0) {
			for (int i = 0; i < resultArray.size(); i++) {
				JSONObject object = (JSONObject) resultArray.get(i);
				int state = object.getIntValue(ConstantUtil.STATE);
				if (state == 3) {
					BigDecimal cost=ChallengeUtils.calcuteClientCostWhenChallengeSuccess(clientUrl,
							fileHash,challengeReward , gasFee);
					log.info("挑战成功="+fileHash+" cost="+cost);
					return cost;

				} else if(state ==4) {
					JSONObject fileInfo = FileUtils.getFileInfo(clientUrl, fileHash);
					JSONObject result = fileInfo.getJSONObject(ConstantUtil.RESULT);
					String payAmountStr = result.get(ConstantUtil.PAY_AMOUNT).toString().split(" ")[0];
					log.info("挑战失败="+fileHash+" payAmount="+ new BigDecimal(payAmountStr).negate());
					return   new BigDecimal(payAmountStr).negate();
				}
			}
		}
		 return null;

	}*/

}
