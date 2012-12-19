package com.askcs.dialog.sdk.model.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.EventCallback;
import com.askcs.dialog.sdk.model.intf.QuestionIntf;

public class Q_fields implements QuestionIntf {
	private static final long serialVersionUID = 748817624285821262L;
	
	String question_id;
	String question_text;
	String type;
	String url;
	String requester;
	String data;
	String token;

	ArrayList<Answer> answers;
	ArrayList<EventCallback> event_callbacks;
	
	public Q_fields(){}
	
	@Override
	public String getQuestion_id() {
		return question_id;
	}
	@Override
	public String getQuestion_text() {
		return question_text;
	}
	@Override
	public String getType() {
		return type;
	}
	@Override
	public String getUrl() {
		return url;
	}
	@Override
	public String getRequester() {
		return requester;
	}
	@Override
	public HashMap<String,String> getExpandedRequester(String language) {
		HashMap<String,String> result = new HashMap<String,String>(0);
		return result;
	}
	@Override
	public HashMap<String,String> getExpandedRequester() {
		return getExpandedRequester(null);
	}
	@Override
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	@Override
	public ArrayList<EventCallback> getEvent_callbacks() {
		return event_callbacks;
	}
	@Override
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	@Override
	public void setQuestion_text(String question_text) {
		this.question_text = question_text;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}
	@Override
	public void setEvent_callbacks(ArrayList<EventCallback> event_callbacks) {
		this.event_callbacks = event_callbacks;
	}
	@Override
	public void setRequester(String requester) {
		this.requester = requester;
	}

	@Override
	public String getQuestion_expandedtext(String language) {
		String text="";
		return text;
	}
	@Override
	public String getQuestion_expandedtext() {
		return getQuestion_expandedtext(null);
	}

	@Override
	public String getData() {
		return this.data;
	}

	@Override
	public String getTrackingToken() {
		return token;
	}

	@Override
	public void setData(String data) {
		this.data=data;
	}

	@Override
	public void setTrackingToken(String token) {
		this.token = token;
	}

}
