package com.zalopay.bankconnector.service.request;

public class WithdrawByTokenRequestData extends DefaultRequestData {

    /**
     * 
     */
    private static final long serialVersionUID = 9189895342955041386L;

    public String tokenNo = "";
    public String amount = "";
    public String currency = "";
}
