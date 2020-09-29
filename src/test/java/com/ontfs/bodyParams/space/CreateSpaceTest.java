package com.ontfs.bodyParams.space;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class CreateSpaceTest extends TestBase {

	private Object[] invalidSpaceVolume = ConstantUtil.invalidSpaceVolume;
	private Object[] invalidCopyNum = ConstantUtil.invalidCopyNum;
	private Object[] invalidExpiredTime = ConstantUtil.invalidExpiredTime_space;

	@Test(groups = "unusual")
	public void testCreateSpaceWithInvalidSpaceVolume() {
		for (int i = 0; i < invalidSpaceVolume.length; i++) {
			JSONObject object = SpaceUtils.createSpace(clientUrl, invalidSpaceVolume[i], copyNum, pdpInterval,
					expiredTime);
			if (i < invalidSpaceVolume.length - 3) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}

	@Test(enabled = false,groups = "unusual")
	public void testCreateSpaceWithInvalidCopyNum() {
		for (int i = 0; i < invalidCopyNum.length; i++) {
			JSONObject object = SpaceUtils.createSpace(clientUrl, volume, invalidCopyNum[i], pdpInterval, expiredTime);
			if (i < invalidCopyNum.length - 3) {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
			} else {
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
			}
		}
	}

	@Test(groups = "unusual")
	public void testCreateSpaceWithInvalidExpiredTime() {
		for (int i = 0; i < invalidExpiredTime.length; i++) {
			JSONObject object = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, invalidExpiredTime[i]);
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
	}

	@AfterClass
	public void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

}
