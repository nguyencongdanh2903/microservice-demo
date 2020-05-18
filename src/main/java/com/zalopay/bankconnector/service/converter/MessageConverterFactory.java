package com.zalopay.bankconnector.service.converter;

import zalopay.bankconnector.bank.converter.ZP2BankMessageConverter;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.enums.ReturnCodeEnum;
import zalopay.bankconnector.enums.SubTransTypeEnum;

public class MessageConverterFactory {

	private static MessageConverterFactory INSTANCE = new MessageConverterFactory();

	private MessageConverterFactory() {

	}

	public static MessageConverterFactory getInstance() {
		return INSTANCE;
	}

	public ZP2BankMessageConverter getZP2BankMessageConverter(int subTransType, ActionResult actionResult) {

		ZP2BankMessageConverter converter = null;
		SubTransTypeEnum subTransTypeEnum = SubTransTypeEnum.fromInt(subTransType);
		switch (subTransTypeEnum) {
		case PAY:
			converter = PayByCardMessageConverter.getInstance();
			break;
		case PAY_BY_TOKEN:
			converter = PayByTokenMessageConverter.getInstance();
			break;
		case WITHDRAW_BY_TOKEN:
			converter = WithdrawByTokenMessageConverter.getInstance();
			break;
		case WITHDRAW:
			converter = WithdrawByCardMessageConverter.getInstance();
			break;
		case LINK:
			converter = LinkMessageConverter.getInstance();
			break;
		case UNLINK:
			converter = UnlinkMessageConverter.getInstance();
			break;
		case VERIFY_OTP:
			converter = VerifyOTPMessageConverter.getInstance();
			break;
		case CHECK_BANK_TRANS:
			converter = QueryStatusMessageConverter.getInstance();
			break;
		case REFUND:
		case REFUND_BY_WITHDRAW:
		case REFUND_BY_REVERT:
		case REFUND_BY_WITHDRAW_BY_TOKEN:
			converter = RefundMessageConverter.getInstance();
			break;
		case REVERT_FOR_PAY:
		case REVERT_FOR_PAY_BY_TOKEN:
		case REVERT_FOR_WITHDRAW:
		case REVERT_FOR_WITHDRAW_BY_TOKEN:
			converter = RevertMessageConverter.getInstance();
			break;
		case DOMESTIC_INQUIRY:
			converter = InquiryCardMessageConverter.getInstance();
			break;
		case DOMESTIC_TRANSFER_FUND_2_CARD:
			converter = FundTransferMessageConverter.getInstance();
			break;

		default:
			actionResult.returnCode = ReturnCodeEnum.BANK_API_NOT_SUPPORTED.getValue();
			actionResult.returnMessage = "SubTransType not supported : " + subTransType;
			return converter;

		}

		actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();

		return converter;
	}

}
