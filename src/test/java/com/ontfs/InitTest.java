package com.ontfs;

import org.testng.annotations.Test;

import com.ontfs.utils.TestBase;


import org.testng.annotations.BeforeSuite;

public class InitTest extends TestBase {
	
	@Test
	public void init() {
		System.out.println(
				"==============================this is " + this.getClass().getName() + "============================");
		log.info("api test start init ...");
	}

	@BeforeSuite
	public void beforeSuite(){
		log.info("this is " + this.getClass().getName() );
		try {
			super.setUp();
		} catch (Exception e) {
			log.error("InitTest 的beforeSuite 抛出异常:",e);
		}
		

	}

//  @AfterSuite
//  public void afterSuite() throws UnirestException, InterruptedException {
//	  Assert.assertTrue(AccountUtils.isLoginIfSoLogout(url, urlRoot));
//  }

}
