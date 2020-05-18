/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zalopay.bankconnector.service.request;

import zalopay.bankconnector.bank.entity.ZP2BankBaseRequest;
import zalopay.bankconnector.util.BankConnectorLogger;
import zalopay.bankconnector.util.CloneUtils;

/**
 *
 * @author tuanln4 <tuanln4@vng.com.vn>
 */
public class DefaultRequest extends ZP2BankBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -8323947133823121947L;

    public String requestID = "";
    public String time = "";
    public String clientCode = "";
    public String signature = "";
    public String clientUserID = "";
    public String data = "";
    public transient DefaultRequestData dataObj = null;
    public String accountNo = "";

    @Override
    public String toMaskedString() {
        DefaultRequest cloneSource = null;
        try {
            cloneSource = (DefaultRequest) CloneUtils.clone(this);
            return cloneSource.toJsonString();

        } catch (Exception e) {
            BankConnectorLogger.ERROR_LOGGER.error(String.format("SBIS Request Error - clone source String data: %s", e.getMessage()));
            return null;
        }
    }
}
