package com.ontfs.batch;

import com.ontfs.utils.*;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

public class FileBatchOperationTest extends TestBase {

    @Test(dataProvider = "getFilesName", groups = "normal")
    public void testBatchUploadFiles(String fileName) {

        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + "and current fileName is :" + fileName);
        log.info("uploadFilePath=" + uploadFilePath);
        String filePath = uploadFilePath + "/" + fileName;
//		log.info("filePath is :" + filePath);
//		File file = new File(filePath);
        JSONObject obj = FileUtils.uploadFile(clientUrl, filePath, new Date().getTime() + "-" + fileName, pwd, expiredTime, copyNum, true, 1, 1);
        Assert.assertEquals(CommonUtils.getDesc(obj), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(obj).toString(), ConstantUtil.SUCCESS_CODE);
        String taskId = ((JSONObject) obj.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

        // verify upload success
        Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
        // Calculate upload cost
//		Assert.assertTrue(FileUtils.CalculateUploadCost(clientUrl, taskId, expiredTime, pdpInterval, copyNum));


        log.info("上传完成：" + taskId);
    }

    @Test(groups = "normal")
    public void testSectorFileOrder() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        //Verify file order in sector
        Assert.assertTrue(SectorUtils.verifySectorFileOrder(serverUrlArray, serverAddressArray));
    }

    @Test(dataProvider = "getdownloadFilesStr", groups = "normal")
    public void testBatchGetFileInfo(String str) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String fileHash = str.split("@", 2)[0];
        JSONObject object = FileUtils.getFileInfo(clientUrl, fileHash);
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

        Assert.assertNotEquals(CommonUtils.getResult(object), null);
    }

    @Test(dataProvider = "getdownloadFilesStr", groups = "normal")
    public void testBatchDownloadFiles(String str) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " downloadFiles,and current fileName is :" + str);
        String fileHash = str.split("@", 2)[0];
        String outFile = downloadFileDirectory + "/" + str.split("@", 2)[1];
        // get readpleder
//		float preRestMoney = FileUtils.getPledgeCost(clientUrl,fileHash);
//		String[] preNodeBalance = NodeUtils.getAllNodeBalance(ontoUrl, serverAddressArray);
//		log.info("文件Hash：" + fileHash + "  restmoney=" + preRestMoney);
        // commit download task
        JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, inorder, maxPeerCnt, outFile, "");
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

        String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

        // verify download file success
        Assert.assertTrue(FileUtils.verifyDownloadSuccess(clientUrl, taskId));
        // Calculate download cost
//		Assert.assertTrue(FileUtils.calculateDownloadCost(clientUrl, ontoUrl, serverAddressArray, taskId, fileHash,
//				maxPeerCnt, preRestMoney, preNodeBalance, copyNum));
        log.info("下载完成：" + fileHash);
    }

    @Test(dataProvider = "getdownloadFilesStr", groups = "normal")
    public void testBatchReadPledge(String str) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String fileHash = str.split("@", 2)[0];
        JSONObject object = FileUtils.getFileReadPledge(clientUrl, fileHash);
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
        JSONObject result = CommonUtils.getResult(object);
        Assert.assertEquals(result.getString(ConstantUtil.FILEHASH), fileHash);

    }

    @Test(dataProvider = "getdownloadFilesStr", groups = "normal")
    public void testBatchDecryptFiles(String str) {

        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " testBatchDecryptFiles,and current str is :" + str);
        String fileHash = str.split("@", 2)[0];
        String fileDesc = str.split("@", 2)[1];
        String sourceFilePath = downloadFileDirectory + "/" + str.split("@", 2)[1];
        String decrptFilePath = decryFileDirectory + "/" + "decry-" +fileDesc;

        JSONObject object = FileUtils.decryptFile(clientUrl, sourceFilePath, decrptFilePath, pwd);
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
    }

    @Test(groups = "normal")
    public void verifyDecryFileByMD5() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

        // Verify file MD5 encryption
        Assert.assertTrue(FileUtils.verifyDecryFileListMD5(uploadFilePath, decryFileDirectory));
    }

    @DataProvider(name = "getDecryptFile", parallel = true)
    private Object[][] getDecryptFile() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("downloadFileDirectory=" + downloadFileDirectory);
        File filePath = new File(downloadFileDirectory);
        log.info("filePath.isDirectory()=" + filePath.isDirectory());
        log.info("filePath=" + filePath);
        if (filePath.isDirectory()) {
            File[] files = filePath.listFiles();
            Object[][] objArray = new Object[files.length][1];
            for (int i = 0; i < files.length; i++) {

                log.info("解密文件：" + files[i].getName());
                objArray[i][0] = files[i].getName();
            }
            return objArray;
        } else {
            return new Object[][]{};
        }
    }

    @Test(dataProvider = "getdownloadFilesStr", groups = "normal")
    public void testBatchRenewFiles(String str) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String fileHash = str.split("@", 2)[0];
        JSONObject object = FileUtils.renewFile(clientUrl, fileHash, 3600);
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

        // verify renew success

        Assert.assertTrue(FileUtils.verifyFileRenewSuccess(clientUrl, fileHash, expiredTime, 3600));

    }

    @Test(dataProvider = "getdownloadFilesStr", groups = "normal")
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

	@BeforeClass(alwaysRun = true)
	public static void beforeClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);
		SectorUtils.createSectorBeforeUploadFiles(1);

	}

	@AfterClass(alwaysRun = true)
	public static void afterClass() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
		deleteFileAndTaskAndSpace(clientUrl);

	}

}
