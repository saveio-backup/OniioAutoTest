package com.ontfs.scenarioTest.file;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
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
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

public class DecryptFileTest extends TestBase {

	@Test
	public void testTwoDecryptFileWithSameName() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String downloadPath = FileUtils.getDownloadFilePathAfterDownloadFileSuccessfully(pwd);
		String decryptPath = decryFileDirectory + "/" + System.currentTimeMillis();
		// first decrypt
		JSONObject object = FileUtils.decryptFile(clientUrl, downloadPath, decryptPath, pwd);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		//second decrypt
		
		JSONObject object2 = FileUtils.decryptFile(clientUrl, downloadPath, decryptPath, pwd);
		Assert.assertEquals(CommonUtils.getError(object2), ConstantUtil.INTERNAL_ERROR);

	}
	
	@Test
	public void testDecrypUnencryptedFilesWithPwd() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String downloadPath=FileUtils.getDownloadFilePathAfterDownloadFileSuccessfully();
		JSONObject object = FileUtils.decryptFile(clientUrl, downloadPath, decryFileDirectory+"/"+System.currentTimeMillis(), pwd);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
	}
	
	@Test
	public void testDecryAfterDeleteFile() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// upload file
		String filePath= uploadFilePath + "/" + getFilesName()[0][0];
		JSONObject obj = FileUtils.uploadFile(clientUrl, filePath, String.valueOf(System.currentTimeMillis()) , pwd,
				expiredTime, copyNum, true, 1,1);
		Assert.assertEquals(CommonUtils.getDesc(obj), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(obj), ConstantUtil.SUCCESS_CODE);
		String taskId = ((JSONObject) obj.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
		// verify upload success
		Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));

		// getFileHash
		String fileHash = TaskUtils.getUploadFileHashByTaskId(clientUrl, taskId);
		
		//download file
		String downloadFilePath= downloadFileDirectory+"/"+System.currentTimeMillis();
		JSONObject downloadObject=FileUtils.downloadFile(clientUrl, fileHash, true, maxPeerCnt,downloadFilePath, "");
		Assert.assertEquals(CommonUtils.getDesc(downloadObject), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(downloadObject).toString(), ConstantUtil.SUCCESS_CODE);
		
		String DownloadTaskId = ((JSONObject) downloadObject.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

		// verify download file success
		Assert.assertTrue(FileUtils.verifyDownloadSuccess(clientUrl, DownloadTaskId));

		//delete file
		String[] array=new String[] {fileHash};
		JSON json=(JSON) JSON.toJSON(array);
		JSONObject deleteObject=FileUtils.deleteFiles(clientUrl, json);
		Assert.assertEquals(CommonUtils.getDesc(deleteObject), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(deleteObject).toString(), ConstantUtil.SUCCESS_CODE);
		 
		//decrypt file
		String decryptFilePath= decryFileDirectory+"/"+System.currentTimeMillis();
		JSONObject decrypt=FileUtils.decryptFile(clientUrl, downloadFilePath,decryptFilePath, pwd);
		Assert.assertEquals(CommonUtils.getDesc(decrypt), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(decrypt).toString(), ConstantUtil.SUCCESS_CODE);
		
		Assert.assertTrue(FileUtils.verifyDecryFileListMD5(filePath, decryptFilePath));
	
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
