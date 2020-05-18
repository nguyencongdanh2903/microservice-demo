package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.PayByQRRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class PayByQRMessageConverter extends ZP2BankCoreMessageConverter {

    public static PayByQRMessageConverter INSTANCE = new PayByQRMessageConverter();
    
    private PayByQRMessageConverter() {
        
    }
    
    public ZP2BankCoreMessageConverter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data, ActionResult actionResult) {
        try {
            
            PayByQRRequestData reqData = (PayByQRRequestData) data.dataObj;
            // FIXME: query data for qrData in source.dataObj.paymentInfo, fee, consumerPAN, consumerName
            //reqData.qrData = source.dataObj.paymentInfo.get(ApiParamsConst.PaymentINFO_);
            reqData.amount = String.valueOf(source.dataObj.amount);
            reqData.currency = Strings.nullToEmpty(source.dataObj.currency);
            
        }catch(Exception e){
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }

    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target, ActionResult actionResult) {
        // TODO Auto-generated method stub

    }

}
