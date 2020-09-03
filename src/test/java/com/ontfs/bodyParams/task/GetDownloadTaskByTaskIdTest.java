package com.ontfs.bodyParams.task;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class GetDownloadTaskByTaskIdTest extends TestBase {

	private Object[] invalidTaskId = ConstantUtil.invalidTaskId;

	@Test
	public void testGetDownloadTaskByTaskIdWithInvalidTaskId() {
		for (int i = 0; i < invalidTaskId.length; i++) {
			JSONObject object=TaskUtils.getDownloadTaskinfoByTaskId(clientUrl, invalidTaskId[i]);
			if(i<invalidTaskId.length-2) {
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
