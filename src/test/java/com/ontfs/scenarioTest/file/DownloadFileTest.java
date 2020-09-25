package com.ontfs.scenarioTest.file;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.paramBean.responseBean.Task;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class DownloadFileTest extends TestBase{
 
	@Test
	public void testDownloadNonEncrptedFileWithEncrypt() {
		String fileHash = FileUtils.getFileHashByUploadFile(1,1);
		JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, inorder, maxPeerCnt,
				downloadFileDirectory + "/" + System.currentTimeMillis(), pwd);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		String taskId = CommonUtils.getResult(object).getString(ConstantUtil.TASK_ID);

		Assert.assertFalse(FileUtils.verifyDownloadSuccess(clientUrl, taskId));
		JSONObject obj = TaskUtils.getDownloadTaskinfoByTaskId(clientUrl, taskId);
		Task task = CommonUtils.getResult(obj).toJavaObject(Task.class);
		Assert.assertEquals(task.TaskBaseInfo.Progress, ConstantUtil.DOWNLOAD_ERROR);
		Assert.assertTrue(task.TaskBaseInfo.ErrorInfo.contains("no fs nodes avaiable when ask for download"));
	}

	@Test
	public void testTwoDownloadWithSameFileName() {
		String fileHash = FileUtils.getFileHashByUploadFile(1,1);
		//first download
		String downloadPath=downloadFileDirectory + "/"+System.currentTimeMillis();
		JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, inorder, maxPeerCnt,
				downloadPath, "");
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		String taskId = CommonUtils.getResult(object).getString(ConstantUtil.TASK_ID);
		Assert.assertTrue(FileUtils.verifyDownloadSuccess(clientUrl, taskId));

		// second download
		JSONObject object2=FileUtils.downloadFile(clientUrl, fileHash, inorder, maxPeerCnt,
				downloadPath, "");
		Assert.assertEquals(CommonUtils.getError(object2), ConstantUtil.INTERNAL_ERROR);
		
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
