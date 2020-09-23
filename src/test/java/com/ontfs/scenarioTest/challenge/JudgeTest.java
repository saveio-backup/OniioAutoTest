package com.ontfs.scenarioTest.challenge;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.ChallengeUtils;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class JudgeTest extends TestBase {

	@Test
	public void testJudgeWhenNoChallenge() {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);
		// get pdp list
		JSONObject pdpList = FileUtils.getPdpinfoList(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(pdpList), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(pdpList), ConstantUtil.SUCCESS);

		String nodeAddr = pdpList.getJSONArray(ConstantUtil.RESULT).getJSONObject(0).getString(ConstantUtil.NODE_ADDR);

		JSONObject object = ChallengeUtils.judge(clientUrl, fileHash, nodeAddr);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);

	}

	@Test
	public void testJudgeWhenChallengeNoReply() {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);
		// get pdp list
		JSONObject pdpList = FileUtils.getPdpinfoList(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(pdpList), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(pdpList), ConstantUtil.SUCCESS);

		String nodeAddr = pdpList.getJSONArray(ConstantUtil.RESULT).getJSONObject(0).getString(ConstantUtil.NODE_ADDR);

		// send challenge
		JSONObject challenge = ChallengeUtils.challenge(clientUrl, fileHash, nodeAddr);
		Assert.assertEquals(CommonUtils.getError(challenge), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(challenge), ConstantUtil.SUCCESS);
		// get challenge list
		JSONArray challengeList = ChallengeUtils.getChallengeList(clientUrl, fileHash)
				.getJSONArray(ConstantUtil.RESULT);
		if (challengeList.getJSONObject(0)
				.getIntValue(ConstantUtil.STATE) == ConstantUtil.CHALLENGE_NO_REPLY_AND_VALID) {
			// judge
			JSONObject object = ChallengeUtils.judge(clientUrl, fileHash, nodeAddr);
			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
		}
	}

	@Test
	public void testJudgeWhenTheFileWasDeleted() {
		
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);
		// get pdp list
		JSONObject pdpList = FileUtils.getPdpinfoList(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(pdpList), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(pdpList), ConstantUtil.SUCCESS);

		String nodeAddr = pdpList.getJSONArray(ConstantUtil.RESULT).getJSONObject(0).getString(ConstantUtil.NODE_ADDR);

		// send challenge
		JSONObject challenge = ChallengeUtils.challenge(clientUrl, fileHash, nodeAddr);
		Assert.assertEquals(CommonUtils.getError(challenge), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(challenge), ConstantUtil.SUCCESS);
		
		//delete file
		String[] array=new String[] {fileHash};
		JSON arrayJson=(JSON) JSON.toJSON(array);
		JSONObject delete=FileUtils.deleteFiles(clientUrl, arrayJson);
		Assert.assertEquals(CommonUtils.getError(delete), ConstantUtil.SUCCESS_CODE);
		Assert.assertEquals(CommonUtils.getDesc(delete), ConstantUtil.SUCCESS);
		
		// judge
		JSONObject object = ChallengeUtils.judge(clientUrl, fileHash, nodeAddr);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
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

}
