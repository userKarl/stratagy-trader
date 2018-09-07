package com.zd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
public class NettyGlobal {

	@Value("${netty.ctp.server.port}")
	public int nettyCtpServerPort;// CTP下单服务器端口

	@Value("${netty.ctp.server.host}")
	public String nettyCtpServerHost;// CTP下单服务器地址
}
