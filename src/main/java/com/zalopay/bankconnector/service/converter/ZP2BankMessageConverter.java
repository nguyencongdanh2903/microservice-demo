package com.zalopay.bankconnector.service.converter;

import com.zalopay.bankconnector.service.request.ZP2BankBaseRequest;
import com.zalopay.bankconnector.service.response.ZP2BankBaseResponse;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;
import com.zalopay.bankconnector.web.rest.vm.response.InvokePaymentBankConnectorResponse;

public interface ZP2BankMessageConverter {

	ZP2BankBaseRequest convertRequest(InvokePaymentBankConnectorRequest source, ZP2BankBaseRequest target,
			ActionResult actionResult);

	InvokePaymentBankConnectorResponse convertResponse(ZP2BankBaseRequest sourceRequest,
			ZP2BankBaseResponse sourceResponse, InvokePaymentBankConnectorResponse target, ActionResult actionResult);
}
