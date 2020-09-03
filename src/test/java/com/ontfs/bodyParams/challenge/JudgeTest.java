package com.ontfs.bodyParams.challenge;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.ChallengeUtils;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class JudgeTest extends TestBase{


  private Object[] invalidFileHash = ConstantUtil.invalidFileHash;
	private Object[] invalidServerAddress=ConstantUtil.invalidWallet;

	@Test
	public void testJudgeWithInvalidFileHash() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			JSONObject object = ChallengeUtils.judge(clientUrl, invalidFileHash[i], serverAddress[1]);
			if(i<invalidFileHash.length-1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}
	
	@Test
	public void testJudgeWithInvalidServerAddress() {
		String fileHash=FileUtils.getFileHashByUploadFile(1,pwd);
		for (int i = 0; i < invalidServerAddress.length; i++) {
			JSONObject object=ChallengeUtils.judge(clientUrl, fileHash, invalidServerAddress[i]);
			if(i<invalidServerAddress.length-1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}
	
	@Test
	public void testJudgeWithInvalidParams() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			for (int j = 0; j < serverAddress.length; j++) {
				JSONObject object = ChallengeUtils.judge(clientUrl, invalidFileHash[i],invalidServerAddress[j]);
				if(i<invalidFileHash.length-1 &&j<invalidServerAddress.length-1 ) {
					Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
				} else {
					Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
				}
			}
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

}