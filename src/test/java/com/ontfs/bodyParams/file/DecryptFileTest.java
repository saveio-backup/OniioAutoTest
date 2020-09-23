package com.ontfs.bodyParams.file;

import com.ontfs.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;

public class DecryptFileTest extends TestBase {

	private Object[] invalidPwd = ConstantUtil.invalidPwd;
	private Object[] invalidFilePath = ConstantUtil.invalidFilePath;
	private Object[] invalidDecryptPath = ConstantUtil.invalidDecryptPath;

	@Test
	public void testDecryptFileWithWrongPwd() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String downloadPath = FileUtils.getDownloadFilePathAfterDownloadFileSuccessfully(pwd);
		for (int i = 0; i < invalidPwd.length - 1; i++) {
			JSONObject object = FileUtils.decryptFile(clientUrl, downloadPath,
					decryFileDirectory + "/" + System.currentTimeMillis(), invalidPwd[i]);
			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
		}
	}

	@Test
	public void testDecryptFileWithPwdTypeIsWrong() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String downloadPath = FileUtils.getDownloadFilePathAfterDownloadFileSuccessfully(pwd);
		// 解密密码类型错误
		JSONObject object = FileUtils.decryptFile(clientUrl, downloadPath,
				decryFileDirectory + "/" + System.currentTimeMillis(), invalidPwd[invalidPwd.length - 1]);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
	}

	@Test
	public void testDecryptFileWithFilePathIsWrong() {
		for (int i = 0; i < invalidFilePath.length - 1; i++) {
			JSONObject object = FileUtils.decryptFile(clientUrl, invalidFilePath[i],
					decryFileDirectory + "/" + System.currentTimeMillis(), pwd);
			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
		}

	}

	@Test
	public void testDecryptFileWithFilePathTypeIsWrong() {
		JSONObject object = FileUtils.decryptFile(clientUrl, invalidFilePath[invalidFilePath.length - 1],
				decryFileDirectory + "/" + System.currentTimeMillis(), pwd);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
	}

	@Test
	public void testDecryptFileWithInvalidDecryptPath() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String downloadPath = FileUtils.getDownloadFilePathAfterDownloadFileSuccessfully(pwd);
		for (int i = 0; i < invalidDecryptPath.length - 1; i++) {
			JSONObject object = FileUtils.decryptFile(clientUrl, downloadPath, invalidDecryptPath[i], pwd);
			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
		}
		JSONObject object = FileUtils.decryptFile(clientUrl, downloadPath,
				invalidDecryptPath[invalidDecryptPath.length - 1], pwd);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
	}



	@Test
	public void testTwoDecryptFileWithSameName() {
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
		String downloadPath=FileUtils.getDownloadFilePathAfterDownloadFileSuccessfully();
		JSONObject object = FileUtils.decryptFile(clientUrl, downloadPath, decryFileDirectory+"/"+System.currentTimeMillis(), pwd);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
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
