package com.zalopay.bankconnector.service.request;

public class GenerateQRRequestData extends DefaultRequestData  {

    /**
     * 
     */
    private static final long serialVersionUID = -6423119837256827444L;

    public String amount = "";
    public String feeType = "";
    public String flatFee = "";
    public String percentageFee = "";
    public String currency = "";
    public String billNo = "";
}
