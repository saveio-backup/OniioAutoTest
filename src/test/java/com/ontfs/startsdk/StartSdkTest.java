package com.ontfs.startsdk;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.SdkUtil;
import com.ontfs.utils.TestBase;

public class StartSdkTest extends TestBase {

	@Test
	public void testStartSdk() {
		JSONObject object = SdkUtil.startSdk(clientUrl,  ontoUrl,  walletPath,  gasPrice,  gasLimit,  pdpVersion,
				 pwd,  p2pProtocol,  p2pListenAddr,  p2pNetworkId,  txTimeout);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
	}
}
