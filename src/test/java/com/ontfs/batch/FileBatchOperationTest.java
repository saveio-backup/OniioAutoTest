package com.ontfs.batch;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.TestBase;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.NodeUtils;
import com.ontfs.utils.TaskUtils;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

public class FileBatchOperationTest extends TestBase {

	@Test(dataProvider = "getFilesName")
	public void testBatchUploadFiles(String fileName) {

		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ "and current fileName is :" + fileName);
		String filePath = uploadFilePath + "/" + fileName;
//		log.info("filePath is :" + filePath);
//		File file = new File(filePath);
		JSONObject obj = FileUtils.uploadFile(clientUrl,filePath,  new Date().getTime() + "-" + fileName,pwd, expiredTime, copyNum, true,1);
		Assert.assertEquals(CommonUtils.getDesc(obj), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(obj).toString(), ConstantUtil.SUCCESS_CODE);
		String taskId = ((JSONObject) obj.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

		// verify upload success
		Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
		// Calculate upload cost
//		Assert.assertTrue(FileUtils.CalculateUploadCost(clientUrl, taskId, expiredTime, pdpInterval, copyNum));

		log.info("上传完成：" + taskId);
	}

	@Test(dataProvider = "getdownloadFilesStr")
	public void testBatchGetFileInfo(String str) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String fileHash = str.split("@", 2)[0];
		JSONObject object=FileUtils.getFileInfo(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		
		Assert.assertNotEquals(CommonUtils.getResult(object), null);
	}
	@Test(dataProvider = "getdownloadFilesStr")
	public void testBatchDownloadFiles(String str) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " downloadFiles,and current fileName is :" + str);
		String fileHash = str.split("@", 2)[0];
		String outFile = downloadFileDirectory + "/" + str.split("@", 2)[1];
		// get readpleder
//		float preRestMoney = FileUtils.getPledgeCost(clientUrl,fileHash);
//		String[] preNodeBalance = NodeUtils.getAllNodeBalance(ontoUrl, serverAddress);
//		log.info("文件Hash：" + fileHash + "  restmoney=" + preRestMoney);
		// commit download task
		JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, inorder, maxPeerCnt, outFile,"");
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

		String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

		// verify download file success
		Assert.assertTrue(FileUtils.verifyDownloadSuccess(clientUrl, taskId));
		// Calculate download cost
//		Assert.assertTrue(FileUtils.calculateDownloadCost(clientUrl, ontoUrl, serverAddress, taskId, fileHash,
//				maxPeerCnt, preRestMoney, preNodeBalance, copyNum));
		log.info("下载完成：" + fileHash);
	}

	@Test(dataProvider = "getdownloadFilesStr")
	public void testBatchReadPledge(String str) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String fileHash = str.split("@", 2)[0];
		JSONObject object = FileUtils.getFileReadPledge(clientUrl, fileHash);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		JSONObject result = CommonUtils.getResult(object);
		Assert.assertEquals(result.getString(ConstantUtil.FILEHASH), fileHash);

	}

	@Test(dataProvider = "getDecryptFile")
	public void testBatchDecryptFiles(String fileName) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		String sourceFilePath = downloadFileDirectory + "/" + fileName;
		String decrptFilePath = decryFileDirectory + "/" + "decry-" + fileName;

		JSONObject object = FileUtils.decryptFile(clientUrl, sourceFilePath, decrptFilePath, pwd);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
	}

	@Test
	public void verifyDecryFileByMD5() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// Verify file MD5 encryption
		Assert.assertTrue(FileUtils.verifyDecryFileListMD5(uploadFilePath, decryFileDirectory));
	}

	@DataProvider(name = "getDecryptFile", parallel = true)
	private Object[][] getDecryptFile() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		File filePath = new File(downloadFileDirectory);

		if (filePath.isDirectory()) {
			File[] files = filePath.listFiles();
			Object[][] objArray = new Object[files.length][1];
			for (int i = 0; i < files.length; i++) {
				
				log.info("解密文件："+files[i].getName());
				objArray[i][0] = files[i].getName();
			}
			return objArray;
		} else {
			return new Object[][] {};
		}
	}

	@Test(dataProvider = "getdownloadFilesStr")
	public void testBatchRenewFiles(String str) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String fileHash = str.split("@", 2)[0];
		JSONObject object = FileUtils.renewFile(clientUrl, fileHash, 3600);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

		// verify renew success

		Assert.assertTrue(FileUtils.verifyFileRenewSuccess(clientUrl, fileHash, expiredTime, 3600));

	}

	@Test(dataProvider = "getdownloadFilesStr")
	public void testBatchChangeFileOwner(String str) {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		String fileHash = str.split("@", 2)[0];

		JSONArray pdpArray = FileUtils.getPdpinfoList(clientUrl, fileHash).getJSONArray(ConstantUtil.RESULT);
		Assert.assertNotEquals(pdpArray.size(), 0);

		JSONObject object = FileUtils.changeFileOwner(clientUrl, fileHash, wallet2);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

		Assert.assertTrue(FileUtils.verifyChangeFileOwnerSuccess(clientUrl, fileHash,
				pdpArray.getJSONObject(0).getString(ConstantUtil.NODE_ADDR), wallet2));
	}

	public static void testBatchDeleteTask(String urlRoot) {
		TaskUtils.deleteAllTasks(urlRoot);
		// verify delete all task success
		Assert.assertTrue(TaskUtils.verifyDeleteTasksSuccess(urlRoot));
	}

	public static void testBatchDeleteFiles(String clientUrl) {

		Map<String, Double> preServerProfit = NodeUtils.getNodeProfit(clientUrl);

//		log.info("删除文件前的nodelist=" + preServerProfit);
//		Map<String, Double> expiredServerProfit = NodeUtils.calculateAllFileProfitForNode(clientUrl, preServerProfit,
//				defaultPerBlockSize, gasPerKbForSaveWithFile);
		
		FileUtils.deleteAllFiles(clientUrl);

//		log.info("删除文件=expiredServerProfit" + expiredServerProfit);

		// verify delete All files success
		Assert.assertTrue(FileUtils.verifyfDeleteAllFilesSuccess(clientUrl));
		
		Map<String, Double> postServerProfit = NodeUtils.getNodeProfit(clientUrl);
		
		log.info("删除文件后的nodelist=" + postServerProfit);
//		Assert.assertTrue(NodeUtils.verifyServerProfitCorrect(expiredServerProfit, postServerProfit));

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
