package com.ontfs.scenarioTest.space;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TestBase;

public class GetSpaceTest extends TestBase{
	
  @Test
  public void testGetSpaceWhenNoSpace() {
	 JSONObject object=SpaceUtils.getSpaceInfo(clientUrl);
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

}
