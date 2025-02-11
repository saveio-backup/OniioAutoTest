package com.ontfs.scenarioTest.file;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontfs.utils.FileUtils;
import com.ontfs.utils.TestBase;

import org.testng.annotations.BeforeMethod;

import org.testng.annotations.AfterClass;

public class GetFileInfoTEst extends TestBase {
	@Test(groups  ="scenario")
	public void testGetFileInfoAfterDeleteFile() {
		// upload file
		String fileHash = FileUtils.getFileHashByUploadFile(1, 1,pwd);
		// delete file
		String[] array = new String[] { fileHash };
		JSON json = (JSON) JSON.toJSON(array);
		JSONObject object = FileUtils.deleteFiles(clientUrl, json);

	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
	}

}
