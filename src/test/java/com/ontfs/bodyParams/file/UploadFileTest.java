package com.ontfs.bodyParams.file;

import java.io.File;
import java.util.Date;

import com.ontfs.dataProvider.SectorDataProvider;
import com.ontfs.utils.*;
import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.paramBean.responseBean.Task;

public class UploadFileTest extends TestBase {

    private Object[] invalidFilePath = ConstantUtil.invalidFilePath;

    private Object[] invalidExpiredTime = ConstantUtil.invalidExpiredTime;

    private Object[] invalidCopynum = ConstantUtil.invalidCopyNum;
    ;

    private Object[] invalidStorageType = ConstantUtil.invalidStorageType;

    private Object[] validCopyNum = ConstantUtil.validCopyNum;

    private Object[] validStorageType = ConstantUtil.validStorageType;

    @Test(dataProvider = "invaidStringFilePath", dataProviderClass = DataProviderUtils.class,groups = "unusual")
    public void testUploadFileWithFilePathIsWrong(String filePath) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " filepath是" + filePath);

        JSONObject object = FileUtils.uploadFile(clientUrl, filePath,
                new Date().getTime() + "-" + System.currentTimeMillis(), pwd, expiredTime, copyNum, true, 1, 1);
        verifyUploadFileError(object);
    }

    @Test(dataProvider = "invalidNumber", dataProviderClass = DataProviderUtils.class,groups = "unusual")
    public void testUploadFileWithFilePathTypeIsWrong(Object filePath) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + " filepath是" + filePath);
        JSONObject object = FileUtils.uploadFile(clientUrl, filePath, new Date().getTime(), pwd, expiredTime, copyNum,
                true, 1, 1);
        verifyUploadFileError(object);
    }

    @Test(dataProvider = "descIsNull", dataProviderClass = DataProviderUtils.class,groups = "unusual")
    public void testUploadFileWithInvalidDesc(Object desc) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

        JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", desc, pwd, expiredTime,
                copyNum, true, 1, 1);
        if (desc == " ") {
            String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
            // verify upload success
            Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
        } else {
            Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INTERNAL_ERROR);
        }
    }

    @Test(groups = "normal")
    public void testUploadFileWithDescIsChinese() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "花花", pwd, expiredTime,
                copyNum, true, 1, 1);
        String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
        // verify upload success
        Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));

        JSONObject taskObject = TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId);
        Task uploadTask = taskObject.getJSONObject(ConstantUtil.RESULT).toJavaObject(Task.class);
        // get file info
        JSONObject fileinfoObject = FileUtils.getFileInfo(clientUrl, uploadTask.TaskBaseInfo.FileHash);
        String desc = fileinfoObject.getJSONObject(ConstantUtil.RESULT).getString(ConstantUtil.FILE_DESC);
        Assert.assertEquals(desc, "花花");

    }

    @Test(groups = "unusual")
    public void testUploadFileWithInvaildExpiredTime() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (int i = 0; i < invalidExpiredTime.length; i++) {
            JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
                    invalidExpiredTime[i], copyNum, true, 1, 1);

            verifyUploadFileError(object);

        }
    }

    @Test(groups = "unusual")
    public void testUploadFileWithInvaildCopyNum() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (int i = 0; i < invalidCopynum.length; i++) {
            JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
                    expiredTime, invalidCopynum[i], true, 1, 1);
            verifyUploadFileError(object);
        }
    }

    @Test(groups = "unusual")
    public void testUploadFileWithValidCopyNum() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (int i = 0; i < validCopyNum.length; i++) {
            JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
                    expiredTime, validCopyNum[i], true, 1, 1);
            Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
            AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
            String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

            // verify upload success
            Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
        }
    }

    @Test(groups = "unusual")
    public void testUploadFileWithInvaildStorageType() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (int i = 0; i < invalidStorageType.length; i++) {
            JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
                    expiredTime, copyNum, true, invalidStorageType[i], 1);
            verifyUploadFileError(object);
        }
    }

    @Test(groups = "normal")
    private void testUploadFileWithVaildStorageType() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        //create sapce
        JSONObject obj = SpaceUtils.createSpace(clientUrl, volume, copyNum, pdpInterval, expiredTime);
        Assert.assertEquals(CommonUtils.getDesc(obj), ConstantUtil.SUCCESS);
        Assert.assertEquals(CommonUtils.getError(obj), ConstantUtil.SUCCESS_CODE);
        for (int i = 0; i < validStorageType.length; i++) {
            JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
                    expiredTime, copyNum, true, validStorageType[i], 1);
            Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
            AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
            String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

            // verify upload success
            Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));
        }
    }

    @Test(dataProvider = "invalidProveLevel", dataProviderClass = SectorDataProvider.class,groups = "unusual")
    private void testUploadFileWithInvalidProveLevel(Object invalidProveLevel) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        //upload file
        JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
                expiredTime, copyNum, true, 1, invalidProveLevel);
        verifyUploadFileError(object);
    }

    @Test(groups = "normal")
    private void testUploadFileToDifferentLevelOfTheSameNode() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);
        //create sector
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "22", "1G", 2);
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

        JSONObject object1 = SectorUtils.createSector(serverUrlArray[0], "33", "1G", 3);
        Assert.assertEquals(CommonUtils.getError(object1), ConstantUtil.SUCCESS_CODE);

        String fileHash = FileUtils.getFileHashByUploadFile(1, 2);
        Assert.assertTrue(SectorUtils.verifySectorFile(serverUrlArray[0], "22", fileHash));

        String fileHash1 = FileUtils.getFileHashByUploadFile(1, 3);
        Assert.assertTrue(SectorUtils.verifySectorFile(serverUrlArray[0], "33", fileHash1));

    }

    /**
     * test upload files to same level sector of the same node
     */
    @Test(groups = "normal")
    private void testUploadFilesToSameLevelOfThSameNode() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);
        //create sector
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "222", "1G", 2);
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

        //create sector
        JSONObject object1 = SectorUtils.createSector(serverUrlArray[0], "2222", "1G", 2);
        Assert.assertEquals(CommonUtils.getError(object1), ConstantUtil.SUCCESS_CODE);

        String fileHash = FileUtils.getFileHashByUploadFile(1, 2);
        Assert.assertTrue(SectorUtils.verifyNodeContainFile(serverUrlArray[0], serverAddressArray[0], fileHash, 2));
    }

    /**
     * test upload file to same sector of different node
     */
    @Test(groups = "normal")
    private void testUploadFilesToSameSectorOfDifferentNode() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);

        //create sector
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "10", "1G", 1);
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

        //create sector
        JSONObject object1 = SectorUtils.createSector(serverUrlArray[1], "10", "1G", 1);
        Assert.assertEquals(CommonUtils.getError(object1), ConstantUtil.SUCCESS_CODE);

        String fileHash = FileUtils.getFileHashByUploadFile(1, 1);
        boolean result1 = SectorUtils.verifyNodeContainFile(serverUrlArray[0], serverAddressArray[0], fileHash, 1);
        boolean result2 = SectorUtils.verifyNodeContainFile(serverUrlArray[1], serverAddressArray[1], fileHash, 1);

        Assert.assertTrue(result1 || result2);

    }

    /**
     * test upload file to different sector of different node
     */
    @Test(groups = "normal")
    private  void  testUploadFileToDifferentSectorOfDifferentNode(){
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);

        //create sector
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "1", "1G", 1);
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

        //create sector
        JSONObject object1 = SectorUtils.createSector(serverUrlArray[1], "2", "1G", 2);
        Assert.assertEquals(CommonUtils.getError(object1), ConstantUtil.SUCCESS_CODE);

        String fileHash = FileUtils.getFileHashByUploadFile(1, 1);
        String fileHash1 = FileUtils.getFileHashByUploadFile(1, 2);
      Assert.assertTrue(SectorUtils.verifyNodeContainFile(serverUrlArray[0], serverAddressArray[0], fileHash, 1));
        Assert.assertTrue(SectorUtils.verifyNodeContainFile(serverUrlArray[1], serverAddressArray[1], fileHash, 2));

    }

    @Test(groups = "unusual")
    private void testUploadFileToNoExistSector(){
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);
        JSONObject object = FileUtils.uploadFile(clientUrl, uploadFilePath + "/wallet.dat", "wallet.dat", pwd,
                expiredTime, copyNum, true, 1, 2);
        verifyUploadFileError(object);
    }
    private void verifyUploadFileError(JSONObject object) {
        String error = CommonUtils.getError(object);

        if (ConstantUtil.SUCCESS_CODE.equals(error)) {
            String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
            // get task
            Task task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
                    .toJavaObject(Task.class);

            int progress = task.TaskBaseInfo.Progress;
            while (progress != ConstantUtil.UPLOAD_ERROR) {
                task = (TaskUtils.getUploadTaskInfoByTaskId(clientUrl, taskId).getJSONObject(ConstantUtil.RESULT))
                        .toJavaObject(Task.class);
                progress = task.TaskBaseInfo.Progress;
                sleep(2000);

            }
            Assert.assertEquals(progress, ConstantUtil.UPLOAD_ERROR);
        } else if (ConstantUtil.INTERNAL_ERROR.equals(error)) {

            Assert.assertNotEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        } else if (ConstantUtil.INVALID_PARAMS.equals(error)) {

            Assert.assertNotEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);

        }
    }


    @BeforeMethod(alwaysRun = true)
    public static void beforeMethod() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        deleteFileAndTaskAndSpace(clientUrl);
        SectorUtils.createSectorBeforeUploadFiles(1);
    }

    @AfterClass
    public static void afterClass() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        deleteFileAndTaskAndSpace(clientUrl);
    }
}
