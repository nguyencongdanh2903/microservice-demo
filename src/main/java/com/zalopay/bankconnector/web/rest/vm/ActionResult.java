package com.zalopay.bankconnector.web.rest.vm;

import zalopay.bankconnector.entity.log.BaseLogEntity;

/**
 *
 * @author duyhv
 */
public class ActionResult extends BaseEntity {

	public int returnCode;
	public String returnMessage = "_";
	public String step = "_";
	public String stepResult = "_";
	public String exception = "_";
	public String extraInfo = "";

	public ActionResult() {

		this.returnCode = 0;
	}

	public ActionResult(int returnCode, String returnMessage) {

		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}

	public ActionResult(int returnCode, String returnMessage, String step, String stepResult, String exception) {

		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
		this.step = step;
		this.stepResult = stepResult;
		this.exception = exception;
	}

	public void fillFrom(BaseLogEntity logEntity) {

		this.step = logEntity.step;
		this.stepResult = logEntity.stepResult;
		this.exception = logEntity.exception;
	}

	public void cloneFrom(ActionResult iActionResult) {
        
        this.returnCode = iActionResult.returnCode;
        this.returnMessage = iActionResult.returnMessage;
        this.step = iActionResult.step;
        this.stepResult = iActionResult.stepResult;
        this.exception = iActionResult.exception;
        this.extraInfo = iActionResult.extraInfo;
    }
}