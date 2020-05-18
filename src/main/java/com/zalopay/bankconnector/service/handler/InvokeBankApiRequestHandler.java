package com.zalopay.bankconnector.service.handler;

import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zalopay.bankconnector.config.QueueConfig;
import com.zalopay.bankconnector.config.SBISConfigEntity;
import com.zalopay.bankconnector.config.SBISDbConfig;
import com.zalopay.bankconnector.service.BankClientFactory;
import com.zalopay.bankconnector.service.converter.MessageConverterFactory;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.response.DefaultResponse;
import com.zalopay.bankconnector.service.selector.BankSelector;

import zalopay.bankconnector.bank.client.BankClient;
import zalopay.bankconnector.bank.converter.ZP2BankMessageConverter;
import zalopay.bankconnector.bank.entity.ZP2BankBaseRequest;
import zalopay.bankconnector.bank.entity.ZP2BankBaseResponse;
import zalopay.bankconnector.bank.handler.AbstractInvokeBankApiRequestHandler;
import zalopay.bankconnector.bank.selector.BankMIDSelector;
import zalopay.bankconnector.bank.validator.ZP2BankValidator;
import zalopay.bankconnector.config.bank.entity.BankConnConfig;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.bank.ZP2BankLogEntity;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;

public class InvokeBankApiRequestHandler extends AbstractInvokeBankApiRequestHandler {

	@Autowired
	private SBISConfigEntity sbisConfig;

	@Autowired
	private BankConnConfig bankConn;

	@Autowired
	private SBISDbConfig sbisDbConfig;

	@Autowired
	private QueueConfig queueConfigs;

	private static final Logger LOGGER = Logger.getLogger(InvokeBankApiRequestHandler.class);

	@Override
	protected ZP2BankMessageConverter getConverter(int transType, ActionResult actionResult) {

		// TODO Auto-generated method stub
		return MessageConverterFactory.getInstance().getZP2BankMessageConverter(transType, actionResult);
	}

	@Override
	protected BankClient getBankProcessor(ActionResult actionResult) {

		// TODO Auto-generated method stub
		return BankClientFactory.getInstance().getBankClient(actionResult);
	}

	@Override
	protected ZP2BankLogEntity addInvokeBankInfosToLogEntity(ZP2BankLogEntity logEntity,
			InvokePaymentBankConnectorRequest paymentRequest, InvokePaymentBankConnectorResponse paymentResponse,
			ZP2BankBaseRequest invokeBankRequest, ZP2BankBaseResponse invokeBankResponse, ActionResult actionResult) {

		if (invokeBankRequest != null) {
			DefaultRequest reqData = (DefaultRequest) invokeBankRequest;
			logEntity.bankAPIName = String.valueOf(reqData.subTransType);
			logEntity.requestID = reqData.requestID;
			logEntity.bankRequest = reqData.toJsonString();
			if (reqData.dataObj != null) {
				logEntity.bankReqData = reqData.dataObj.toJsonString();
			}
		}

		if (invokeBankResponse != null) {
			DefaultResponse repsData = (DefaultResponse) invokeBankResponse;
			logEntity.bankReturnCode = String.valueOf(repsData.code);
			logEntity.bankResponse = repsData.toJsonString();
			if (repsData.dataObj != null) {
				logEntity.bankTransID = repsData.dataObj.bankTransId;
				logEntity.bankRespData = repsData.dataObj.toJsonString();
			}
		}
		return logEntity;
	}

	@Override
	protected BankMIDSelector getBankMIDSelector(ActionResult actionResult) {

		return BankSelector.getInstance();
	}

}
