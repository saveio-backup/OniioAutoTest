package com.ontfs.batch;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.ChallengeUtils;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.NodeUtils;
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

public class ChallengeBatchOperationTest extends TestBase {

	public static String preChallengeClientBalance;

	public static Map<String, Double> preChallengeNodeProfit = new HashedMap();

	public static Map<String, BigDecimal> nodeChallengeReward = new HashedMap();

	public static Map<String, BigDecimal> nodeJudgePunish = new HashedMap();

	// the second number is the sum of profit pledge
	public static Map<String, BigDecimal> preJudgeNodePP = new HashedMap();

	public static Object[][] judgeAddress = null;
	public static Object[][] challengeAddress = null;

	public static String preJudgeClientBalance;

	@Test(dataProvider = "getFilesName")
	public void testBatchUploadFiles(String fileName) {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ "and current fileName is :" + fileName);
		String filePath = uploadFilePath + "/" + fileName;
//		File file = new File(filePath);
		JSONObject obj = FileUtils.uploadFile(clientUrl,filePath,  new Date().getTime() + "-" +fileName ,pwd, expiredTime, copyNum, true,1);
		Assert.assertEquals(CommonUtils.getDesc(obj), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(obj).toString(), ConstantUtil.SUCCESS_CODE);
		String taskId = ((JSONObject) obj.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

		// verify upload success
		Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
		log.info("上传完成：" + taskId);
	}

	@Test(dataProvider = "getChallengeAddress")
	public void testBatchChalleng(String address) {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ "and current fileName is :" + address);

//		String b1 = NodeUtils.getAllNodeBalance(ontoUrl, new String[] { wallet })[0];
		String fileHash = address.split("-")[0];
		String serverAdd = address.split("-")[1];
		JSONObject object = ChallengeUtils.challenge(clientUrl, fileHash, serverAdd);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

