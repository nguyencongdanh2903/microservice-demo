package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.VerifyOTPRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.bank.converter.ZP2BankMessageConverter;
import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class VerifyOTPMessageConverter extends ZP2BankCoreMessageConverter {

    private static VerifyOTPMessageConverter INSTANCE = new VerifyOTPMessageConverter();

    private VerifyOTPMessageConverter(){
    	
    }
    
    public static ZP2BankMessageConverter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void convertAdditionalRequestInfos(
            InvokePaymentBankConnectorRequest source,
            DefaultRequest data,
            ActionResult actionResult) {

        try {
          

            VerifyOTPRequestData reqData = (VerifyOTPRequestData) data.dataObj;
            reqData.otpValue = source.dataObj.otpValue;
            
            String traceNo = (String)source.dataObj.refTransInfo.get(ApiParamsConst.REF_TRACENO);
            reqData.refRequestID = traceNo;
            
            String accountNo = (String)source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CARDNUMBER);
            data.accountNo = accountNo;

        } catch (Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }

    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target,
            ActionResult actionResult) {
    }

}
