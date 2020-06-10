package com.zalopay.bankconnector.web.rest.vm.request;

import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.entity.TransEntity;
import zalopay.bankconnector.util.BankConnectorLogger;
import zalopay.bankconnector.util.CardUtil;
import zalopay.bankconnector.util.CloneUtils;
import zalopay.bankconnector.util.ExceptionUtil;
import zalopay.bankconnector.util.GsonUtils;

public class InvokePaymentBankConnectorRequest extends BankConnectorBaseRequest {

    private static final long serialVersionUID = 9175861182239010303L;
    public InvokePaymentBankConnectorRequestData dataObj = new InvokePaymentBankConnectorRequestData();

    public static InvokePaymentBankConnectorRequest fromTransEntity(TransEntity transEntity) {
        InvokePaymentBankConnectorRequest req = new InvokePaymentBankConnectorRequest();
        req.dataObj.amount = transEntity.amount;
        req.dataObj.bankCode = transEntity.bankCode;
        req.dataObj.bankToken = transEntity.bankToken;
        req.dataObj.currency = transEntity.currency;
        req.dataObj.description = transEntity.description;
        req.dataObj.extraInfo = transEntity.extraInfo;
        req.dataObj.userInfo = transEntity.userInfo;
        req.dataObj.linkAfterPay = transEntity.linkAfterPay;
        req.dataObj.paymentInfo = transEntity.paymentInfo;
        req.dataObj.phone = transEntity.phone;
        req.dataObj.renewToken = transEntity.renewToken;
        req.dataObj.tpeTransID = transEntity.merchantTransID;
        req.dataObj.transID = transEntity.transID;
        req.dataObj.refTransID = transEntity.refTransID;
        req.dataObj.refTraceNo = transEntity.refTraceNo;
        req.dataObj.traceNo = transEntity.traceNo;
        req.dataObj.transType = transEntity.transType;
        req.dataObj.subTransType = transEntity.subTransType;
        req.dataObj.userIP = transEntity.userIP;
        req.dataObj.zaloPayID = transEntity.zaloPayID;
        req.dataObj.transTime = transEntity.transTime;
        req.dataObj.otpValue = transEntity.otpValue;
        req.dataObj.tokenExpireDate = transEntity.tokenExpireDate;
        req.dataObj.tokenIssueDate = transEntity.tokenIssueDate;
        req.dataObj.requiredOTP = transEntity.requiredOTP;
        req.dataObj.refBankTransID = transEntity.refBankTransID;
        req.dataObj.connChannel = transEntity.connChannel;
        req.dataObj.appID = transEntity.appID;
        req.dataObj.mPIN = transEntity.mPIN;
        req.dataObj.bankUserId = transEntity.bankUserId;
        req.dataObj.noTransPerDevice = transEntity.noTransPerDevice;
        
        req.dataObj.refTransInfo = transEntity.refTransInfo;
        req.dataObj.bussinessType = transEntity.businessType;
        req.dataObj.bankTrace = transEntity.bankTrace;
        req.dataObj.bankMID = transEntity.bankMID;
        req.dataObj.authorizationCode = transEntity.authorizationCode;
        req.dataObj.fundType = transEntity.fundType;
        req.dataObj.cardScheme = transEntity.cardScheme;
        
        if (transEntity.extraInfo != null) {
            req.dataObj.extraInfo.put(ApiParamsConst.EXTRAINFO_BUSINESS_TYPE, transEntity.businessType);
            if (transEntity.extraInfo.containsKey(ApiParamsConst.REFTRANSTYPE)) {
                req.dataObj.refTransType = (int) transEntity.extraInfo.get(ApiParamsConst.REFTRANSTYPE);
            }
        }
        return req;
    }
    

	public String toMaskedString() {
		String maskingStr = null;
		try {
			InvokePaymentBankConnectorRequest request = (InvokePaymentBankConnectorRequest) CloneUtils.clone(this);
			if (request.dataObj != null) {
				request.dataObj.cleanData();
				request.cleanData();
			} else {
				request.data = CardUtil.masking(request.data);
			}
			maskingStr = GsonUtils.toJsonString(request);
			request = null;
		} catch (Exception ex) {
			BankConnectorLogger.ERROR_LOGGER.error(String.format("toMaskedString: transID=%s, ex=%s",
					this.dataObj.transID, ExceptionUtil.getExInfo(ex)));
		}
		return maskingStr;
	}

	public void cleanData() {
		this.data = null;
	}


}
