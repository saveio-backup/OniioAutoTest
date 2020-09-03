package com.ontfs.bodyParams.file;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class GetFilePdpInfoListTest extends TestBase {
	
	private Object[] invalidFileHash=ConstantUtil.invalidFileHash;
  @Test
  public void testGetPdpListWithInvalidFileHash() {
	  for (int i = 0; i < invalidFileHash.length; i++) {
			JSONObject object = FileUtils.getPdpinfoList(clientUrl, invalidFileHash[i]);
			if (i < invalidFileHash.length - 1) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
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
  

}
