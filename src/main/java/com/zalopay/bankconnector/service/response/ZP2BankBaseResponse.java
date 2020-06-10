package com.zalopay.bankconnector.service.response;

import com.zalopay.bankconnector.web.rest.vm.BaseEntity;

public abstract class ZP2BankBaseResponse extends BaseEntity {

	public transient String rawResponse = "";
	public transient int transType;
	public transient int subTransType;
	public transient int requiredOTP = 0;

	public abstract String toMaskedString();
}
