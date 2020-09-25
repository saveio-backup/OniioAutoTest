package com.ontfs.scenarioTest.space;

import com.ontfs.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class CreateSpaceTest extends TestBase {

	@Test
	public void testCreateSpaceWhenSpaceExists() {
		// firstly,create space
		JSONObject object = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

		// second create space
		JSONObject againCreateSpace = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getError(againCreateSpace), ConstantUtil.INTERNAL_ERROR);

	}
	
	

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		SectorUtils.createSectorBeforeUploadFiles();
	}
	@AfterClass
	public void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

}
