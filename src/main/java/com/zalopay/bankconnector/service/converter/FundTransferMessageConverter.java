package com.zalopay.bankconnector.service.converter;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.FundTransferRequestData;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.bank.converter.ZP2BankMessageConverter;
import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.constant.CoreApiNameConst;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorResponse;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.NextActionEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class FundTransferMessageConverter extends ZP2BankCoreMessageConverter {
	private static FundTransferMessageConverter INSTANCE = new FundTransferMessageConverter();

	private FundTransferMessageConverter() {

	}

	public static ZP2BankMessageConverter getInstance() {
		return INSTANCE;
	}

	@Override
	protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data,
			ActionResult actionResult) {
		FundTransferRequestData reqData = (FundTransferRequestData) data.dataObj;
		if (!Strings.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CARDNUMBER))
				.isEmpty()) {
			reqData.cardNo = Strings
					.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_CARDNUMBER));
		} else {
			reqData.cardNo = Strings
					.nullToEmpty((String) source.dataObj.paymentInfo.get(ApiParamsConst.PAYMENTINFO_ACCOUNTNUMBER));
		}
		reqData.amount = String.valueOf(source.dataObj.amount);
		reqData.description = (String) source.dataObj.description;
		data.accountNo = reqData.cardNo;
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