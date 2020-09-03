package com.ontfs.scenarioTest.file;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.paramBean.responseBean.Task;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;

import java.io.File;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;

public class ChangeOwnerTest extends TestBase {
	
	@Test
	public void testChangeOwnerForSpaceFile() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// create space
		JSONObject space = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(space), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(space), ConstantUtil.SUCCESS_CODE);

		// upload space file
		String fileHash = FileUtils.getFileHashByUploadFile(0, pwd);

		// change space file owner
		JSONObject object = FileUtils.changeFileOwner(clientUrl, fileHash, wallet2);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
	}

	@Test
	public void testChangeOwnerWithOriginalOwner() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String fileHash=FileUtils.getFileHashByUploadFile(1, pwd);
		
		JSONObject object=FileUtils.changeFileOwner(clientUrl, fileHash, wallet);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		
	}
	
	@Test(enabled = false)
	public void testChangeOwnerForFailedFileUpload() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		//upload file fail
		Object fileName=getFilesName()[0][0];
		String filePath=uploadFilePath+"/"+fileName;
//		File file=new File(uploadFilePath+"/"+getFilesName()[0][0]);
		JSONObject object=FileUtils.uploadFile(clientUrl, filePath,fileName, pwd, expiredTime, copyNum+12, true, 1);
		String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
		// get task
		Task task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
				.toJavaObject(Task.class);

		int progress = task.TaskBaseInfo.Progress;
		while (progress != 8) {
			task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
					.toJavaObject(Task.class);
			progress = task.TaskBaseInfo.Progress;
			sleep(2000);

		}
		
		String fileHash=task.TaskBaseInfo.FileHash;
		Assert.assertEquals(progress, ConstantUtil.UPLOAD_ERROR);
		
		//change fileowner
		JSONObject changeObject=FileUtils.changeFileOwner(clientUrl, fileHash, wallet2);
		
		Assert.assertEquals(CommonUtils.getError(changeObject), ConstantUtil.INTERNAL_ERROR);
		
		
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
