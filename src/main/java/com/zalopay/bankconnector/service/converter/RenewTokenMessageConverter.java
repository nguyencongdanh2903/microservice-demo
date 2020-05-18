package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.RenewTokenRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class RenewTokenMessageConverter extends ZP2BankCoreMessageConverter {

    public static RenewTokenMessageConverter INSTANCE = new RenewTokenMessageConverter();

    private RenewTokenMessageConverter() {

    }

    public ZP2BankCoreMessageConverter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data, ActionResult actionResult) {

        try {
            
            RenewTokenRequestData reqData = (RenewTokenRequestData) data.dataObj;
            reqData.tokenNo = Strings.nullToEmpty(source.dataObj.bankToken);

        } catch (Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }

    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target, ActionResult actionResult) {

        if (BankTransStatusEnum.SUCCESSFUL.getValue().equals(target.dataObj.bankTransStatus)) {
            /*
             * RenewTokenResponseData resData = (RenewTokenResponseData) data.dataObj;
             * resData.tokenNo = target.dataObj.bankToken; resData.tokenExpireDate =
             * target.dataObj.tokenExpireDate; resData.tokenIssueDate =
             * target.dataObj.tokenIssueDate;
             */
        }

    }

}
