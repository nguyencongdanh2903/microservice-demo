package com.zalopay.bankconnector.web.rest.vm.response;

import com.zalopay.bankconnector.web.rest.vm.ActionResult;

import zalopay.bankconnector.enums.BankConnectorReturnStatusEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;
import zalopay.bankconnector.util.BankConnectorLogger;
import zalopay.bankconnector.util.CloneUtils;
import zalopay.bankconnector.util.ExceptionUtil;
import zalopay.bankconnector.util.GsonUtils;

public class InvokePaymentBankConnectorResponse extends BankConnectorBaseResponse {

	private static final long serialVersionUID = 311808081331283677L;
	public transient InvokePaymentBankConnectorResponseData dataObj = new InvokePaymentBankConnectorResponseData();

	public void setResult(ActionResult actionResult) {
		if (actionResult.returnCode == ReturnCodeEnum.SUCCESSFUL.getValue()) {
			code = BankConnectorReturnStatusEnum.SUCCESSFUL.getValue();
		} else {
			if (actionResult.returnCode == ReturnCodeEnum.CALL_BANK_EXCEPTION.getValue()) {
				code = BankConnectorReturnStatusEnum.TIMEOUT.getValue();
			} else {
				code = BankConnectorReturnStatusEnum.FAIL.getValue();
				if (dataObj != null) {
					dataObj.returnCode = actionResult.returnCode + "";
				}
			}
		}
		data = dataObj.toJsonString();
		message = actionResult.toJsonString();
	}

	public String toMaskedString() {
		String maskingStr = null;
		try {
			InvokePaymentBankConnectorResponse response = (InvokePaymentBankConnectorResponse) CloneUtils.clone(this);
			if (response.dataObj != null) {
				response.dataObj.cleanData();
				response.data = GsonUtils.toJsonString(response.dataObj);
			}
			maskingStr = GsonUtils.toJsonString(response);
			response = null;
		} catch (Exception ex) {
			BankConnectorLogger.ERROR_LOGGER.error(
					String.format("toMaskedString: transID=%s, ex=%s", this.transID, ExceptionUtil.getExInfo(ex)));
		}
		return maskingStr;
	}

}
