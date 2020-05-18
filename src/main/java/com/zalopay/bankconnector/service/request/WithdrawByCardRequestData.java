package com.zalopay.bankconnector.service.request;

public class WithdrawByCardRequestData extends DefaultRequestData {

    /**
     * 
     */
    private static final long serialVersionUID = -7259461419695689896L;

    public String cardNo = "";
    public String holderName = "";
    public String cvv = "";
    public String issueDate = "";
    public String expireDate = "";
    public String amount = "";
    public String currency = "";
}
