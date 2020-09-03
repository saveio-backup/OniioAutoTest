package com.ontfs.scenarioTest.file;

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
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;

public class UploadFileTest extends TestBase {

	@Test
	public void testUploadSpaceFileWhenNoSpace() {
		// upload space File
//		File file=new File(uploadFilePath+"/"+getFilesName()[0][0]);
		Object fileName=getFilesName()[0][0];
		JSONObject object=FileUtils.uploadFile(clientUrl, uploadFilePath+"/"+fileName, fileName.toString(), pwd, expiredTime, copyNum, true, 0);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
	}
	
	@Test
	public void testUploadFileSizeGreaterThanSpaceVolume() {
		//create space
		JSONObject object=SpaceUtils.createSpace(clientUrl, 256, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		
		//upload file
		
//		File file=new File(uploadFilePath+"/soft.zip");
		JSONObject uploadObject=FileUtils.uploadFile(clientUrl, uploadFilePath+"/soft.zip", "soft.zip", pwd, expiredTime, copyNum, true, 0);
		Assert.assertEquals(CommonUtils.getDesc(uploadObject), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(uploadObject).toString(), ConstantUtil.SUCCESS_CODE);
		String taskId = ((JSONObject) uploadObject.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

		// verify upload success
		Assert.assertFalse(FileUtils.verifyUploadSuccess(clientUrl, taskId));
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
