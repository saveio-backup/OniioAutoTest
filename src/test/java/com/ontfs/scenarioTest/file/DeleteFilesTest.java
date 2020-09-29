package com.ontfs.scenarioTest.file;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;

public class DeleteFilesTest extends TestBase {

	@Test(groups  ="scenario")
	public void testDeleteFilesWithOneInvalidFiles() {
		// upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);

		//delete files
		String[] array = new String[] { fileHash, "qweqweqwe" };
		JSON json = (JSON) JSON.toJSON(array);
		JSONObject object = FileUtils.deleteFiles(clientUrl, json);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		Assert.assertNotNull(CommonUtils.getResult(object));
		
		Assert.assertTrue(FileUtils.verifyfDeleteAllFilesSuccess(clientUrl));

	}

	@Test(enabled = false,groups  ="scenario")
	public void testDeleteTheSameFileTwice() {
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);
		// first delete
		String[] fileHashArray = new String[] { fileHash };
		Object json = JSON.toJSON(fileHashArray);

		JSONObject object = FileUtils.deleteFiles(clientUrl, json);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

		// second delete

		JSONObject object1 = FileUtils.deleteFiles(clientUrl, json);
		Assert.assertEquals(CommonUtils.getError(object1), ConstantUtil.INTERNAL_ERROR);

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

}
