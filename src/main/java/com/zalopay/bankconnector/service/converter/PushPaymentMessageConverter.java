package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.PushPaymentRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class PushPaymentMessageConverter extends ZP2BankCoreMessageConverter {
    public static PushPaymentMessageConverter INSTANCE = new PushPaymentMessageConverter();

    private PushPaymentMessageConverter() {

    }

    public ZP2BankCoreMessageConverter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data, ActionResult actionResult) {

        try {

            PushPaymentRequestData reqData = (PushPaymentRequestData) data.dataObj;
            reqData.amount = String.valueOf(source.dataObj.amount);
            reqData.currency = Strings.nullToEmpty(source.dataObj.currency);

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