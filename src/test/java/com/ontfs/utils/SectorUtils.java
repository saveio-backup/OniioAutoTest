package com.ontfs.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.Assert;

public class SectorUtils extends TestBase {

    /**
     * create sector
     *
     * @param serverUrl
     * @param sectorId
     * @param sectorSize
     * @param proveLevel
     * @return
     */
    public static JSONObject createSector(String serverUrl, Object sectorId, Object sectorSize, Object proveLevel) {
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.CREATE_SECTOR, sectorId, proveLevel, sectorSize);
        log.info("createSector bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(serverUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("抛出异常", e);
        }
        return object;
    }

    /**
     * delete sector
     *
     * @param serverUrl
     * @param sectorId
     * @return
     */
    public static JSONObject deleteSector(String serverUrl, Object sectorId) {
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.DELETE_SECTOR, sectorId);
        log.info("deleteSector bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(serverUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("抛出异常", e);
        }
        return object;
    }

    /**
     * get sector info
     *
     * @param serverUrl
     * @param sectorId
     * @return
     */
    public static JSONObject getSectorInfo(String serverUrl, Object sectorId) {
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_SECTOR_INFO, sectorId);
        log.info("getSectorInfo bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(serverUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("抛出异常", e);
        }
        return object;
    }

    /**
     * get sector info for node
     *
     * @param serverUrl
     * @param nodeAddr
     * @return
     */
    public static JSONObject getSectorInfosForNode(String serverUrl, Object nodeAddr) {
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_SECTOR_INFOS_FOR_NODE, nodeAddr);
        log.info("getSectorInfosForNode bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(serverUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("抛出异常", e);
        }
        return object;
    }

    /**
     * get local sector
     *
     * @param serverUrl
     * @param sectorId
     * @return
     */
    public static JSONObject getLocalSector(String serverUrl, Object sectorId) {
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_LOCAL_SECTOR, sectorId);
        log.info("getLocalSector bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(serverUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("抛出异常", e);
        }
        return object;
    }

    /**
     * create sector before upload files
     *
     * @return
     */
    public static void createSectorBeforeUploadFiles(int proveLevel) {
        log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());
        for (int i = 0; i < serverUrlArray.length; i++) {
            //get sector for node
            JSONObject sectors = getSectorInfosForNode(serverUrlArray[i], serverAddressArray[i]);
            Assert.assertEquals(CommonUtils.getError(sectors), ConstantUtil.SUCCESS_CODE);
            int count = CommonUtils.getResult(sectors).getIntValue("SectorCount");
            if (count > 0) {
                JSONArray sectorArray = CommonUtils.getResult(sectors).getJSONArray("SectorInfos");
                for (int j = 0; j < sectorArray.size(); j++) {
                     if(sectorArray.getJSONObject(j).getIntValue("ProveLevel")==proveLevel){
                        return;
                     }
                }

            }
            //delete file
            deleteFileAndTaskAndSpace(clientUrl);
            //delete sector
            deleteAllSector(serverAddressArray,serverUrlArray);
            //create sector
            JSONObject object = createSector(serverUrlArray[0], "111111", "3G", proveLevel);
            Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);

            //verify getsector
            JSONObject sectorObject = getSectorInfo(serverUrlArray[0], "111111");
            Assert.assertEquals(CommonUtils.getError(sectorObject), ConstantUtil.SUCCESS_CODE);
            Assert.assertTrue(CommonUtils.getResult(sectorObject) != null);
        }
    }


    public static void deleteAllSector(String[] serverAddress, String[] serverUrl) {
        for (int i = 0; i < serverUrl.length; i++) {
            //get sector for node
            JSONObject sectors = getSectorInfosForNode(serverUrl[i], serverAddress[i]);
            Assert.assertEquals(CommonUtils.getError(sectors), ConstantUtil.SUCCESS_CODE);
            int count = CommonUtils.getResult(sectors).getIntValue("SectorCount");
            if (count > 0) {
                JSONArray sectorArray = CommonUtils.getResult(sectors).getJSONArray("SectorInfos");
                for (int j = 0; j < sectorArray.size(); j++) {
                    //delete sector
                    JSONObject delete = deleteSector(serverUrl[i], sectorArray.getJSONObject(j).getString("SectorID"));
//                    Assert.assertEquals(CommonUtils.getError(delete), ConstantUtil.SUCCESS_CODE);
                }
            }

        }
    }

    /**
     * Verify that the sector contains the specified file
     *
     * @param serverUrl
     * @param sectorId
     * @param fileHash
     * @return
     */
    public static boolean verifySectorFile(String serverUrl, String sectorId, String fileHash) {

        //get sector
        JSONObject sectorObject = getSectorInfo(serverUrl, sectorId);
        Assert.assertEquals(CommonUtils.getError(sectorObject), ConstantUtil.SUCCESS_CODE);
        JSONObject sectorInfo = CommonUtils.getResult(sectorObject);
        if (sectorInfo.getJSONArray("FileList").contains(fileHash)) {
            return true;
        }
        return false;

    }

    public static boolean verifyNodeContainFile(String serverUrl, String serverAddress, String fileHash, int proveLevel) {

        //get sector
        JSONObject sectorObject = getSectorInfosForNode(serverUrl, serverAddress);
        Assert.assertEquals(CommonUtils.getError(sectorObject), ConstantUtil.SUCCESS_CODE);

        JSONObject sectors = getSectorInfosForNode(serverUrl, serverAddress);
        Assert.assertEquals(CommonUtils.getError(sectors), ConstantUtil.SUCCESS_CODE);
        int count = CommonUtils.getResult(sectors).getIntValue("SectorCount");
        if (count > 0) {
            JSONArray sectorArray = CommonUtils.getResult(sectors).getJSONArray("SectorInfos");
            for (int i = 0; i < sectorArray.size(); i++) {
                JSONObject sectorInfo = sectorArray.getJSONObject(i);
                if (sectorInfo.getJSONArray("FileList") != null) {
                    if (sectorInfo.getJSONArray("FileList").contains(fileHash) || sectorInfo.getIntValue("ProveLevel") == proveLevel) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * Verify getsector and getlocalsection Are the files in the same order
     *
     * @param serverUrlArray
     * @param serverAddressArray
     * @return
     */
    public static boolean verifySectorFileOrder(String[] serverUrlArray, String[] serverAddressArray) {
        //getSectorsForNode
        for (int i = 0; i < serverAddressArray.length; i++) {
            JSONObject sectorForNode = getSectorInfosForNode(serverUrlArray[i], serverAddressArray[i]);
            Assert.assertEquals(CommonUtils.getError(sectorForNode), ConstantUtil.SUCCESS_CODE);
            JSONArray sectorInfo = CommonUtils.getResult(sectorForNode).getJSONArray("SectorInfos");
            int sectorCount = CommonUtils.getResult(sectorForNode).getIntValue("SectorCount");
            for (int j = 0; j < sectorCount; j++) {
                String sectorId = sectorInfo.getJSONObject(j).getString("SectorID");
                //get sector info
                JSONObject getSectorObject = getSectorInfo(serverUrlArray[i], sectorId);
                Assert.assertEquals(CommonUtils.getError(getSectorObject), ConstantUtil.SUCCESS_CODE);
                JSONArray fileList1 = CommonUtils.getResult(getSectorObject).getJSONArray("FileList");

                //getlocalSector
                JSONObject getLocalSectorObject = getLocalSector(serverUrlArray[i], sectorId);
                Assert.assertEquals(CommonUtils.getError(getLocalSectorObject), ConstantUtil.SUCCESS_CODE);
                JSONArray fileLsit2 = CommonUtils.getResult(getLocalSectorObject).getJSONArray("FileList");
                if (fileList1 != null) {
                    for (int k = 0; k < fileList1.size(); k++) {
                        String filehash1 = fileList1.getString(k);
                        String filhash2 = fileLsit2.getJSONObject(k).getString("FileHash");
                        if (!filehash1.equals(filhash2)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
