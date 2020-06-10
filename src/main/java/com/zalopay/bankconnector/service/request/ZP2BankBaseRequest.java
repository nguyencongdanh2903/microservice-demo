package com.zalopay.bankconnector.service.request;

import zalopay.bankconnector.entity.BaseMessage;
import zalopay.bankconnector.enums.BusinessTypeEnum;
import zalopay.bankconnector.enums.ConnChannelTypeEnum;

public abstract class ZP2BankBaseRequest extends BaseMessage {

	private static final long serialVersionUID = -2345829013230230189L;

	public transient String rawRequest = "";
	public transient int subTransType = -1;
	public transient String bankAPIName = "";
	public transient String bankMID = "";
	public transient int requiredOTP = 0;
	public transient BusinessTypeEnum businessType = null;
	public transient ConnChannelTypeEnum connChannelType = null;
	public transient boolean isForcePayByCard = false;

	public abstract String toMaskedString();
}
