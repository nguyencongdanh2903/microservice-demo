package com.zalopay.bankconnector.service.utils;

import java.util.Map;

import org.springframework.stereotype.Service;

import zalopay.bankconnector.constant.AppConstant;
import zalopay.bankconnector.entity.ReturnCodeMappingEntity;
import zalopay.bankconnector.util.BankConnectorLogger;

@Service
public class MappingCodeUtil {

	private MappingCodeUtil() {

	}

	public static ReturnCodeMappingEntity mapBankToGatewayCode(String system, String bankCode, int transType,
			String bankReturnCode, Map<String, ReturnCodeMappingEntity> bankToGtwMapCodes) {

		String mapCodeName = AppConstant.getBank2ZPReturnCodeKey(system, bankCode, transType, bankReturnCode);

		if (bankToGtwMapCodes == null) {
			BankConnectorLogger.ERROR_LOGGER
					.error(String.format("action=MappingCodeUtil.mapBankToGatewayCode, error=MAP_RETURN_CODE_IS_NULL"));
			return new ReturnCodeMappingEntity();
		}

		Map<String, ReturnCodeMappingEntity> map = bankToGtwMapCodes;
		if (!map.containsKey(mapCodeName)) {
			return new ReturnCodeMappingEntity();
		}

		return map.get(mapCodeName);
	}

	public static ReturnCodeMappingEntity mapGatewayToBankCode(String system, String bankCode, int transType,
			int gatewayReturnCode, Map<String, ReturnCodeMappingEntity> gtwToBankMapCodes) {

		String mapCodeName = AppConstant.getZP2BankReturnCodeKey(system, bankCode, transType, gatewayReturnCode);

		if (!gtwToBankMapCodes.containsKey(mapCodeName)) {
			BankConnectorLogger.ERROR_LOGGER
					.error(String.format("action=MappingCodeUtil.mapBankToGatewayCode, error=MAP_RETURN_CODE_IS_NULL"));
			return new ReturnCodeMappingEntity();
		}

		Map<String, ReturnCodeMappingEntity> map = gtwToBankMapCodes;
		if (!map.containsKey(mapCodeName)) {
			return new ReturnCodeMappingEntity();
		}

		return map.get(mapCodeName);
	}

}