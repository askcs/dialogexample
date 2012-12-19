package com.askcs.dialog.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.askcs.dialog.sdk.model.impl.Q_fields;
import com.askcs.dialog.sdk.model.intf.QuestionIntf;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.code.twig.annotation.Id;

public class Question implements QuestionIntf {
	private static final long serialVersionUID = -9069211642074173182L;
	
	public static final String QUESTION_TYPE_CLOSED = "closed";
	public static final String QUESTION_TYPE_OPEN = "open";
	public static final String QUESTION_TYPE_COMMENT = "comment";
	public static final String QUESTION_TYPE_REFERRAL = "referral";
	
	QuestionIntf question;
	private String preferred_language = "nl";
	private String expandedText="";
	@Id private String question_url="";
	private String base_url="";

	public Question() {
		this.question = new Q_fields(); // Default to simple in-memory class
	}
	
	public Question(String id, String text, String type) {
		this.question = new Q_fields(); // Default to simple in-memory class
		this.question.setQuestion_id(id);
		this.question.setQuestion_text(text);
		this.question.setType(type);
	}

	// Getters/Setters:
	@Override
	public String getQuestion_id() {
		return question.getQuestion_id();
	}

	@Override
	public String getQuestion_text() {
		return question.getQuestion_text();
	}

	@Override
	public String getType() {
		return question.getType();
	}

	@Override
	public String getUrl() {
		return question.getUrl();
	}

	@Override
	public String getRequester() {
		return question.getRequester();
	}
	@Override
	@JsonIgnore
	public HashMap<String,String> getExpandedRequester() {
		return question.getExpandedRequester(this.preferred_language);
	}
	@Override
	@JsonIgnore
	public HashMap<String,String> getExpandedRequester(String language) {
		this.preferred_language=language;
		return question.getExpandedRequester(language);
	}	
	@Override
	public ArrayList<Answer> getAnswers() {
		return question.getAnswers();
	}

	@Override
	public ArrayList<EventCallback> getEvent_callbacks() {
		return question.getEvent_callbacks();
	}
	
	public void setQuestion_expandedtext(String expandedText) {
		this.expandedText = expandedText;
	}

	@Override
	public String getQuestion_expandedtext() {
		return this.expandedText;
	}

	@Override
	@JsonIgnore
	public String getQuestion_expandedtext(String language) {
		this.preferred_language=language;
		return this.expandedText;
	}

	@Override
	public void setQuestion_id(String question_id) {
		question.setQuestion_id(question_id);
	}

	@Override
	public void setQuestion_text(String question_text) {
		question.setQuestion_text(question_text);
	}

	@Override
	public void setType(String type) {
		question.setType(type);
	}

	@Override
	public void setUrl(String url) {
		question.setUrl(url);
	}
	@Override
	public void setRequester(String requester) {
		question.setRequester(requester);
	}
	
	@Override
	public void setAnswers(ArrayList<Answer> answers) {
		question.setAnswers(answers);
	}

	@Override
	public void setEvent_callbacks(ArrayList<EventCallback> event_callbacks) {
		question.setEvent_callbacks(event_callbacks);
	}

	@JsonIgnore
	public String getPreferred_language() {
		return preferred_language;
	}

	@JsonIgnore
	public void setPreferred_language(String preferred_language) {
		this.preferred_language = preferred_language;
	}

	@Override
	public String getData() {
		return this.question.getData();
	}

	@Override
	public void setData(String data) {
		this.question.setData(data);
		
	}

	@Override
	public String getTrackingToken() {
		return this.question.getTrackingToken();
	}

	@Override
	public void setTrackingToken(String token) {
		this.question.setTrackingToken(token);
	}
	
	@JsonIgnore
	public String getQuestion_url() {
		return question_url;
	}
	
	public void setQuestion_url(String question_url) {
		this.question_url = question_url;
	}
	
	@JsonIgnore
	public String getBase_url() {
		return base_url;
	}
	
	public void setBase_url(String base_url) {
		this.base_url = base_url;
	}
	
	@JsonIgnore
	public QuestionIntf getInterface() {
		return this.question;
	}
}
