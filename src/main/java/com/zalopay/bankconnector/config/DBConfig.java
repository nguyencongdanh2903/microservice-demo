package com.zalopay.bankconnector.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import zalopay.bankconnector.dao.DBPoolConfig;

@Component
@ConfigurationProperties(prefix = "dbConfigs")
public class DBConfig {

	public Map<String, DBPoolConfig> dbConfigs;
}
