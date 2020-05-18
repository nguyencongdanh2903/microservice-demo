package com.zalopay.bankconnector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bankConn")
public class BankConnConfig {

	public String connSystemName;
	public String connCode;
	public String connHashAlg;
	public String connHashKey1;
	public String connHashKey2;
	public boolean connIsCheckSig;
	public boolean connTestTimeout;
	public int connTestTimeoutDuration;
	public boolean connPrecheckConfig;
	public String connTraceNoPrefix;
	public String apiVersion;
	public String apiBaseUrl;
}
