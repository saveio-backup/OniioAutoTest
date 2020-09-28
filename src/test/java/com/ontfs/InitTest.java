package com.ontfs;

import org.testng.annotations.Test;

import com.ontfs.utils.TestBase;


import org.testng.annotations.BeforeSuite;

public class InitTest extends TestBase {
	
	@Test
	public void initEnvironment() {
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		super.init();

	}

	@BeforeSuite
	public void beforeSuite(){
		log.info("=========The current method is " + Thread.currentThread().getStackTrace()[1].getMethodName());

		

	}

//  @AfterSuite
//  public void afterSuite() throws UnirestException, InterruptedException {
//	  Assert.assertTrue(AccountUtils.isLoginIfSoLogout(url, urlRoot));
//  }

}
