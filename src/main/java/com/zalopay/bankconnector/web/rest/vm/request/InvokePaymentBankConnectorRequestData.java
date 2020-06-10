package com.zalopay.bankconnector.web.rest.vm.request;

import java.util.HashMap;
import java.util.Map;

import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.entity.BaseMessage;
import zalopay.bankconnector.enums.ConnChannelTypeEnum;

public class InvokePaymentBankConnectorRequestData extends BaseMessage {

    private static final long serialVersionUID = -995460698448412129L;

    public String zaloPayID = "";
    public String bankCode = "";
    public int transType;
    public int subTransType;
    public String tpeTransID;
    public long transID;
    public long refTransID;
    @Deprecated
    public String refBankTransID = "";
    @Deprecated
    public String refTraceNo = "";
    @Deprecated
    public int refTransType;
    public String bankMID = "";
    public String traceNo = "";
    public String bankTrace = "";
    public String phone = "";
    public String bankToken = "";
    public String tokenIssueDate = "";
    public String tokenExpireDate = "";
    public long amount;
    public String currency;
    public Map<String, Object> paymentInfo = new HashMap<>();
    public Map<String, Object> userInfo = new HashMap<>();
    public int pmcID;
    public int linkAfterPay;
    public int renewToken;
    public String userIP = "";
    public String description = "";
    public Map<String, Object> extraInfo = new HashMap<>();
    public long transTime;
    public String otpValue;
    public int requiredOTP;
    public int appID = -1;
    public String bussinessType;
    public Map<String, Object> refTransInfo;
    public String mPIN;
    public String noTransPerDevice;
    public String bankUserId;
    public String authorizationCode = "";
    public String fundType;
    public String cardScheme;
    public String connChannel = ConnChannelTypeEnum.WALLET.getValue();
    
    public void cleanData() {
    	paymentInfo = null;
		bankToken = null;
		tokenExpireDate = null;
		tokenIssueDate = null;
		otpValue = null;
		mPIN = null;

		if (extraInfo != null) {
			extraInfo.remove(ApiParamsConst.REFTRANSENTITY);
			extraInfo.remove(ApiParamsConst.REFOTPTRANSENTITY);
		}
    }
}
