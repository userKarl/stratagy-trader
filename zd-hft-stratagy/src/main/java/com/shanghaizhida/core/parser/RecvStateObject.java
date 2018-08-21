package com.shanghaizhida.core.parser;

import java.io.InputStream;

public class RecvStateObject {

	// 服务器返回的非压缩格式数据
	public InputStream inputStream = null;
	public Parser parser = null;
	public byte[] buffer = new byte[1024*8];
	public Object implProcessState = null;

	public String remoteIPPort;

}
