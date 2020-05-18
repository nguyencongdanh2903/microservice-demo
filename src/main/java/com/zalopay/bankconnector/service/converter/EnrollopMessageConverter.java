package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.EnrollopRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.constant.CoreApiNameConst;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.NextActionEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class EnrollopMessageConverter extends ZP2BankCoreMessageConverter {

    public static EnrollopMessageConverter INSTANCE = new EnrollopMessageConverter();
    
    private EnrollopMessageConverter() {
        
    }
    
    public ZP2BankCoreMessageConverter getInstance() {
        return INSTANCE;
    }
    @Override
    protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data, ActionResult actionResult) {
        try{

            EnrollopRequestData reqData = (EnrollopRequestData) data.dataObj;
            
            reqData.cardNo = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CARDNUMBER));
            reqData.holderName = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_HOLDERNAME));
            reqData.cvv = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CVV));
            reqData.issueDate = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_ISSUEDDATE));
            reqData.expireDate = Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_EXPIREDDATE));
            
            
        }catch(Exception e) {
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = ExceptionUtils.getMessage(e);
        }
        
    }

    @Override
    protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target, ActionResult actionResult) {
        if ( String.valueOf(6).equals(target.dataObj.bankReturnCode)) {
            target.dataObj.nextAction = NextActionEnum.VERIFY_OTP.getValue();
            target.dataObj.nextActionValue = CoreApiNameConst.VERIFY_OTP;
            target.dataObj.bankTransStatus = BankTransStatusEnum.PROCESSING.getValue();
            target.dataObj.returnCode = ReturnCodeEnum.PROCESSING.getValue() + "";
        }
        
    }

}