		Assert.assertTrue(ChallengeUtils.verifyChallengeSuccess(clientUrl, fileHash, serverAdd));

	}

	@Test
	public void testChallengeClientCost() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// verify client cost
		if (challengeAddress != null) {
			BigDecimal clientCost = new BigDecimal(0);
			for (int i = 0; i < challengeAddress.length; i++) {
				String address = challengeAddress[i][0].toString();
				String fileHash = address.split("-")[0];
				String serverAdd = address.split("-")[1];
//               BigDecimal amount=ChallengeUtils.calcuteClientChallengeCost(clientUrl, serverAdd, fileHash,
//						challengeReward, gasFee);
//               log.info("clientChallengeCost 累加=" + amount);
//               clientCost=clientCost.add(amount);
				

			}

//			String postClientBalance = NodeUtils.getAllNodeBalance(ontoUrl, new String[] { wallet })[0];
//			BigDecimal postClientBalanceAmount = new BigDecimal(postClientBalance.split("-")[1]);
//			BigDecimal preClientBalanceAmount = new BigDecimal(preChallengeClientBalance.split("-")[1]);

//			log.info("Challenge后clientCost=" + clientCost);
//			log.info("Challenge后postClientBalanceAmount=" + postClientBalanceAmount);
//			log.info("Challenge后preClientBalanceAmount=" + preClientBalanceAmount);
//			Assert.assertEquals(postClientBalanceAmount.add(clientCost).setScale(9), preClientBalanceAmount.setScale(9));

		}
	}

	@Test
	public void testChallengeServerProfit() {
		// verify server cost

		if (challengeAddress != null) {
			for (int i = 0; i < challengeAddress.length; i++) {
				String address = challengeAddress[i][0].toString();
				String fileHash = address.split("-")[0];
				String serverAdd = address.split("-")[1];
//				nodeChallengeReward = ChallengeUtils.calcuteChallengeNodeReward(clientUrl, fileHash, serverAdd,
//						nodeChallengeReward, challengeReward, gasFee);
				
				 log.info("nodeChallengeReward="+nodeChallengeReward.toString());
				
			}

			JSONArray postNodeArray = NodeUtils.getNodeList(clientUrl,50).getJSONArray(ConstantUtil.RESULT);
			log.info("Challenge后验证方法nodeReward=" + nodeChallengeReward.toString());
			log.info("Challenge后验证方法的preNodeProfit=" + preChallengeNodeProfit.toString());
			log.info("Challenge后验证方法的postNodeArray=" + postNodeArray.toString());
			for (int i = 0; i < postNodeArray.size(); i++) {
				JSONObject node = postNodeArray.getJSONObject(i);
				String nodeAddr = node.getString(ConstantUtil.NODE_ADDR);
				double profit = node.getDoubleValue(ConstantUtil.PROFIT);
				Assert.assertEquals(profit, preChallengeNodeProfit.get(nodeAddr)
						+ nodeChallengeReward.get(nodeAddr).multiply(new BigDecimal(Math.pow(10, 9))).doubleValue(),10000000);
			}
		}

	}

	@Test(dataProvider = "getJudgeAddress")
	public void testBatchJudge(String addStr) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		if (!"no data".equals(addStr)) {

			String fileHash = addStr.split("-")[0];
			String nodeAddr = addStr.split("-")[1];
			JSONObject object = ChallengeUtils.judge(clientUrl, fileHash, nodeAddr);
			Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
			Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
			// verify judge success
			Assert.assertTrue(ChallengeUtils.verifyJudgeSuccess(clientUrl, fileHash, nodeAddr));

		}
	}

	@Test
	public void testJudgeClientrCost() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// verify client cost
		if (judgeAddress[0][0] != "no data") {
			BigDecimal clientJudgeCost = new BigDecimal(0);
			for (int i = 0; i < judgeAddress.length; i++) {
				String fileHash = judgeAddress[i][0].toString().split("-")[0];
				
//				BigDecimal clientCost=ChallengeUtils.calcuteJudgeClientCost(clientUrl, fileHash, challengeReward, gasFee);
//				clientJudgeCost = clientJudgeCost.add(clientCost);
//				log.info("每个judge的clientCost=" + clientCost);

			}

//			String postClientBalance = NodeUtils.getAllNodeBalance(ontoUrl, new String[] { wallet })[0];
//			BigDecimal postClientBalanceAmount = new BigDecimal(postClientBalance.split("-")[1]);
//			BigDecimal preClientBalanceAmount = new BigDecimal(preJudgeClientBalance.split("-")[1]);
//
//			log.info("Judge后clientCost=" + clientJudgeCost);
//			log.info("Judge后postClientBalanceAmount=" + postClientBalanceAmount);
//			log.info("Judge后preClientBalanceAmount=" + preClientBalanceAmount);
//
//			Assert.assertEquals(preClientBalanceAmount.add(clientJudgeCost).setScale(9), postClientBalanceAmount.setScale(9));
		}
	}

	@Test
	public void testJudegeServerCost() {
		// verify server Cost
		// verify client cost
		if (judgeAddress[0][0] != "no data") {
			for (int i = 0; i < judgeAddress.length; i++) {
				String fileHash = judgeAddress[i][0].toString().split("-")[0];
				log.info("fileHash="+fileHash);
				String nodeAddr = judgeAddress[i][0].toString().split("-")[1];
//				nodeJudgePunish = ChallengeUtils.calcuteJudgeNodePunish(clientUrl, fileHash, nodeAddr, nodeJudgePunish,
//						defaultPerBlockSize, gasPerKbForSaveWithFile, gasFee);
				log.info("节点每次审判数量nodeJudgePunish："+nodeJudgePunish.toString());
			}
			JSONArray postNodeArray = NodeUtils.getNodeList(clientUrl,50).getJSONArray(ConstantUtil.RESULT);
			log.info("Judge后验证方法nodeReward=" + nodeJudgePunish.toString());
			log.info("Judge后验证方法的preNodeProfit=" + preJudgeNodePP.toString());
			log.info("Judge后验证方法的postNodeArray=" + postNodeArray.toString());
			for (int i = 0; i < postNodeArray.size(); i++) {
				JSONObject node = postNodeArray.getJSONObject(i);
				String nodeAddr = node.getString(ConstantUtil.NODE_ADDR);
				String profit = node.getString(ConstantUtil.PROFIT);
				String pledge = node.getString(ConstantUtil.PLEDGE);
				String prePP = new BigDecimal(profit).add(new BigDecimal(pledge)).toString();
				BigDecimal punish = nodeJudgePunish.get(nodeAddr);
				Assert.assertEquals(preJudgeNodePP.get(nodeAddr).setScale(9),
						new BigDecimal(prePP).add(punish.multiply(new BigDecimal(10).pow(9))).setScale(9));
			}
		}

	}

	@DataProvider(name = "getJudgeAddress", parallel = true)
	public Object[][] getJudgeAddress() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		initJudge();
		List<String> addressList = ChallengeUtils.getJudgeAddress(clientUrl, copyNum);
		if (addressList.size() > 0) {
			int length = addressList.size();
			judgeAddress = new Object[length][1];
			for (int i = 0; i < length; i++) {
				String hash = addressList.get(i);
				log.info(addressList.get(i));
				judgeAddress[i][0] = hash;
				log.info("执行judge的文件：" + hash);
			}
		} else {
			judgeAddress = new Object[1][1];
			judgeAddress[0][0] = "no data";
		}
		return judgeAddress;
	}

	private void initJudge() {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

//		preJudgeClientBalance = NodeUtils.getAllNodeBalance(ontoUrl, new String[] { wallet })[0];

		log.info("init()后preJudgeClientBalance=" + preJudgeClientBalance);
		JSONArray nodeArray = NodeUtils.getNodeList(clientUrl,50).getJSONArray(ConstantUtil.RESULT);
		for (int i = 0; i < nodeArray.size(); i++) {
			JSONObject node = nodeArray.getJSONObject(i);
			nodeJudgePunish.put(node.getString(ConstantUtil.NODE_ADDR), new BigDecimal(0));
			String profit = node.getString(ConstantUtil.PROFIT);
			String pledge = node.getString(ConstantUtil.PLEDGE);
			preJudgeNodePP.put(node.getString(ConstantUtil.NODE_ADDR),
					new BigDecimal(profit).add(new BigDecimal(pledge)));

		}
		log.info("init()的nodeJudgePunish=" + nodeJudgePunish.toString());
		log.info("init()的nodePreJudgePP=" + preJudgeNodePP.toString());

	}

	@DataProvider(name = "getChallengeAddress", parallel = true)
	public Object[][] getChallengeAddress() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

