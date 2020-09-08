package com.ontfs.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.testng.Assert;
import org.testng.AssertJUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ontfs.paramBean.requestBean.DecryptFileReqBean;
import com.ontfs.paramBean.requestBean.DownloadFileReqBean;
import com.ontfs.paramBean.requestBean.ReqJson;
import com.ontfs.paramBean.requestBean.UploadFileReqBean;

public class FileUtils extends TestBase {
    private static Logger log = Logger.getLogger(FileUtils.class);

    /**
     * upload one file
     * @param url
     * @param filePath
     * @param fileDesc
     * @param pwd
     * @param expiredTime
     * @param copyNum
     * @param firstPdp
     * @param storageType
     * @return
     */
    public static JSONObject uploadFile(String url, Object filePath, Object fileDesc, Object pwd, Object expiredTime,
                                        Object copyNum, boolean firstPdp, Object storageType) {


//		String filePath = "";

//		if (CommonUtils.getWinOS()) {
//			log.info("os is windows");
//			filepath = file.getPath().replace("" + file.separatorChar, "\\\\");
//		} else {
//		log.info("os is unix");
//		filePath = file.getPath().replace("" + file.separatorChar, "/");

//		}

        UploadFileReqBean upload = new UploadFileReqBean(filePath, fileDesc, new File(filePath.toString()).length(), expiredTime, copyNum,
                storageType, firstPdp, pwd);

        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.UPLOAD_FILE, upload);
        log.info("uploadFile bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(url, bodyParams);
        } catch (UnirestException e) {
            log.error("上传报错", e);
        }
        return object;
    }

    /**
     * Calculate Upload Cost
     *
     * @param url
     * @param taskId
     * @param expiredtime
     * @param pdpinterval
     * @param copynum
     * @return
     */
	/*public static Boolean CalculateUploadCost(String url, String taskId, long expiredtime, int pdpinterval,
			int copynum) {
		// getfileInfo get filehash
		String fileHash = TaskUtils.getUploadFileHashByTaskId(url, taskId);

		if (!StringUtils.isBlank(fileHash)) {
			// getfileinfo
			JSONObject fileInfo = verifyFileInfoNotNull(url, fileHash);
			JSONObject result = (JSONObject) fileInfo.get(ConstantUtil.RESULT);
			String startTimeStr = result.get(ConstantUtil.TIME_START).toString().substring(0, 19);
			long startTime = CommonUtils.getTimeMillis(startTimeStr) / 1000;
			String str = result.get(ConstantUtil.PAY_AMOUNT).toString().split(" ")[0];
			double payAmount = new BigDecimal(str).multiply(new BigDecimal(10).pow(9)).doubleValue();
			int blockCount = (Integer) result.get(ConstantUtil.FILE_BLOCK_COUNT);
			long intervalMinute = (CommonUtils.formatTimeStampToHour(expiredtime)
					- CommonUtils.formatTimeStampToHour(startTime)) / 60 / 60;
			double expiredCost = intervalMinute * copynum * blockCount * result.getIntValue(ConstantUtil.CURRFEERATE);
			if (expiredCost == payAmount) {
				log.info("计费成功  文件" + fileHash);
				log.info("payAmount=" + payAmount);
				log.info("blockCount=" + blockCount);
				log.info("expiredCost=" + expiredCost);
				return true;
			}
			log.info("计费失败  文件" + fileHash);
			log.info("payAmount=" + payAmount);
			log.info("blockCount=" + blockCount);
			log.info("expiredCost=" + expiredCost);

		}
		return false;
	}*/

    /**
     * verify upload successful
     *
     * @param url
     * @param taskId
     * @return
     */
    public static Boolean verifyUploadSuccess(String url, String taskId) {
        JSONObject object = TaskUtils.getUploadTaskInfoByTaskId(url, taskId);
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
        int status = (Integer) ((JSONObject) (((JSONObject) object.get(ConstantUtil.RESULT))
                .get(ConstantUtil.TASK_BASE_INFO))).get(ConstantUtil.STATUS);
        int progress = (Integer) ((JSONObject) (((JSONObject) object.get(ConstantUtil.RESULT))
                .get(ConstantUtil.TASK_BASE_INFO))).get(ConstantUtil.PROGRESS);
        if (status == ConstantUtil.STATTASK_FINISH && progress == ConstantUtil.UPLOAD_DONE) {
            return true;
        } else if (progress == ConstantUtil.UPLOAD_ERROR) {
            return false;

        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                log.error("休眠失败", e);
            }
            return verifyUploadSuccess(url, taskId);
        }

    }

    /**
     * get file info
     *
     * @param url
     * @param fileHash
     * @return
     */
    public static JSONObject getFileInfo(String url, Object fileHash) {

        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_FILE_INFO, fileHash);
        log.info("getfileinfo bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(url, bodyParams);
        } catch (UnirestException e) {
            log.error("获取文件信息报错", e);
        }
        return object;
    }

    /**
     * verify FileInfo Not Null
     *
     * @param url
     * @param fileHash
     * @return
     */
    public static JSONObject verifyFileInfoNotNull(String url, String fileHash) {
        JSONObject object = getFileInfo(url, fileHash);
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
        Object result = object.get(ConstantUtil.RESULT);
        if (result == null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("休眠失败", e);
            }
            return verifyFileInfoNotNull(url, fileHash);
        }
        return object;
    }

    /**
     * Get the file hash that can be downloaded
     *
     * @param url
     * @param copynum
     * @return
     */
    public static JSONArray getDwonloadFile(String url, int copynum) {
        // get file list
        JSONObject fileList = getFileList(url);

        Assert.assertEquals(CommonUtils.getDesc(fileList), ConstantUtil.SUCCESS);
        Assert.assertEquals(CommonUtils.getError(fileList), ConstantUtil.SUCCESS_CODE);

        Object resultObj = fileList.get(ConstantUtil.RESULT);
        JSONArray array = new JSONArray();
        JSONArray strArray = new JSONArray();
        if (resultObj != null) {
            // Query whether the number of PDP submissions is equal to copynum
            JSONArray fileHashArray = (JSONArray) resultObj;
            // filter file list status=2 and progress=8
            array = getUploadedSuccessFileHash(url, fileHashArray);
            for (int i = 0; i < array.size(); i++) {
                // 验证节点有提交pdp
                Assert.assertTrue(verifyPdpCountEqualCopyNum(url, array.get(i).toString(), 1));
                // 使用@拼接filename 和filehash
                JSONObject fileinfo = (JSONObject) getFileInfo(url, array.get(i).toString()).get(ConstantUtil.RESULT);
                String str = array.get(i).toString() + "@" + fileinfo.get(ConstantUtil.FILE_DESC).toString();
                strArray.add(str);
            }

        }
        return strArray;
    }

    /**
     * Get the uploaded file hash
     *
     * @param url
     * @param fileHashArray
     * @return
     */
    private static JSONArray getUploadedSuccessFileHash(String url, JSONArray fileHashArray) {
        JSONArray array = new JSONArray();
        JSONObject resultObject = (JSONObject) TaskUtils.getAllUploadTaskList(url).get(ConstantUtil.RESULT);
        Map<String, Object> map = JSONObject.parseObject(resultObject.toString());
        Iterator<Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, Object> entry = entries.next();
//			String key = entry.getKey();
            JSONObject value = (JSONObject) entry.getValue();
            JSONObject baseInfo = (JSONObject) value.get(ConstantUtil.TASK_BASE_INFO);
            for (int i = 0; i < fileHashArray.size(); i++) {
                String hash = fileHashArray.get(i).toString();
                if (StringUtils.equals(baseInfo.get(ConstantUtil.FILEHASH).toString(), hash)
                        && (Integer) baseInfo.get(ConstantUtil.STATUS) == ConstantUtil.STATTASK_FINISH
                        && (Integer) baseInfo.get(ConstantUtil.PROGRESS) == ConstantUtil.UPLOAD_DONE) {
                    array.add(hash);
                }
            }
        }
        return array;
    }

    /**
     * verify whether the number of PDP submissions is equal to copynum
     *
     * @param url
     * @param fileHash
     * @param copynum
     * @return
     */
    public static boolean verifyPdpCountEqualCopyNum(String url, String fileHash, int copynum) {
        JSONObject pdpinfo = getPdpinfoList(url, fileHash);
        Object resultArray = pdpinfo.get(ConstantUtil.RESULT);
        if (StringUtils.equals(ConstantUtil.NULL_ARRAY, resultArray.toString())
                || ((JSONArray) resultArray).size() < copynum) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                log.error("休眠失败", e);
            }
            return verifyPdpCountEqualCopyNum(url, fileHash, copynum);
        }
        return true;

    }

    /**
     * get file list
     *
     * @param url
     * @return
     */
    public static JSONObject getFileList(String url) {
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_FILE_LIST);

        log.info("getFileList bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(url, bodyParams);
        } catch (UnirestException e) {
            log.error("获取文件列表报错", e);
        }
        return object;
    }

    /**
     * get pdpinfo list
     *
     * @param url
     * @param fileHash
     * @return
     */
    public static JSONObject getPdpinfoList(String url, Object fileHash) {
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_PDP_LIST, fileHash);
        log.info("getPdpinfoList bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(url, bodyParams);
        } catch (UnirestException e) {
            log.error("获取pdp 提交信息报错", e);
        }
        return object;

    }

    /**
     * download file
     * @param url
     * @param fileHash
     * @param inOrder
     * @param maxPeerCnt
     * @param outFilePath
     * @param pwd
     * @return
     */
    public static JSONObject downloadFile(String url, Object fileHash, boolean inOrder, int maxPeerCnt,
                                          Object outFilePath, Object pwd) {

        DownloadFileReqBean download = new DownloadFileReqBean(fileHash, inOrder, maxPeerCnt, outFilePath, pwd);
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.DOWNLOAD_FILE, download);
        log.info("downloadfile bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(url, bodyParams);
        } catch (UnirestException e) {
            log.error("提交下载任务信息报错", e);
        }
        return object;
    }

    /**
     * calculate download cost
     *
     * @param clientUrl
     * @param ontoUrl
     * @param serverAddress
     * @param taskId
     * @param fileHash
     * @param maxPeerCnt
     * @param preRestMoney
     * @param preNodeBlance
     * @return
     */
