package com.ontfs.bodyParams.file;

import java.io.File;
import java.util.Date;

import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.paramBean.responseBean.Task;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.DataProviderUtils;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

public class UploadFileTest extends TestBase {

	private Object[] invalidFilePath = ConstantUtil.invalidFilePath;

	private Object[] invalidExpiredTime = ConstantUtil.invalidExpiredTime;

	private Object[] invalidCopynum = ConstantUtil.invalidCopyNum;;

	private Object[] invalidStorageType = ConstantUtil.invalidStorageType;

	private Object[] validCopyNum = ConstantUtil.validCopyNum;
	
	private Object[] validStorageType=ConstantUtil.validStorageType;

	@Test(dataProvider = "invaidStringFilePath", dataProviderClass = DataProviderUtils.class)
	public void testUploadFileWithFilePathIsWrong(String filePath) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " filepath是" + filePath);

		JSONObject object = FileUtils.uploadFile(clientUrl, filePath,
				new Date().getTime() + "-" + System.currentTimeMillis(), pwd, expiredTime, copyNum, true, 1);
		verifyUploadFileError(object);
	}

	@Test(dataProvider = "invalidNumber", dataProviderClass = DataProviderUtils.class)
	public void testUploadFileWithFilePathTypeIsWrong(Object filePath) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " filepath是" + filePath);
		JSONObject object = FileUtils.uploadFile(clientUrl, filePath, new Date().getTime(), pwd, expiredTime, copyNum,
				true, 1);
		verifyUploadFileError(object);
	}

	@Test(dataProvider = "descIsNull", dataProviderClass = DataProviderUtils.class)
	public void testUploadFileWithInvalidDesc(Object desc) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", desc, pwd, expiredTime,
				copyNum, true, 1);
		if (desc == " ") {
			String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
			// verify upload success
			Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
		} else {
			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
		}
	}

	@Test
	public void testUploadFileWithDescIsChinese() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "花花", pwd, expiredTime,
				copyNum, true, 1);
		String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
		// verify upload success
		Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));

		JSONObject taskObject = TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId);
		Task uploadTask = taskObject.getJSONObject(ConstantUtil.RESULT).toJavaObject(Task.class);
		// get file info
		JSONObject fileinfoObject = FileUtils.getFileInfo(clientUrl, uploadTask.TaskBaseInfo.FileHash);
		String desc = fileinfoObject.getJSONObject(ConstantUtil.RESULT).getString(ConstantUtil.FILE_DESC);
		Assert.assertEquals(desc, "花花");

	}

	@Test
	public void testUploadFileWithInvaildExpiredTime() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		for (int i = 0; i < invalidExpiredTime.length; i++) {
			JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
					invalidExpiredTime[i], copyNum, true, 1);

			verifyUploadFileError(object);

		}
	}

	@Test
	public void testUploadFileWithInvaildCopyNum() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		for (int i = 0; i < invalidCopynum.length; i++) {
			JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
					expiredTime, invalidCopynum[i], true, 1);
			verifyUploadFileError(object);
		}
	}

	@Test
	public void testUploadFileWithValidCopyNum() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		for (int i = 0; i < validCopyNum.length; i++) {
			JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
					expiredTime, validCopyNum[i], true, 1);
			Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
			AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
			String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

			// verify upload success
			Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
		}
	}

	@Test
	public void testUploadFileWithInvaildStorageType() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		for (int i = 0; i < invalidStorageType.length; i++) {
			JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
					expiredTime, copyNum, true, invalidStorageType[i]);
			verifyUploadFileError(object);
		}
	}
	
	@Test
	private void testUploadFileWithVaildStorageType() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		//create sapce
		JSONObject obj = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(obj), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(obj), ConstantUtil.SUCCESS_CODE);
		for (int i = 0; i < validStorageType.length; i++) {
			JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
					expiredTime, copyNum, true, validStorageType[i]);
			Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
			AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
			String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

			// verify upload success
			Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
		}
	}

	private void verifyUploadFileError(JSONObject object) {
		String error = CommonUtils.getError(object);

		if (ConstantUtil.SUCCESS_CODE.equals(error)) {
			String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
			// get task
			Task task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
					.toJavaObject(Task.class);

			int progress = task.TaskBaseInfo.Progress;
			while (progress != ConstantUtil.UPLOAD_ERROR) {
				task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
						.toJavaObject(Task.class);
				progress = task.TaskBaseInfo.Progress;
				sleep(2000);

			}
			Assert.assertEquals(progress, ConstantUtil.UPLOAD_ERROR);
		} else if (ConstantUtil.INTERNAL_ERROR.equals(error)) {

			Assert.assertNotEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		} else if (ConstantUtil.INVALID_PARAMS.equals(error)) {

			Assert.assertNotEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);

		}
	}

	@BeforeMethod
	public static void beforeMethod() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

	@AfterClass
	public static void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}
}
