package com.zalopay.bankconnector.service.converter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.RefundRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;
import com.zalopay.bankconnector.web.rest.vm.response.InvokePaymentBankConnectorResponse;

import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.constant.CoreApiNameConst;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.NextActionEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class RefundMessageConverter extends ZP2BankCoreMessageConverter {

	public static RefundMessageConverter INSTANCE = new RefundMessageConverter();

	private RefundMessageConverter() {

	}

	public static ZP2BankCoreMessageConverter getInstance() {
		return INSTANCE;
	}

	@Override
	protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data,
			ActionResult actionResult) {

		try {

			RefundRequestData reqData = (RefundRequestData) data.dataObj;
			reqData.amount = String.valueOf(source.dataObj.amount);

			String refTraceNo = (String) source.dataObj.refTransInfo.get(ApiParamsConst.REF_TRACENO);
			reqData.refRequestID = Strings.nullToEmpty(refTraceNo);

		} catch (Exception e) {
			actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
			actionResult.returnMessage = ExceptionUtils.getMessage(e);
		}

	}

	@Override
	protected void convertAdditionalResponseInfos(DefaultResponse data, InvokePaymentBankConnectorResponse target,
			ActionResult actionResult) {
		if (String.valueOf(6).equals(target.dataObj.bankReturnCode)) {
			target.dataObj.nextAction = NextActionEnum.VERIFY_OTP.getValue();
			target.dataObj.nextActionValue = CoreApiNameConst.VERIFY_OTP;
			target.dataObj.bankTransStatus = BankTransStatusEnum.PROCESSING.getValue();
			target.dataObj.returnCode = ReturnCodeEnum.PROCESSING.getValue() + "";
		}
	}

}
