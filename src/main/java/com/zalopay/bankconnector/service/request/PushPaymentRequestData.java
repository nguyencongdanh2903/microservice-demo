package com.zalopay.bankconnector.service.request;

public class PushPaymentRequestData extends DefaultRequestData {

    /**
     * 
     */
    private static final long serialVersionUID = -4440708904084557654L;

    public String merchantPAN = "";
    public String consumerPAN = "";
    public String consumerName = "";
    public String amount = "";
    public String fee = "";
    public String currency = "";
    public String billNo = "";

}
