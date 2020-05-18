package com.zalopay.bankconnector.service.request;

public class PayByCardRequestData extends DefaultRequestData {

    /**
     * 
     */
    private static final long serialVersionUID = -2667948979426865453L;
    
    public String cardNo = "";
    public String holderName = "";
    public String cvv = "";
    public String issueDate = "";
    public String expireDate = "";
    public String amount = "";
    public String currency = "";

}
