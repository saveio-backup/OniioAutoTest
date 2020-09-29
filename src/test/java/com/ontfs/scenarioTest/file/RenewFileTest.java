package com.ontfs.scenarioTest.file;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class RenewFileTest extends TestBase {
	
	@Test(groups  ="scenario")
	public void testRenewFileForSpaceFile() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// create space
		JSONObject object = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

		// upload space file
		String fileHash = FileUtils.getFileHashByUploadFile(0,1, pwd);
		JSONObject object2 = FileUtils.renewFile(clientUrl, fileHash, 3600);
		Assert.assertEquals(CommonUtils.getError(object2), ConstantUtil.INTERNAL_ERROR);

	}
	
	@Test(enabled = false,groups  ="scenario")
	public void testRenewFileThatUploadFailed() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// upload failed file
		String fileHash=FileUtils.getFileHashByUploadFile(1,1, pwd);
		JSONObject object = FileUtils.renewFile(clientUrl, fileHash, 3600);
		
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
