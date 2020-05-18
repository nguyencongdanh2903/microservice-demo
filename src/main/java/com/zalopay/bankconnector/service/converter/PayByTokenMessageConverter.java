package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.PayByTokenRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.bank.converter.ZP2BankMessageConverter;
import zalopay.bankconnector.constant.CoreApiNameConst;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.NextActionEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class PayByTokenMessageConverter extends ZP2BankCoreMessageConverter {

    private static PayByTokenMessageConverter INSTANCE = new PayByTokenMessageConverter();

    private PayByTokenMessageConverter(){
    	
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

            PayByTokenRequestData reqData = (PayByTokenRequestData) data.dataObj;
            reqData.tokenNo = source.dataObj.bankToken;
            reqData.amount = String.valueOf(source.dataObj.amount);
            reqData.currency = Strings.nullToEmpty(source.dataObj.currency);
            
        } catch (Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }

    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target,
            ActionResult actionResult) {
        if ( String.valueOf(6).equals(target.dataObj.bankReturnCode)) {
            target.dataObj.nextAction = NextActionEnum.VERIFY_OTP.getValue();
            target.dataObj.nextActionValue = CoreApiNameConst.VERIFY_OTP;
            target.dataObj.bankTransStatus = BankTransStatusEnum.PROCESSING.getValue();
            target.dataObj.returnCode = ReturnCodeEnum.PROCESSING.getValue() + "";
        }
    }

}
