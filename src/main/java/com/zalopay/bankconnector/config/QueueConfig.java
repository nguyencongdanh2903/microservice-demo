package com.zalopay.bankconnector.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import zalopay.bankconnector.dao.DBPoolConfig;
import zalopay.bankconnector.entity.config.AMQueueConfigEntity;

@Component
@ConfigurationProperties(prefix = "queueConfigs")
public class QueueConfig {

	public Map<String, AMQueueConfigEntity> queueConfigs;
}
