package com.ontfs.bodyParams.sector;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.*;
import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DeleteSectorTest extends TestBase {

    @Test
    private void testDeleteSectorWhenSectorNoExist() {
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);
        JSONObject object = SectorUtils.deleteSector(serverUrlArray[0], "2");
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.EXECUTE_ERROR);
    }

    @Test
    private void testDeleteSectorWhenExistFile() {
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);

        //create sectoe
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "1", "1G", 1);
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

        //upload file
        String fileHash = FileUtils.getFileHashByUploadFile(1, 1);
        Assert.assertTrue(SectorUtils.verifyNodeContainFile(serverUrlArray[0], serverAddressArray[0], fileHash, 1));
        JSONObject deleteSector = SectorUtils.deleteSector(serverUrlArray[0], "1");
        Assert.assertEquals(CommonUtils.getError(deleteSector), ConstantUtil.EXECUTE_ERROR);

    }


    @Test(enabled = false)
    private void testDeleteSectorWhenDeleteFile() {
        SectorUtils.deleteAllSector(serverAddressArray, serverUrlArray);

        //create sectoe
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "1", "1G", 1);
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

        //upload file
        String fileHash = FileUtils.getFileHashByUploadFile(1, 1);
        Assert.assertTrue(SectorUtils.verifyNodeContainFile(serverUrlArray[0], serverAddressArray[0], fileHash, 1));
        //delete file
        JSONObject object1 = FileUtils.deleteFiles(clientUrl, fileHash);
        Assert.assertEquals(CommonUtils.getError(object1), ConstantUtil.SUCCESS_CODE);
        //delete sector
        JSONObject deleteSector = SectorUtils.deleteSector(serverUrlArray[0], "1");
        Assert.assertEquals(CommonUtils.getError(deleteSector), ConstantUtil.EXECUTE_ERROR);

    }

    @AfterTest(alwaysRun = true)
    public static void afterTest() {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        deleteFileAndTaskAndSpace(clientUrl);
    }

    @BeforeTest(alwaysRun = true)
    public static  void beforeTest(){
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        deleteFileAndTaskAndSpace(clientUrl);
    }

}
