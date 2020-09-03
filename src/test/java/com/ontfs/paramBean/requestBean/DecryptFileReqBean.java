package com.ontfs.paramBean.requestBean;

public class DecryptFileReqBean {
	public Object FilePath;
	public Object OutFilePath;
	public Object DecryptPwd;

	public DecryptFileReqBean(Object filePath, Object outFilePath, Object decryptPwd) {
		super();
		FilePath = filePath;
		OutFilePath = outFilePath;
		DecryptPwd = decryptPwd;
	}

}
