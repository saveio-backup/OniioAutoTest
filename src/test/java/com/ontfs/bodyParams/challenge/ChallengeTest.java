package com.ontfs.bodyParams.challenge;

import com.ontfs.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class ChallengeTest extends TestBase {
	private Object[] invalidFileHash = ConstantUtil.invalidFileHash;
	private Object[] invalidServerAddress=ConstantUtil.invalidWallet;

	@Test
	public void testChallengeWithInvalidFileHash() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			JSONObject object = ChallengeUtils.challenge(clientUrl, invalidFileHash[i], serverAddressArray[1]);
			if(i<invalidFileHash.length-1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}
	
	@Test
	public void testChallengeWithInvalidServerAddress() {
		String fileHash=FileUtils.getFileHashByUploadFile(1,1,pwd);
		for (int i = 0; i < invalidServerAddress.length; i++) {
			JSONObject object=ChallengeUtils.challenge(clientUrl, fileHash, invalidServerAddress[i]);
			if(i<invalidServerAddress.length-1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}
	
	@Test
	public void testChallengeWithInvalidParams() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			for (int j = 0; j < serverAddressArray.length; j++) {
				JSONObject object = ChallengeUtils.challenge(clientUrl, invalidFileHash[i],invalidServerAddress[j]);
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
	@BeforeClass
	public void beforeClass(){
		SectorUtils.createSectorBeforeUploadFiles();
	}
}