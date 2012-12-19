package com.askcs.dialog.sdk.model.impl;

import com.askcs.dialog.sdk.model.intf.AnswerIntf;

public class A_fields implements AnswerIntf {
	private static final long serialVersionUID = -2673880321244315796L;
	String answer_id;
	String answer_text;
	String callback;
	
	public A_fields() {}

	@Override
	public String getAnswer_id() {
		return answer_id;
	}

	@Override
	public String getAnswer_text() {
		return answer_text;
	}

	@Override
	public String getCallback() {
		return callback;
	}

	@Override
	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
	}

	@Override
	public void setAnswer_text(String answer_text) {
		this.answer_text = answer_text;
	}

	@Override
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	@Override
	public String getAnswer_expandedtext() {
		return getAnswer_expandedtext(null);
	}

	@Override
	public String getAnswer_expandedtext(String language) {
		// TODO Auto-generated method stub
		return null;
	}

}
