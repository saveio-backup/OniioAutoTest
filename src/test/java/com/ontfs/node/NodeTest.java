package com.ontfs.node;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.NodeUtils;
import com.ontfs.utils.TestBase;

public class NodeTest extends TestBase {

	public static Object[][] serverArray = null;

	@Test
	public void TestNodeList() {
		JSONObject object = NodeUtils.getNodeList(clientUrl,50);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
	}

	@Test(dataProvider = "getNodeAddrs")
	public void testQueryNode(String nodeAddr) {
		JSONObject object = NodeUtils.queryNode(clientUrl, nodeAddr);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		Assert.assertNotEquals(CommonUtils.getResult(object), null);
	}

	@Test
	public void testDetailService() {
		JSONObject object = NodeUtils.detailService(clientUrl);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		Assert.assertNotEquals(CommonUtils.getResult(object), null);
	}

	@DataProvider(name = "getNodeAddrs", parallel = true)
	public Object[][] getNodeAddrs() {
		if (serverAddressArray.length != 0) {
			int length = serverAddressArray.length;
			serverArray = new Object[length][1];
			for (int i = 0; i < length; i++) {
				String nodeAddr = serverAddressArray[i];
				serverArray[i][0] = nodeAddr;
			}
		}
		return serverArray;
	}
}
