package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.QueryStatusRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;
import com.zalopay.bankconnector.web.rest.vm.response.InvokePaymentBankConnectorResponse;

import zalopay.bankconnector.bank.converter.ZP2BankMessageConverter;
import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.NextActionEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class QueryStatusMessageConverter extends ZP2BankCoreMessageConverter {

    @Override
    protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data, ActionResult actionResult) {

        try {
            data.requestID = source.dataObj.traceNo;
            QueryStatusRequestData reqData = (QueryStatusRequestData) data.dataObj;
            reqData.refRequestID = (String)source.dataObj.refTransInfo.get(ApiParamsConst.REF_TRACENO);

        } catch (Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }

    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target, ActionResult actionResult) {
        // TODO Auto-generated method stub
        try {
            // Currently only successful cases can be trusted
            if (target.dataObj.bankTransStatus.equals(BankTransStatusEnum.SUCCESSFUL.getValue())) {
                target.dataObj.nextAction = NextActionEnum.UPDATE_TRANS_STATUS.getValue();
            }
        } catch (Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }
    }

}
