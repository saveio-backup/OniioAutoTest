package com.ontfs.paramBean.requestBean;

public class UploadFileReqBean {

	public Object FilePath;
	public Object FileDesc;
	public Object FileSize;
	public Object TimeExpired;
	public Object CopyNum;
	public Object StorageType;
	public boolean FirstPdp;
	public Object EncryptPassword;
	public Object ProveLevel;
	public UploadFileReqBean(Object filePath2, Object fileDesc, Object fileSize, Object timeExpired, Object copyNum,
			Object storageType, boolean firstPdp, Object encryptPassword,Object proveLevel) {
		super();
		FilePath = filePath2;
		FileDesc = fileDesc;
		FileSize = fileSize;
		TimeExpired = timeExpired;
		CopyNum = copyNum;
		StorageType = storageType;
		FirstPdp = firstPdp;
		EncryptPassword = encryptPassword;
		ProveLevel=proveLevel;
	}
	
	
}
