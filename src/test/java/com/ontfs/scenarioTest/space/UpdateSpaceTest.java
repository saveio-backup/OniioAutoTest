package com.ontfs.scenarioTest.space;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class UpdateSpaceTest extends TestBase {

	@Test
	public void testUpdateSpaceWhenSpaceNONExists() {

		// update space
		JSONObject object = SpaceUtils.updateSpace(clientUrl, volume, expiredTime + 3600);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
	}

	@Test
	public void testUpdateSpaceLessThanTheUsedSpace() {
		// create space
		JSONObject object = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

		// upload space file
		JSONObject uploadFile = FileUtils.uploadFile(clientUrl, uploadFilePath + "/tcnative-1.dll",
				"jdk-8u212-linux-x64.tar.gz", pwd, expiredTime, copyNum, true, 0);
		String taskId = ((JSONObject) uploadFile.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
		// verify upload file success
		Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));

		// update space
		JSONObject updateSpace = SpaceUtils.updateSpace(clientUrl, 1, expiredTime);
		Assert.assertEquals(CommonUtils.getError(updateSpace), ConstantUtil.INTERNAL_ERROR);

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
