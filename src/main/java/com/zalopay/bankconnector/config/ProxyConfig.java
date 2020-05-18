package com.zalopay.bankconnector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "proxy")
public class ProxyConfig {

	public String proxyHost;
	public int proxyPort;
	public int callTimeOutMilis;
}
