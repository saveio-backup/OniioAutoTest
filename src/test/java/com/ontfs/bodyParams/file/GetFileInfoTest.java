package com.ontfs.bodyParams.file;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;


import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class GetFileInfoTest extends TestBase {

	private Object[] invalidFileHash =ConstantUtil.invalidFileHash;

	@Test
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
	
	@Test
	public void testGetFileInfoAfterDeleteFile() {
		String fileHash=FileUtils.getFileHashByUploadFile(1, pwd);
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
