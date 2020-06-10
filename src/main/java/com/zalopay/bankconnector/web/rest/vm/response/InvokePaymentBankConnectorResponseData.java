package com.zalopay.bankconnector.web.rest.vm.response;

import java.util.HashMap;
import java.util.Map;

import zalopay.bankconnector.entity.BaseMessage;
import zalopay.bankconnector.enums.NextActionEnum;

public class InvokePaymentBankConnectorResponseData extends BaseMessage {

	private static final long serialVersionUID = -995460698448412129L;

	public int nextAction = NextActionEnum.NONE.getValue();
	public String nextActionValue = "";
	public String bankTransID = "";
	public String bankReturnCode = "";
	public String bankTransStatus = "";
	public String bankReturnMessage = "";
	public String bankTransTime = "";
	public String bankToken = "";
	public String tokenExpireDate;
	public String tokenIssueDate;
	public String returnCode = "";
	public String bankMID = "";
	public String traceNo = "";
	public String bankTrace = "";
	public String authorizationCode = "";
	public Map<String, Object> extraInfo = new HashMap<>();

	public void cleanData() {
		bankToken = null;
		tokenExpireDate = null;
		tokenIssueDate = null;
	}
}
