package com.zalopay.bankconnector.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zalopay.bankconnector.config.DataSourceEnum;
import zalopay.bankconnector.dao.config.CommonConfigLoader;
import zalopay.bankconnector.entity.ReturnCodeMappingEntity;

@Component
public class SBISDbConfig {

	@Autowired
	private DBConfig dbConfig;

	@Autowired
	private BankConnConfig bankConn;

	@Autowired
	private SBISConfigEntity sbisConfig;

	public Map<String, ReturnCodeMappingEntity> bankToZPMapCodes;

	@PostConstruct
	private void loadMapCode() {

		bankToZPMapCodes = CommonConfigLoader.getInstance(dbConfig.dbConfigs.get(DataSourceEnum.SETTING_DS.getName()))
				.loadBank2ZPMappingCode(bankConn.connSystemName, sbisConfig.bankCode);

//		logger.info(String.format("####################loadMapCode->DONE#####################"));
	}
}
