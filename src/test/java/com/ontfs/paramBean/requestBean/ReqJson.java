package com.ontfs.paramBean.requestBean;

public class ReqJson {
	public String jsonrpc;
	public String id;
	public String method;
	public Object params;

	public ReqJson( String method, Object params) {
		super();
		this.jsonrpc = "2.0";
		this.id = "1";
		this.method = method;
		this.params = params;
	}

}
