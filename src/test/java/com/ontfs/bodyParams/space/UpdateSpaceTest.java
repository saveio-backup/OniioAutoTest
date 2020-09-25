package com.ontfs.bodyParams.space;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class UpdateSpaceTest extends TestBase {

	private Object[] invalidSpaceVolume = ConstantUtil.invalidSpaceVolume;
	private Object[] invalidExpiredTime = ConstantUtil.invalidExpiredTime_space;

	@Test
	public void testUpdateSpaceWithInvalidVolume() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		for (int i = 0; i < invalidSpaceVolume.length; i++) {
			JSONObject object = SpaceUtils.updateSpace(clientUrl, invalidSpaceVolume[i], expiredTime);
			if (i < invalidSpaceVolume.length - 3) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}

	@Test
	public void testUpdateSpaceWithInvalidExpiredTime() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		for (int i = 0; i < invalidExpiredTime.length; i++) {
			JSONObject object = SpaceUtils.updateSpace(clientUrl, volume, invalidExpiredTime[i]);
			if (i < invalidExpiredTime.length - 3) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}

	

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
		JSONObject space = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(space), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(space), ConstantUtil.SUCCESS_CODE);
		
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
		 
	}

}
