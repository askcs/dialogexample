package com.askcs.dialog.sdk.model;

import java.util.UUID;

import com.askcs.dialog.sdk.model.impl.*;
import com.askcs.dialog.sdk.model.intf.AnswerIntf;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.code.twig.annotation.Id;

public class Answer implements AnswerIntf {
	private static final long serialVersionUID = -3770386829613266208L;
	
	AnswerIntf answer;
	@Id private String dsId="";
	private String expandedtext = "";
	
	public Answer(){
		this.answer = new A_fields();
	}
	public Answer(String answer_text){
		this.answer = new A_fields();
		this.answer.setAnswer_id(UUID.randomUUID().toString());
		this.answer.setAnswer_text(answer_text);
		
		this.dsId = this.answer.getAnswer_id();
	}
	public Answer(String answer_text, String callback){
		this.answer = new A_fields();
		this.answer.setAnswer_id(UUID.randomUUID().toString());
		this.answer.setAnswer_text(answer_text);
		this.answer.setCallback(callback);
		
		this.dsId = this.answer.getAnswer_id();
	}
	@JsonIgnore
	public String getDsId() { return this.dsId; }
	@Override
	public String getAnswer_id() { return answer.getAnswer_id(); }
	@Override
	public String getAnswer_text() { return answer.getAnswer_text(); }
	@Override
	public String getCallback() { return answer.getCallback(); }
	
	public void setAnswer_expandedtext(String expandedtext) {
		this.expandedtext=expandedtext;
	}
	
	@Override
	public String getAnswer_expandedtext() { return this.expandedtext; }
	@Override
	@JsonIgnore
	public String getAnswer_expandedtext(String language) {
		return this.expandedtext; 
	}

	@Override
	public void setAnswer_id(String answer_id) { answer.setAnswer_id(answer_id); }
	@Override
	public void setAnswer_text(String answer_text) { answer.setAnswer_text(answer_text); }
	@Override
	public void setCallback(String callback) { answer.setCallback(callback); }
	
	@JsonIgnore
	public AnswerIntf getInterface() { return this.answer; }
}
