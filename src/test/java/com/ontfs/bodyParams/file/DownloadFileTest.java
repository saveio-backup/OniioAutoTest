package com.ontfs.bodyParams.file;

import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.paramBean.responseBean.Task;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

public class DownloadFileTest extends TestBase {

	private Object[] invalidFileHash = ConstantUtil.invalidFileHash;
	private Object[] invalidOutPath = ConstantUtil.invalidOutPath;
	private Object[] invalidDecryptPwd = ConstantUtil.invalidPwd;

	@Test
	public void testDownloadFileWithInvaildFileHash() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		for (int i = 0; i < invalidFileHash.length; i++) {
			JSONObject object = FileUtils.downloadFile(clientUrl, invalidFileHash[i], true, maxPeerCnt,
					downloadFileDirectory, pwd);
			verifyDownloadFileErrorCode(object);
		}

	}

	@Test
	public void testDownloadFileWithInvalidOutPath() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String fileHash = FileUtils.getFileHashByUploadFile(1,pwd);
		for (int i = 0; i < invalidOutPath.length; i++) {
			JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, true, maxPeerCnt, invalidOutPath[i], pwd);
			verifyDownloadFileErrorCode(object);
		}

	}

	@Test
	public void testDownloadFileWithInvalidPwd() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String fileHash = FileUtils.getFileHashByUploadFile(1,pwd);
		for (int i = 0; i < invalidDecryptPwd.length; i++) {
			JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, true, maxPeerCnt, downloadFileDirectory,
					invalidDecryptPwd[i].toString());
			verifyDownloadFileErrorCode(object);
		}
	}


	private void verifyDownloadFileErrorCode(JSONObject object) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String error = CommonUtils.getError(object);

		if (ConstantUtil.SUCCESS_CODE.equals(error)) {
			String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
			// get task
			Task task = (TaskUtils.getDownloadTaskinfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
					.toJavaObject(Task.class);

			int progress = task.TaskBaseInfo.Progress;
			while (progress != ConstantUtil.DOWNLOAD_ERROR) {
				task = (TaskUtils.getDownloadTaskinfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
						.toJavaObject(Task.class);
				progress = task.TaskBaseInfo.Progress;
				sleep(2000);

			}
			Assert.assertEquals(progress, ConstantUtil.DOWNLOAD_ERROR);
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
