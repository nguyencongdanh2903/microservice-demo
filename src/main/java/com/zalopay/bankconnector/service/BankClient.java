package com.zalopay.bankconnector.service;

import com.zalopay.bankconnector.service.request.ZP2BankBaseRequest;
import com.zalopay.bankconnector.service.response.ZP2BankBaseResponse;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;

public interface BankClient {

	ZP2BankBaseResponse request(ZP2BankBaseRequest bankClientRequest, ActionResult actionResult);

}
