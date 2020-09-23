package com.ontfs.bodyParams.file;

import com.ontfs.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeMethod;

import java.util.Date;

import org.bson.json.JsonMode;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class RenewFileTest extends TestBase {

	private Object[] invalidFileHash =ConstantUtil.invalidFileHash;
	private Object[] invalidTime = ConstantUtil.invalidTime;

	@Test
	public void testRenewFileWithInvalidTime() {
		String fileHash = FileUtils.getFileHashByUploadFile(1,1,pwd);
		for (int i = 0; i < invalidTime.length; i++) {
			JSONObject object = FileUtils.renewFile(clientUrl, fileHash, invalidTime[i]);
			if (i < invalidTime.length - 4) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}

	}

	@Test
	public void testRenewFileWithInvalidFileHash() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			JSONObject object = FileUtils.renewFile(clientUrl, invalidFileHash[i], 3600);
			if (i < invalidFileHash.length - 1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
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
