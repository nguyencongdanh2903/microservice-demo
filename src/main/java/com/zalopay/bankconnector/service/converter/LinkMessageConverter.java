package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.LinkRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;
import com.zalopay.bankconnector.web.rest.vm.response.InvokePaymentBankConnectorResponse;

import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.constant.CoreApiNameConst;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.NextActionEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class LinkMessageConverter extends ZP2BankCoreMessageConverter {

    @Override
    protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data, ActionResult actionResult) {

        try {

            LinkRequestData reqData = (LinkRequestData) data.dataObj;
            if (!Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CARDNUMBER))
                    .isEmpty()) {
                reqData.cardNo = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CARDNUMBER));
            } else {
                reqData.cardNo = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst
                        .PAYMENTINFO_ACCOUNTNUMBER));
            }
            reqData.holderName = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_HOLDERNAME));
            reqData.issueDate = "1117";
            reqData.cvv = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CVV));
            reqData.expireDate = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_EXPIREDDATE));
            reqData.description = (String) source.dataObj.description;
            data.accountNo = reqData.cardNo;

            //FIXME: need covert from ID - email - remaining fields
        } catch (Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }

    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target, ActionResult actionResult) {
        if (String.valueOf(6).equals(target.dataObj.bankReturnCode)) {
            target.dataObj.nextAction = NextActionEnum.VERIFY_OTP.getValue();
            target.dataObj.nextActionValue = CoreApiNameConst.VERIFY_OTP;
            target.dataObj.bankTransStatus = BankTransStatusEnum.PROCESSING.getValue();
            target.dataObj.returnCode = ReturnCodeEnum.PROCESSING.getValue() + "";
            target.dataObj.extraInfo.put(ApiParamsConst.EXTRAINFO_KYC_FIELDS, String.format("%s|%s", ApiParamsConst
                    .USER_IDNUMBER, ApiParamsConst.USER_PHONE_NO));
        }

    }

}
