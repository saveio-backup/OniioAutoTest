package com.ontfs.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.AssertJUnit;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TaskUtils {
	private static Logger log = Logger.getLogger(TaskUtils.class);

	/**
	 * delete task by taskID
	 * 
	 * @param url
	 * @param taskId
	 * @return
	 */
	public static JSONObject deleteTask(String url, Object taskId) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.DELETE_TASK, taskId);
		log.info("deleteTask bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("删除任务报错", e);
		}
		return object;
	}

	/**
	 * delete all tasks
	 * 
	 * @param url
	 */
	public static void deleteAllTasks(String url) {
		// get all taskidList
		ArrayList<String> taskIdList = getAllTasksList(url);
		for (String key : taskIdList) {
			JSONObject object = deleteTask(url, key);
 			Assert.assertEquals(CommonUtils.getError(object), ConstantUtil.SUCCESS_CODE);
		}

	}

	/**
	 * get all download TaskList
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject getAllDownloadTaskList(String url) {
	
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_ALL_DOWNLOAD_TASK_LIST);
		log.info("getAllDownloadTaskList bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("获取下载任务列表信息报错", e);
		}
		return object;
	}

	/**
	 * get download taskinfo by taskId
	 * 
	 * @param url
	 * @param taskId
	 * @return
	 */
	public static JSONObject getDownloadTaskinfoByTaskId(String url, Object taskId) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_DOWNLOAD_TASK_INFO_BY_ID,taskId);
		log.info("getDownloadTaskinfoById bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("查询下载任务信息报错", e);
		}
		return object;
	}

	/**
	 * get all uploadtask list
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject getAllUploadTaskList(String url) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_ALL_UPLOAD_TASK_LIST);
		log.info("getAllUploadTaskList bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("获取上传任务列表信息报错", e);
		}
		return object;
	}

	/**
	 * get upload task info by taskId
	 * 
	 * @param url
	 * @param taskId
	 * @return
	 */
	public static JSONObject getUploadTaskInfoByTaskId(String url, Object taskId) {
		
		String bodyParams = CommonUtils.getBodyParams(ConstantUtil.GET_UPLOAD_TASK_INFO_BY_ID, taskId);
		log.info("getuploadtaskinfobyid bodyParams is :" + bodyParams);
		JSONObject object = null;
		try {
			object = CommonUtils.postReqHttp(url, bodyParams);
		} catch (UnirestException e) {
			log.error("获取上传任务失败", e);
		}
		return object;
	}

	/**
	 * get upload filehash
	 * 
	 * @param url
	 * @param taskId
	 * @return
	 */
	public static String getUploadFileHashByTaskId(String url, String taskId) {
		JSONObject object = getUploadTaskInfoByTaskId(url, taskId);
		Assert.assertEquals(CommonUtils.getDesc(object), ConstantUtil.SUCCESS);
		AssertJUnit.assertEquals(CommonUtils.getError(object).toString(), ConstantUtil.SUCCESS_CODE);
		String fileHash = (String) ((JSONObject) (((JSONObject) object.get(ConstantUtil.RESULT))
				.get(ConstantUtil.TASK_BASE_INFO))).get(ConstantUtil.FILEHASH);
		int progress = (Integer) ((JSONObject) (((JSONObject) object.get(ConstantUtil.RESULT))
				.get(ConstantUtil.TASK_BASE_INFO))).get(ConstantUtil.PROGRESS);
		if (progress==ConstantUtil.UPLOAD_DONE || (!StringUtils.isBlank(fileHash) && progress>=ConstantUtil.UPLOAD_CONTRACT_STORE_FILES && progress!=ConstantUtil.UPLOAD_ERROR)) {

			return fileHash;
		}else if(progress==ConstantUtil.UPLOAD_ERROR) {
			return null;
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("休眠失败", e);
		}
		return getUploadFileHashByTaskId(url, taskId);

	}

	/**
	 * verify delete all task successfu
	 * @param url
	 * @return
	 */
	public static boolean verifyDeleteTasksSuccess(String url) {
		// TODO Auto-generated method stub
		ArrayList<String> taskList = getAllTasksList(url);
		if (taskList.size() > 0) {
			return false;
		}
		return true;

	}

	/**
	 * get all taskids
	 * 
	 * @param url
	 * @return
	 */
	public static ArrayList<String> getAllTasksList(String url) {
		// get all upload tasks
		JSONObject allUploadTasks = getAllUploadTaskList(url);
		Object uploadResult = allUploadTasks.get(ConstantUtil.RESULT);
		ArrayList<String> taskIdList = new ArrayList<String>();
		if (uploadResult != null) {
			Map<String, Object> UploadTaskMap = JSONObject.parseObject(uploadResult.toString());
			Iterator<Entry<String, Object>> entries = UploadTaskMap.entrySet().iterator();
			while (entries.hasNext()) {
				Entry<String, Object> entry = entries.next();
				String key = entry.getKey();
				taskIdList.add(key);
			}
		}
		// get all dawnload tasks
		JSONObject allDownloadTask = getAllDownloadTaskList(url);
		Object downloadResult = allDownloadTask.get(ConstantUtil.RESULT);
		if (downloadResult != null) {
			Map<String, Object> DownloadTaskMap = JSONObject.parseObject(downloadResult.toString());
			Iterator<Entry<String, Object>> entries = DownloadTaskMap.entrySet().iterator();
			while (entries.hasNext()) {
				Entry<String, Object> entry = entries.next();
				String key = entry.getKey();
				taskIdList.add(key);
			}
		}
		return taskIdList;

	}

}
