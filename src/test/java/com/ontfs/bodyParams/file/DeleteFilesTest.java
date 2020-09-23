package com.ontfs.bodyParams.file;

import com.ontfs.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeMethod;

import java.util.Arrays;
import java.util.Stack;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;

public class DeleteFilesTest extends TestBase {
	private Object[] invalidFileHash = ConstantUtil.invalidFileHash;

	@Test(enabled = false)
	public void testDeleteFilesWithFileHashIsWrong() {
		for (int i = 0; i < invalidFileHash.length; i++) {

			Object[] array = new Object[] {invalidFileHash[i]};
			JSON json=(JSON) JSON.toJSON(array);
			JSONObject object = FileUtils.deleteFiles(clientUrl, json);
			if (i < invalidFileHash.length - 1) {

				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}

	}

	@Test(enabled = false)
	public void testDeleteFilesWithInvalidParams() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			for (int j = 0; j < invalidFileHash.length; j++) {
				Object[] array = new Object[] {invalidFileHash[i],invalidFileHash[j]};
				JSON json=(JSON) JSON.toJSON(array);
				JSONObject object = FileUtils.deleteFiles(clientUrl,json);
				if (i < invalidFileHash.length - 1 || j < invalidFileHash.length - 1) {
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
