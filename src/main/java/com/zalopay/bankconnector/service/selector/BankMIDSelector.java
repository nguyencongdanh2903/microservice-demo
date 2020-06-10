package com.zalopay.bankconnector.service.selector;

import com.zalopay.bankconnector.web.rest.vm.ActionResult;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;

public interface BankMIDSelector {

	String select(InvokePaymentBankConnectorRequest paymentRequest, ActionResult actionResult);

}