//		initChallenge();

		List<String> addressList = ChallengeUtils.getChellangeAddress(clientUrl, copyNum);

		if (addressList.size() > 0) {
			int length = addressList.size();
			challengeAddress = new Object[length][1];
			for (int i = 0; i < length; i++) {
				String hash = addressList.get(i);
				log.info(addressList.get(i));
				challengeAddress[i][0] = hash;
				log.info("发起挑战的文件：" + hash);
			}
		}

		return challengeAddress;
	}

//	private Map<String, BigDecimal> initChallenge() {
//		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
//
////		preChallengeClientBalance = NodeUtils.getAllNodeBalance(ontoUrl, new String[] { wallet })[0];
//
//		log.info("init()后preChallengeClientBalance=" + preChallengeClientBalance);
//		JSONArray nodeArray = NodeUtils.getNodeList(clientUrl,50).getJSONArray(ConstantUtil.RESULT);
//		for (int i = 0; i < nodeArray.size(); i++) {
//			JSONObject node = nodeArray.getJSONObject(i);
//			nodeChallengeReward.put(node.getString(ConstantUtil.NODE_ADDR), new BigDecimal(0));
//			preChallengeNodeProfit.put(node.getString(ConstantUtil.NODE_ADDR), node.getDouble(ConstantUtil.PROFIT));
//
//		}
////		log.info("init()的nodeReward=" + nodeChallengeReward.toString());
//		log.info("init()的preNodeProfit=" + preChallengeNodeProfit.toString());
//		return nodeChallengeReward;
//	}



	@AfterClass
	public static void afterClass() {
		deleteFileAndTaskAndSpace(clientUrl);

	}

	@BeforeClass
	public static void BeforeClass() {
		deleteFileAndTaskAndSpace(clientUrl);

	}

}
