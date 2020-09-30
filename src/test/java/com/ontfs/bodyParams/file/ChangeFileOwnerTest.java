package com.ontfs.bodyParams.file;

import com.ontfs.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class ChangeFileOwnerTest extends TestBase {
	private Object[] invalidFileHash = ConstantUtil.invalidFileHash;
	private Object[] invalidWallet = ConstantUtil.invalidWallet;

	@Test(groups = "unusual")
	public void testChangeFileOwnerWithInvalidFileHash() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			JSONObject object = FileUtils.changeFileOwner(clientUrl, invalidFileHash[i], wallet);
			if (i == invalidFileHash.length - 1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			}
		}
	}

	@Test(groups = "unusual")
	public void testChangeFileOwnerWithInvalidWallet() {
		String fileHash = FileUtils.getFileHashByUploadFile(1,1,pwd);
		for (int i = 0; i < invalidWallet.length; i++) {
			JSONObject object = FileUtils.changeFileOwner(clientUrl, fileHash, invalidWallet[i]);
			if (i == invalidWallet.length - 1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			}
		}
	}

	@Test(groups = "unusual")
	public void testChangeFileOwnerWithInvalidParams() {
		for (int i = 0; i < invalidFileHash.length; i++) {
			for (int j = 0; j < invalidWallet.length; j++) {
				JSONObject object = FileUtils.changeFileOwner(clientUrl, invalidFileHash[i], invalidWallet[j]);
				if (i == invalidFileHash.length - 1 || j == invalidWallet.length - 1) {
					Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
				} else {
					Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
				}
			}
		}
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
	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		SectorUtils.createSectorBeforeUploadFiles(1);
	}

}
