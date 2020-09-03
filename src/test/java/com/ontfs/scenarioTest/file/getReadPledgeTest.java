package com.ontfs.scenarioTest.file;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TestBase;

public class getReadPledgeTest extends TestBase {
	
	@Test
	public void testGetPledgeWithNoDownloadFile() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// Upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, pwd);
		//get pledge
		JSONObject object = FileUtils.getFileReadPledge(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);

	}
	
	@Test
	public void testGetPledgeAfterDeleteFile() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// Upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, pwd);
		
		//download file
		JSONObject object=FileUtils.downloadFile(clientUrl, fileHash, true, maxPeerCnt, downloadFileDirectory+"/"+System.currentTimeMillis(), pwd);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

		Assert.assertTrue(FileUtils.verifyDownloadSuccess(clientUrl, taskId));
		
		//delete file
		String[] array=new String[] {fileHash};
		JSON json=(JSON) JSON.toJSON(array);
		JSONObject deleteObject=FileUtils.deleteFiles(clientUrl, json);
		Assert.assertEquals(CommonUtils.getDesc(deleteObject), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(deleteObject), ConstantUtil.SUCCESS_CODE);
		Assert.assertTrue(FileUtils.verifyfDeleteAllFilesSuccess(clientUrl));
		
		
		//get pledge
		JSONObject pledgeObject=FileUtils.getFileReadPledge(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(pledgeObject), ConstantUtil.SUCCESS_CODE);
		
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
