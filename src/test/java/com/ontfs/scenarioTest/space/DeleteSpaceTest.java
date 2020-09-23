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

public class DeleteSpaceTest extends TestBase {

	@Test
	public void testDeleteSpaceWhenNoSpace() {
		JSONObject object = SpaceUtils.deleteSpace(clientUrl);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
	}

	@Test
	public void testDeleteSpaceWhenExitNotExpiredFile() {
		// create space
	
		JSONObject object = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

		// upload space file
//		File file = new File(uploadFilePath + "/jdk-8u212-linux-x64.tar.gz");
		JSONObject uploadFile = FileUtils.uploadFile(clientUrl, uploadFilePath + "/" +
						"tcnative-1.dll","jdk-8u212-linux-x64.tar.gz" , pwd, expiredTime, copyNum, true,
				0,1);
		String taskId = ((JSONObject) uploadFile.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
		// verify upload file success
		Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
		
		
		//delete space
		JSONObject delete=SpaceUtils.deleteSpace(clientUrl);
		Assert.assertEquals(CommonUtils.getError(delete), ConstantUtil.INTERNAL_ERROR);

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
