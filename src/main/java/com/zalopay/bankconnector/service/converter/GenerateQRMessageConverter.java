package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.GenerateQRRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class GenerateQRMessageConverter extends ZP2BankCoreMessageConverter {

    public static GenerateQRMessageConverter INSTANCE = new GenerateQRMessageConverter();

    private GenerateQRMessageConverter() {

    }

    public ZP2BankCoreMessageConverter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data, ActionResult actionResult) {

        try {

            GenerateQRRequestData reqData = (GenerateQRRequestData) data.dataObj;
            reqData.amount = String.valueOf(source.dataObj.amount);
            reqData.currency = Strings.nullToEmpty((String) source.dataObj.currency);
            
        } catch (Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }

    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target, ActionResult actionResult) {

        if (BankTransStatusEnum.SUCCESSFUL.getValue().equals(target.dataObj.bankTransStatus)) {
            
        }

    }

}
