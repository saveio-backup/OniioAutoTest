package com.ontfs.bodyParams.node;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.NodeUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class GetNodeListTest extends TestBase {
	private Object[] invalidLimit = ConstantUtil.invalidLimit;

	@Test
	public void testGetNodeListWithInvalidLimit() {
		for (int i = 0; i < invalidLimit.length; i++) {
			JSONObject object = NodeUtils.getNodeList(clientUrl, invalidLimit[i]);
			/*
			 * if (i < invalidLimit.length - 3) {
			 * Assert.assertEquals(CommonUtils.getError(object),
			 * ConstantUtil.INTERNAL_ERROR); } else {
			 */
				Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_PARAMS);
//			}
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
