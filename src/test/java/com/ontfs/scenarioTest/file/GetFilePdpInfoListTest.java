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

public class GetFilePdpInfoListTest extends TestBase{
  @Test
  public void testGetPdpListAfterDeleteFile() {
	  
	  log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
	  //upload file
	  String fileHash=FileUtils.getFileHashByUploadFile(1,1, pwd);
		
		//delete file
		String[] array=new String[] {fileHash};
		JSON json=(JSON) JSON.toJSON(array);
		JSONObject deleteObject=FileUtils.deleteFiles(clientUrl, json);
		Assert.assertEquals(CommonUtils.getDesc(deleteObject), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(deleteObject).toString(), ConstantUtil.SUCCESS_CODE);
		
		//get file pdp info
		JSONObject pdpObject=FileUtils.getPdpinfoList(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getError(pdpObject), ConstantUtil.INTERNAL_ERROR);
	  
	  
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
 