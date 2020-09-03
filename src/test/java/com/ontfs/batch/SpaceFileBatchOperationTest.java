package com.ontfs.batch;

import java.io.File;
import java.util.Date;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.SpaceUtils;
import com.ontfs.utils.TaskUtils;
import com.ontfs.utils.TestBase;

public class SpaceFileBatchOperationTest extends TestBase {
	

	@Test
	public void createSpace() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		JSONObject object = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		// Calculate space cost
//		Assert.assertTrue(SpaceUtils.calculateSpaceCost(clientUrl, volume, copyNum, pdpInterval, expiredTime));

	}
	
	@Test
	public void getSpace() {
		JSONObject object=SpaceUtils.getSpaceInfo(clientUrl);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
	}

	@Test(dataProvider = "getFilesName")
	public void batchUploadSpaceFile(String fileName) {
		log.info("=========The current method is  " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ "and current fileName is :" + fileName);
		String filePath = uploadFilePath + "/" + fileName;
//		log.info("filePath is :" + filePath);
//		File file = new File(filePath);
		JSONObject object = FileUtils.uploadFile(clientUrl, filePath,  new Date().getTime() + "-" +fileName,pwd, expiredTime, copyNum, true, 0);
		//
		String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
		// verify upload file success
		Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
		log.info("空间文件上传完成：" + taskId);
	}

	@Test(dataProvider = "getdownloadFilesStr")
	public void testBatchDownloadSpaceFiles(String str) {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " and current fileName is :" + str);
		String fileHash = str.split("@", 2)[0];
		String outFile = downloadFileDirectory + "/" +new Date().toString()+"-"+str.split("@", 2)[1];
	

		// commit download task
		JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, inorder, maxPeerCnt, outFile, pwd);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

		
		Assert.assertTrue(FileUtils.verifyDownloadSuccess(clientUrl, taskId));
		log.info("空间文件下载完成：" + fileHash);
	}
	@Test
	public void deleteSpace() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		FileUtils.deleteAllFiles(clientUrl);
		JSONObject object = SpaceUtils.deleteSpace(clientUrl);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		
	    Assert.assertTrue(SpaceUtils.verifydeleteSpaceSuccess(clientUrl));
	}

	@Test
	public void updateSpace() {
		JSONObject object=SpaceUtils.updateSpace(clientUrl,volume+1024,expiredTime+3600);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
	} 
	

	@BeforeClass
	public static void beforeClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

	@AfterClass
	public static void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
	}

	
}
