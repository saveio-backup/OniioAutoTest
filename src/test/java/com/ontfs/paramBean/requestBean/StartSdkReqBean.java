package com.ontfs.paramBean.requestBean;

public class StartSdkReqBean {
	
	public Object ChainRpcAddr;
	public Object WalletPath;
	public Object GasPrice;
	public Object GasLimit;
	public Object PdpVersion;
	public Object WalletPwd;
	public Object P2pProtocol;
	public Object P2pListenAddr;
	public Object P2pNetworkId;
	public Object TxTimeout;
	public StartSdkReqBean(Object chainRpcAddr, Object walletPath, Object gasPrice, Object gasLimit, Object pdpVersion,
			Object walletPwd, Object p2pProtocol, Object p2pListenAddr, Object p2pNetworkId, Object txTimeout) {
		super();
		ChainRpcAddr = chainRpcAddr;
		WalletPath = walletPath;
		GasPrice = gasPrice;
		GasLimit = gasLimit;
		PdpVersion = pdpVersion;
		WalletPwd = walletPwd;
		P2pProtocol = p2pProtocol;
		P2pListenAddr = p2pListenAddr;
		P2pNetworkId = p2pNetworkId;
		TxTimeout = txTimeout;
	}
	
	
	

}
