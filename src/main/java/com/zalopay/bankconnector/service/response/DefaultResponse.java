/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zalopay.bankconnector.service.response;

import zalopay.bankconnector.util.BankConnectorLogger;
import zalopay.bankconnector.util.CloneUtils;

/**
 *
 * @author tuanln4 <tuanln4@vng.com.vn>
 */
public class DefaultResponse extends ZP2BankBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 8896733531938690001L;

    public String code = "";
    public String requestId = "";
    public String time = "";
    public String caller = "";
    public String operation = "";
    public String signature = "";
    public String data = "";
    public String errorCode = "";
    public String errorMessage = "";
    public String status = "";
    public String responseID = "";
    public DefaultResponseData dataObj = null;

    public String accountNo = "";

    @Override
    public String toMaskedString() {
        try {
            DefaultResponse cloneData = (DefaultResponse) CloneUtils.clone(this);
            return cloneData.toJsonString();
        } catch (Exception e) {
            BankConnectorLogger.ERROR_LOGGER.error(String.format("SBIS Response clone data error: %s", e.getMessage()));
            return null;
        }
    }
}
