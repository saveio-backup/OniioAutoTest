package com.ontfs.bodyParams.file;

import com.ontfs.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeMethod;


import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class GetFileInfoTest extends TestBase {

	private Object[] invalidFileHash =ConstantUtil.invalidFileHash;

	@Test(groups = "unusual")
	public void testGetFileInfoWithInvalidFileHash() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			JSONObject object = FileUtils.getFileInfo(clientUrl, invalidFileHash[i]);
			if (i == invalidFileHash.length - 1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			}
		}

	}
	
	@Test(groups = "normal")
	public void testGetFileInfoAfterDeleteFile() {
		String fileHash=FileUtils.getFileHashByUploadFile(1,1, pwd);
		JSONObject object=FileUtils.getFileInfo(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		
		//delete file
		String[] array=new String[] {fileHash};
		JSONObject delete=FileUtils.deleteFiles(clientUrl, JSON.toJSON(array));
		Assert.assertEquals(CommonUtils.getDesc(delete), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(delete).toString(), ConstantUtil.SUCCESS_CODE);
		
		JSONObject fileinfo=FileUtils.getFileInfo(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(fileinfo), ConstantUtil.INTERNAL_ERROR);
		
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}
	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		SectorUtils.createSectorBeforeUploadFiles();
	}

}
