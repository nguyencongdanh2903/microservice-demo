/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zalopay.bankconnector.constant;

import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.enums.SubTransTypeEnum;

/**
 *
 * @author
 */
public class ApiNameConst {

    public static final String LINK = "link";
    public static final String UNLINK = "unlink";
    public static final String RENEW_TOKEN = "renewtoken";
    public static final String PAY_BY_CARD = "pay";
    public static final String PAY_BY_TOKEN = "cashin";
    public static final String PAY_BY_QR = "paybyqr";
    public static final String WITHDRAW_BY_CARD = "withdraw";
    public static final String WITHDRAW_BY_TOKEN = "cashout";
    public static final String PUSH_PAYMENT = "pushpayment";
    public static final String REFUND = "refund";
    public static final String REVERT = "revert";
    public static final String VERIFY_OTP = "verifyotp";
    public static final String GENERATE_QR = "generateqr";
    public static final String QUERY_STATUS = "querystatus";
    public static final String ENROLLOP = "enrollop";
    public static final String FUNDTRANSFER = "fundTransfer";
    public static final String INQUIRYCARD = "inquiryCard";

    public static String getApiNameFromSubTransType(SubTransTypeEnum messageType, ActionResult actionResult) {
        switch (messageType) {
        case LINK: {
            return LINK;
        }
        case UNLINK: {
            return UNLINK;
        }
        case RENEW_TOKEN: {
            return RENEW_TOKEN;
        }
        case PAY: {
            return PAY_BY_CARD;
        }
        case PAY_BY_TOKEN: {
            return PAY_BY_TOKEN;
        }
        case WITHDRAW: {
            return WITHDRAW_BY_CARD;
        }
        case WITHDRAW_BY_TOKEN: {
            return WITHDRAW_BY_TOKEN;
        }
        case REFUND:
        case REFUND_BY_MANUAL:
        case REFUND_BY_REVERT:
        case REFUND_BY_WITHDRAW:
        case REFUND_BY_WITHDRAW_BY_TOKEN:{
            return REFUND;
        }
        
        case REVERT_FOR_PAY:
        case REVERT_FOR_PAY_BY_TOKEN:
        case REVERT_FOR_WITHDRAW:
        case REVERT_FOR_WITHDRAW_BY_TOKEN: {
            return REVERT;
        }
        
        case VERIFY_OTP: {
            return VERIFY_OTP;
        }
        case ROP_LINK: {
            return ENROLLOP;
        }
        case CHECK_BANK_TRANS:{
            return QUERY_STATUS;
        }
        case DOMESTIC_TRANSFER_FUND_2_CARD:{
        	return FUNDTRANSFER;
        }
        case DOMESTIC_INQUIRY:{
        	return INQUIRYCARD;
        }
        default:
            break;
        }
        return "";
    }

}
