package com.zd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
public class NettyGlobal {

	@Value("${netty.central.server.port}")
	public int nettyCentralServerPort;// 中控服务器端口

	@Value("${netty.central.server.host}")
	public String nettyCentralServerHost;// 中控服务器地址
}