/*
	public static boolean calculateDownloadCost(String clientUrl, String ontoUrl, String[] serverAddress, String taskId,
			String fileHash, int maxPeerCnt, float preRestMoney, String[] preNodeBlance, int copyNum) {

		JSONObject readPledge = getFileReadPledge(clientUrl, fileHash);

		JSONObject pledge = readPledge.getJSONObject(ConstantUtil.RESULT);

		long calculateReadPledge = 0;
		JSONObject fileInfo = getFileInfo(clientUrl, fileHash);
		Object resultObject = fileInfo.get(ConstantUtil.RESULT);
		if (!StringUtils.equals(ConstantUtil.NULL, resultObject.toString())) {
			JSONObject result = (JSONObject) resultObject;
			float blockCount = Float.parseFloat(result.get(ConstantUtil.BLOCK_COUNT).toString());
//			int nodeNum = CommonUtils.parseInt(Math.ceil(blockCount / 32));
			int downnum;
			*/
    /*
     * if (maxPeerCnt > nodeNum) { downnum = nodeNum; } else { downnum = maxPeerCnt;
     * }
     *//*

     */
    /*
     * if (maxPeerCnt > copyNum) { downnum = copyNum; } else { downnum = maxPeerCnt;
     * }
     *//*

			downnum = pledge.getJSONArray(ConstantUtil.READ_PLANS).size();
			calculateReadPledge = (int) (downnum * blockCount * 256 * 1);
		}

		long restMoney = pledge.getLongValue(ConstantUtil.RESTMONEY);
		if (restMoney == calculateReadPledge) {
			log.info("下载计费成功：" + fileHash + " calculateReadPledge=" + calculateReadPledge + " restMoney=" + restMoney);
			return true;
		}
		log.info("下载计费失败：" + fileHash + " calculateReadPledge=" + calculateReadPledge + " restMoney=" + restMoney);

		return false;
	}
*/

    /**
     * verify download file success
     *
     * @param url
     * @param taskId
     * @return
     */
    public static boolean verifyDownloadSuccess(String url, String taskId) {
        // getDownloadTaskInfo
        JSONObject object = TaskUtils.getDownloadTaskinfoByTaskId(url, taskId);
        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
        int status = (Integer) ((JSONObject) (((JSONObject) object.get(ConstantUtil.RESULT))
                .get(ConstantUtil.TASK_BASE_INFO))).get(ConstantUtil.STATUS);
        int progress = (Integer) ((JSONObject) (((JSONObject) object.get(ConstantUtil.RESULT))
                .get(ConstantUtil.TASK_BASE_INFO))).get(ConstantUtil.PROGRESS);
        if (status == ConstantUtil.STATTASK_FINISH && progress == ConstantUtil.DOWNLOAD_DONE) {
            return true;
        } else if (progress == ConstantUtil.DOWNLOAD_ERROR) {
            return false;

        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                log.error("休眠失败", e);
            }
            return verifyDownloadSuccess(url, taskId);
        }

    }

    /**
     * delete files
     * @param url
     * @param fileHash
     * @return
     */
    public static JSONObject deleteFiles(String url, Object... fileHash) {

        String bodyParams;
        bodyParams = CommonUtils.getBodyParams(ConstantUtil.DELETE_FILE, fileHash);

        log.info("deleteFiles bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(url, bodyParams);
        } catch (UnirestException e) {
            log.error("删除文件报错", e);
        }
        return object;
    }

    /**
     * delete all files
     *
     * @param url
     * @return
     */
    public static void deleteAllFiles(String url) {
        // get file list
        JSONObject fileList = FileUtils.getFileList(url);
        Object result = fileList.get(ConstantUtil.RESULT);
        if (result != null) {
            JSONArray fileArray = (JSONArray) result;

            JSONObject object = deleteFiles(url, fileArray);
            Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
            AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

        }

    }

    /**
     * delete all files success
     *
     * @param urlRoot
     * @return
     */
    public static boolean verifyfDeleteAllFilesSuccess(String urlRoot) {
        // get file list
        JSONObject fileList = FileUtils.getFileList(urlRoot);
        Object result = fileList.get(ConstantUtil.RESULT);
        if (result == null) {
            return true;
        }
        return false;
    }

	/*public static int getPledgeCost(String urlRoot, String fileHash) {
		JSONObject pledge = getFileReadPledge(urlRoot, fileHash);
		int restMoney = 0;
		if (pledge.get(ConstantUtil.ERROR).toString().equals(ConstantUtil.SUCCESS_CODE)) {
			restMoney = (Integer) ((JSONObject) pledge.get(ConstantUtil.RESULT)).get(ConstantUtil.RESTMONEY);
		}
		return restMoney;
	}
*/

    /**
     * change file owner
     *
     * @param clientUrl
     * @param invalidFileHash
     * @param invalidWallet
     * @return
     */
    public static JSONObject changeFileOwner(String clientUrl, Object invalidFileHash, Object invalidWallet) {

        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.CHANGE_FILE_OWNER, invalidFileHash, invalidWallet);
        log.info("changeFileOwner bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(clientUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("更改文件所有权报错", e);
        }
        return object;
    }

    /**
     * verify change file owner success
     *
     * @param clientUrl
     * @param fileHash
     * @param nodeAddr
     * @return
     */
    public static boolean verifyChangeFileOwnerSuccess(String clientUrl, String fileHash, String nodeAddr,
                                                       String owner) {
        // verfiy get file list
        JSONObject fileList = getFileList(clientUrl);
        Assert.assertEquals(CommonUtils.getDesc(fileList), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(fileList).toString(), ConstantUtil.SUCCESS_CODE);
        JSONArray fileArray = fileList.getJSONArray(ConstantUtil.RESULT);
        Assert.assertNotEquals(fileArray.size(), 0);

        boolean filelistIsContains = fileList.toString().contains(fileHash);

        JSONObject fileInfo = getFileInfo(clientUrl, fileHash);
        boolean verifyOwner = owner.equals(CommonUtils.getResult(fileInfo).getString(ConstantUtil.FILE_OWNER));

        JSONObject challengeObject = ChallengeUtils.challenge(clientUrl, fileHash, nodeAddr);
        boolean isChallengeSuccess = ConstantUtil.SUCCESS.equals(CommonUtils.getDesc(challengeObject));
        if ((!isChallengeSuccess) && (!filelistIsContains) && verifyOwner) {
            return true;
        }
        return false;
    }

    /**
     * decrypt file
     *
     * @param clientUrl
     * @param sourceFilePath
     * @param decrptFilePath
     * @param invaildPwd
     * @return
     */
    public static JSONObject decryptFile(String clientUrl, Object sourceFilePath, Object decrptFilePath,
                                         Object invaildPwd) {

        DecryptFileReqBean decrypt = new DecryptFileReqBean(sourceFilePath, decrptFilePath, invaildPwd);
        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.DECRYPT_FILE, decrypt);
        log.info("decryptFile bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(clientUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("解密文件抛出异常", e);
        }
        return object;
    }

    /**
     * verify file MD5
     *
     * @param uploadFilePath
     * @param decrptFilePath
     * @return
     */
    public static boolean verifyDecryFileListMD5(String uploadFilePath, String decrptFilePath) {
        File uploadFile = new File(uploadFilePath);
        File decryFile = new File(decrptFilePath);

        if (uploadFile.isDirectory()) {
            File[] uploadFiles = uploadFile.listFiles();
            File[] decryFiles = decryFile.listFiles();
            List<String> uploadFilesMD5 = new ArrayList<String>();
            List<String> decryFilesMD5 = new ArrayList<String>();

            int length = decryFiles.length < uploadFiles.length ? decryFiles.length : uploadFiles.length;

            for (int i = 0; i < decryFiles.length; i++) {
                try {
                    decryFilesMD5.add(MD5Utils.getFileMD5String(decryFiles[i]));
                } catch (IOException e) {
                    log.error("文件MD5加密抛出异常", e);
                }

            }

            for (int i = 0; i < uploadFiles.length; i++) {

                try {
                    uploadFilesMD5.add(MD5Utils.getFileMD5String(uploadFiles[i]));
                } catch (IOException e) {
                    log.error("上传文件MD5加密失败", e);
                }
            }
            log.info("uploadFilesMD5=" + uploadFilesMD5);
            log.info("decryFilesMD5=" + decryFilesMD5);
            for (int i = 0; i < decryFilesMD5.size(); i++) {
                if (!uploadFilesMD5.contains(decryFilesMD5.get(i).trim())) {
                    log.info("解密MD5 不同：" + decryFilesMD5.get(i) + "  i=" + i);
                    return false;
                }
            }

            return true;
        } else {

            String uploadFileMD5 = null;
            String decryptFileMD5 = null;
            try {
                uploadFileMD5 = MD5Utils.getFileMD5String(uploadFile);

                decryptFileMD5 = MD5Utils.getFileMD5String(decryFile);
            } catch (IOException e) {
                log.error("MD5 加密失败", e);
            }
            if (StringUtils.equals(uploadFileMD5, decryptFileMD5)) {
                return true;
            }
            return false;
        }
    }

    /**
     * renew file
     *
     * @param clientUrl
     * @param fileHash
     * @param time
     * @return
     */
    public static JSONObject renewFile(String clientUrl, Object fileHash, Object time) {

        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.REWNEW_FILE, fileHash, time);
        log.info("renewFile bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(clientUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("文件延期抛出异常", e);
        }
        return object;
    }

    /**
     * verify file renew success
     *
     * @param clientUrl
     * @param fileHash
     * @param expiredtime
     * @param time
     * @return
     */
    public static boolean verifyFileRenewSuccess(String clientUrl, String fileHash, long expiredtime, int time) {
        // getfileinfo
        JSONObject fileinfo = getFileInfo(clientUrl, fileHash);

        String fileExpiredTime = CommonUtils.getResult(fileinfo).getString(ConstantUtil.TIME_EXPIRED).substring(0, 19);
        long timestamp = CommonUtils.getTimeMillis(fileExpiredTime) / 1000;
        log.info("timestamp=" + timestamp);
        log.info("expiredtime" + expiredtime);

        if (timestamp > expiredtime) {
            return true;
        }

        return false;
    }

    /**
     * get file read pledge
     *
     * @param clientUrl
     * @param fileHash
     * @return
     */
    public static JSONObject getFileReadPledge(String clientUrl, Object fileHash) {

        String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_FILE_READ_PLEDGE, fileHash);
        log.info("getFileReadPledge bodyParams is :" + bodyParams);
        JSONObject object = null;
        try {
            object = CommonUtils.postReqHttp(clientUrl, bodyParams);
        } catch (UnirestException e) {
            log.error("查询下载质押抛出异常", e);
        }
        return object;
    }

    /**
     * After uploading the file successfully, return to filehash
     *
     * @return
     */
    public static String getFileHashByUploadFile(int storgeType, Object... pwd) {
        // 上传文件

        Object[][] fileName = getFilesName();
//		File file = new File(uploadFilePath + "/" + fileName[0][0]);
        String password;
        if (pwd.length > 0) {
            password = pwd[0].toString();
        } else {
            password = "";
        }
        JSONObject obj = FileUtils.uploadFile(clientUrl, uploadFilePath + "/" + fileName[0][0], new Date().getTime() + "-" + fileName[0][0], password,
                expiredTime, copyNum, true, storgeType);
        Assert.assertEquals(CommonUtils.getDesc(obj), ConstantUtil.SUCCESS);
        AssertJUnit.assertEquals(CommonUtils.getError(obj), ConstantUtil.SUCCESS_CODE);
        String taskId = ((JSONObject) obj.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();
        // verify upload success
        Assert.assertTrue(FileUtils.verifyUploadSuccess(clientUrl, taskId));

        // getFileHash
        String fileHash = TaskUtils.getUploadFileHashByTaskId(clientUrl, taskId);
        return fileHash;
    }

    public static String getDownloadFilePathAfterDownloadFileSuccessfully(Object... pwd) {
        String fileHash;
        if (pwd.length > 0) {
            fileHash = FileUtils.getFileHashByUploadFile(1, pwd[0]);
        } else {
            fileHash = FileUtils.getFileHashByUploadFile(1);
        }
        String filePath = downloadFileDirectory + "/" + System.currentTimeMillis();
        JSONObject object = FileUtils.downloadFile(clientUrl, fileHash, true, maxPeerCnt, filePath, "");

        Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
        Assert.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);

        String taskId = ((JSONObject) object.get(ConstantUtil.RESULT)).get(ConstantUtil.TASK_ID).toString();

        // verify download file success
        Assert.assertTrue(FileUtils.verifyDownloadSuccess(clientUrl, taskId));
        return filePath;

    }

}
