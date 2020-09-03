package com.ontfs;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.testng.Assert;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.ontfs.utils.CommonUtils;
import com.ontfs.utils.FileUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
	String fileUrl = "http://localhost:10335/api/v1/dsp/file";
	String accountUrl = "http://localhost:10335/api/v1/account";
	String uploadFileUrl = fileUrl + "/upload";
	String accountSha256Pwd="3838bd5806d32cd91144865aa822b9551417dd2796c163d390baa7074d3067a7";
	String uploadFilePath = "D:/eclipse-workspace/SaveAutoTest/files/upload";
	String importWalletUrl = accountUrl + "/import/walletfile";
	String keystoreFilePath = "D:\\eclipse-workspace\\SaveAutoTest\\src\\test\\resources\\keystore.dat";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws UnirestException 
     * @throws InterruptedException 
     * @throws IOException 
     */
    public void testApp() throws UnirestException, InterruptedException, IOException
    {
    	StringBuffer buff = new StringBuffer();
    	System.out.println(buff.toString());
    }

	public void getFilesAndUpload() throws UnirestException, InterruptedException {
		File uploadFile = new File(uploadFilePath);
    	
		if(uploadFile.isDirectory())
		{
			String[] files = uploadFile.list();
			for(int i=0;i<files.length;i++)
			{
				System.out.println(files[i]);
				String path = uploadFilePath + "/" + files[i];
				uploadfiles(path);
			}
		}
	}

	public void uploadfiles(String filepath) throws UnirestException, InterruptedException {
		
//    	String filePath = ConfigUtils.getRootPath()+"files/upload/CoreFTP.zip";
//    	File file = new File(filepath);
//  	    JSONObject obj = UploadUtil.uploadOneFile(uploadFileUrl, file, accountSha256Pwd);
//  	    Assert.assertEquals(obj.get("Desc").toString(), "SUCCESS");
//  	    Assert.assertEquals(obj.getInt("Error"), 0);
  	  
  	  //Verify that the upload is actually completed
//  	  FileUtils.verifyTransComplete(fileUrl,1,10);
	}
    
    public void getfiles()
    {
    	
    	File uploadFile = new File(uploadFilePath);
    	
		if(uploadFile.isDirectory())
		{
			String[] files = uploadFile.list();
			Object[][] objArray = new Object[files.length][1];
			for(int i=0;i<files.length;i++)
			{
				System.out.println(files[i]);
				objArray[i][0] = files[i];
			}
		}
    }
}
