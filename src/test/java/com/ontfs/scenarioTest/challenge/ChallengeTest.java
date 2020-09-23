package com.ontfs.scenarioTest.challenge;

import java.util.Map;

import com.ontfs.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.paramBean.responseBean.Task;

public class ChallengeTest extends TestBase {

	/**
	 * Challenge nodes that don't store files
	 */
	@Test
	public void testChallengeNodeThatNoStoreFile() {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);
		// get pdp list
		JSONObject object = FileUtils.getPdpinfoList(clientUrl, fileHash);
		JSONArray pdpArray = object.getJSONArray(ConstantUtil.RESULT);
		// get node list
		JSONObject nodeObject = NodeUtils.getNodeList(clientUrl, 20);
		JSONArray nodeArray = nodeObject.getJSONArray(ConstantUtil.RESULT);

		for (int i = 0; i < pdpArray.size(); i++) {
			String pdpAddr = pdpArray.getJSONObject(i).getString(ConstantUtil.NODE_ADDR);
			for (int j = 0; j < nodeArray.size(); j++) {
				String nodeAddr = nodeArray.getJSONObject(j).getString(ConstantUtil.NODE_ADDR);
				if (StringUtils.equals(pdpAddr, nodeAddr)) {
					nodeArray.remove(j);
				}

			}
		}
		// get client balance
//		JSONObject beforeClientBlanceObject = NodeUtils.getBalance(ontoUrl, wallet);
		// get node profit
//		Map<String, Double> beforeNodeProfit = NodeUtils.getNodeProfit(clientUrl);

		// send challenge
		JSONObject challengeObject = ChallengeUtils.challenge(clientUrl, fileHash,
				nodeArray.getJSONObject(0).getString(ConstantUtil.NODE_ADDR));
		Assert.assertEquals(CommonUtils.getError(challengeObject), ConstantUtil.INTERNAL_ERROR);
		// get client balance
//		JSONObject afterClientBlanceObject = NodeUtils.getBalance(ontoUrl, wallet);
		// get node profit
		Map<String, Double> afterNodeProfit = NodeUtils.getNodeProfit(clientUrl);
//
//		Assert.assertTrue(NodeUtils.verifyServerProfitCorrect(beforeNodeProfit, afterNodeProfit));

//		Assert.assertEquals(CommonUtils.getResult(beforeClientBlanceObject).getString(ConstantUtil.ONG),
//				CommonUtils.getResult(afterClientBlanceObject).getString(ConstantUtil.ONG));

	}

	@Test
	public void testChallengeNodeWithUploadFaileFile() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// upload failed file
		Object fileName= getFilesName()[0][0];
		String filePath=uploadFilePath + "/" +fileName;
		
//		File file = new File(uploadFilePath + "/" + getFilesName()[0][0]);
		JSONObject uploadObject = FileUtils.uploadFile(clientUrl, filePath,fileName.toString(), pwd, expiredTime, 50, true, 1,1);
		Assert.assertEquals(CommonUtils.getError(uploadObject), ConstantUtil.SUCCESS_CODE);

		Assert.assertEquals(CommonUtils.getDesc(uploadObject), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(uploadObject).toString(), ConstantUtil.SUCCESS_CODE);
		String taskId = ((JSONObject) uploadObject.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
		// get task
		Task task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
				.toJavaObject(Task.class);

		int progress = task.TaskBaseInfo.Progress;
		while (progress != ConstantUtil.UPLOAD_ERROR) {
			task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
					.toJavaObject(Task.class);
			progress = task.TaskBaseInfo.Progress;
			sleep(2000);

		}

		String fileHash = task.TaskBaseInfo.FileHash;
		Assert.assertEquals(progress, ConstantUtil.UPLOAD_ERROR);

		// send challenge
		for (int j = 0; j < serverAddressArray.length; j++) {
			JSONObject object = ChallengeUtils.challenge(clientUrl, fileHash, serverAddressArray[j]);
			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
		}

	}

	@Test
	public void testSecondChallengeWhenFirstChallengeNoReply() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);

		// getPdplist
		JSONObject pdpObject = FileUtils.getPdpinfoList(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(pdpObject), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(pdpObject), ConstantUtil.SUCCESS);
		String nodeAddr = pdpObject.getJSONArray(ConstantUtil.RESULT).getJSONObject(0)
				.getString(ConstantUtil.NODE_ADDR);

		// send challenge
		JSONObject object = ChallengeUtils.challenge(clientUrl, fileHash, nodeAddr);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);

		JSONArray challengeList = ChallengeUtils.getChallengeList(clientUrl, fileHash)
				.getJSONArray(ConstantUtil.RESULT);
		if (challengeList.getJSONObject(0)
				.getIntValue(ConstantUtil.STATE) == ConstantUtil.CHALLENGE_NO_REPLY_AND_VALID) {
			// send second challenge
			JSONObject obJsonObject = ChallengeUtils.challenge(clientUrl, fileHash, nodeAddr);
			Assert.assertEquals(CommonUtils.getError(obJsonObject), ConstantUtil.INTERNAL_ERROR);
		}
	}

	@Test(enabled = false)
	public void testDeleteFileWhenChallengeNoReply() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// Upload file success
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);

		// getPdplist
		JSONObject pdpObject = FileUtils.getPdpinfoList(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(pdpObject), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(pdpObject), ConstantUtil.SUCCESS);
		String nodeAddr = pdpObject.getJSONArray(ConstantUtil.RESULT).getJSONObject(0)
				.getString(ConstantUtil.NODE_ADDR);

		// send challenge
		JSONObject object = ChallengeUtils.challenge(clientUrl, fileHash, nodeAddr);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);

		JSONArray challengeList = ChallengeUtils.getChallengeList(clientUrl, fileHash)
				.getJSONArray(ConstantUtil.RESULT);
		// getBalance
//		long preBlance = CommonUtils.getResult(NodeUtils.getBalance(ontoUrl, wallet)).getLongValue(ConstantUtil.ONG);
		if (challengeList.getJSONObject(0)
				.getIntValue(ConstantUtil.STATE) == ConstantUtil.CHALLENGE_NO_REPLY_AND_VALID) {

			String[] array = new String[] { fileHash };
			JSON hashJson = (JSON) JSON.toJSON(array);
			JSONObject deleteObject = FileUtils.deleteFiles(clientUrl, hashJson);
			Assert.assertEquals(CommonUtils.getError(deleteObject), ConstantUtil.SUCCESS_CODE);
			Assert.assertEquals(CommonUtils.getDesc(deleteObject), ConstantUtil.SUCCESS);
			Assert.assertTrue(FileUtils.verifyfDeleteAllFilesSuccess(clientUrl));
//			long postBlance = CommonUtils.getResult(NodeUtils.getBalance(ontoUrl, wallet))
//					.getLongValue(ConstantUtil.ONG);
//
//			if (postBlance > preBlance) {
//				Assert.assertTrue(true);
//			} else {
//				Assert.assertTrue(false);
//			}

		}
	}

	@BeforeMethod
	public void beforeMethod() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

	@AfterClass
	public void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}
	@BeforeClass
	public void beforeClass(){
		SectorUtils.createSectorBeforeUploadFiles();
	}

}
