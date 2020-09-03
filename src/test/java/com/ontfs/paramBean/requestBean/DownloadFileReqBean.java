package com.ontfs.paramBean.requestBean;

public class DownloadFileReqBean {
	public Object FileHash;
	public boolean InOrder;
	public int MaxPeerCnt;
	public Object OutFilePath;
	public Object DecryptPwd;

	public DownloadFileReqBean(Object fileHash, boolean inOrder, int maxPeerCnt, Object outFilePath,
			Object decryptPwd) {
		super();
		FileHash = fileHash;
		InOrder = inOrder;
		MaxPeerCnt = maxPeerCnt;
		OutFilePath = outFilePath;
		DecryptPwd = decryptPwd;
	}

}
