package com.ontfs.bodyParams.sector;

import com.alibaba.fastjson.JSONObject;
import com.ontfs.dataProvider.SectorDataProvider;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.ConstantUtil;
import com.ontfs.utils.SectorUtils;
import com.ontfs.utils.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CreateSectorTest extends TestBase {

    @Test(dataProvider = "invalidSectorId", dataProviderClass = SectorDataProvider.class,groups = "unusual")
    public void testCreateSectorWithInvalidSectorId(Object invalidSectorId) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], invalidSectorId, "3G", 1,sha256Pwd);
        if (String.valueOf(invalidSectorId).equals("0") || String.valueOf(invalidSectorId).equals("-1")) {
            Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.EXECUTE_ERROR);
        } else {
            Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.INVALID_SECTOR_ID);

        }
    }

    @Test(dataProvider = "invalidProveLevel", dataProviderClass = SectorDataProvider.class,groups = "unusual")
    public void testCreateSectorWithInvalidProveLevel(Object invalidProveLevel) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "12", "3G", invalidProveLevel,sha256Pwd);
        String code=CommonUtils.getError(object);
        if(code.equals(ConstantUtil.EXECUTE_ERROR)) {
            Assert.assertEquals(code, ConstantUtil.EXECUTE_ERROR);
        }else{
            Assert.assertEquals(code, ConstantUtil.INVALID_SECTOR_PROVE_LEVEL);
        }
    }

    @Test(dataProvider = "invalidSectorSize", dataProviderClass = SectorDataProvider.class,groups = "unusual")
    public void testCreateSectorWithInvalidSectorSize(Object invalidSectorSize) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "22", invalidSectorSize, 1,sha256Pwd);
       String code=CommonUtils.getError(object);
       if(code.equals(ConstantUtil.EXECUTE_ERROR)) {
           Assert.assertEquals(code, ConstantUtil.EXECUTE_ERROR);
       }else{
           Assert.assertEquals(code, ConstantUtil.INVALID_SECTOR_SIZE);
       }
    }

    /**
     * Use the same ID twice
     */
    @Test(groups = "unusual")
   public void testCreateSectorWhenUseTheSameSectorIdTwice(){
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        //first create sector
       JSONObject object = SectorUtils.createSector(serverUrlArray[0], "22", "1G", 1,sha256Pwd);
       Assert.assertEquals(CommonUtils.getError(object),ConstantUtil.SUCCESS_CODE);
       //second
       JSONObject object1 = SectorUtils.createSector(serverUrlArray[0], "22", "1G", 1,sha256Pwd);
       Assert.assertEquals(CommonUtils.getError(object1),ConstantUtil.EXECUTE_ERROR);
   }
    /**
     * Use the different ID twice
     */
    @Test(groups = "normal")
    public void testCreateSectorWhenUseTheDifferentSectorIdTwice(){
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        //first create sector
        JSONObject object = SectorUtils.createSector(serverUrlArray[0], "8", "1G", 1,sha256Pwd);
        Assert.assertEquals(CommonUtils.getError(object),ConstantUtil.SUCCESS_CODE);
        //second
        JSONObject object1 = SectorUtils.createSector(serverUrlArray[0], "9", "1G", 1,sha256Pwd);
        Assert.assertEquals(CommonUtils.getError(object1),ConstantUtil.SUCCESS_CODE);
    }

   @BeforeTest(alwaysRun = true)
    public static void BeforeTest(){
       log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        deleteFileAndTaskAndSpace(clientUrl);
        SectorUtils.deleteAllSector( serverAddressArray,serverUrlArray);
   }

}

