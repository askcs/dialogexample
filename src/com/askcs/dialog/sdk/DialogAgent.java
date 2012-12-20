package com.askcs.dialog.sdk;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.Question;
import com.askcs.dialog.sdk.util.ParallelInit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

abstract public class DialogAgent {

	protected abstract void initQuestions();
	protected abstract Question getFirstQuestion();
	protected abstract String getUrl();
	
	@Path("/questions/{question_no}")
	@POST
	@Produces("application/json")
	@Consumes("*/*")
	public abstract Response answerQuestion(String answer_json, @PathParam("question_no") String question_no,@QueryParam("preferred_medium") String preferred_medium);
	
	public DialogAgent() {
		if(getFirstQuestion()==null)
			initQuestions();
		
		linkAnswers();
	}
	
	@GET
	@Produces("application/json")
	public Response firstQuestion(@QueryParam("preferred_medium") String preferred_medium) throws Exception{
		
		ObjectMapper om = ParallelInit.getObjectMapper();
		
		Question q = getFirstQuestion();
		String result = om.writeValueAsString(q);
		
		return Response.ok(result).build();
	}
	
	@Path("/questions/{question_no}")
	@GET
	@Produces("text/plain")
	@Consumes("*/*")
	public  Response getQuestionText(@PathParam("question_no") String question_no, @QueryParam("preferred_medium") String prefered_mimeType ){
		Question question = getQuestion(question_no);
		String result = question.getQuestion_expandedtext();
		
		return Response.ok(result).build();		
	}
	
	@Path("/answers/{answer_no}")
	@GET
	@Produces("text/plain")
	@Consumes("*/*")
	public Response getAnswerText(@PathParam("answer_no") String answer_no, @QueryParam("preferred_medium") String prefered_mimeType){
		
		Answer answer = getAnswer(answer_no);
		String result = answer.getAnswer_expandedtext();
		
		return Response.ok(result).build();
	}
	

	
	@Path("/reset")
	@GET
	@Produces("text/plain")
	public Response reset() throws Exception{
		
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore();
		
		Iterator<Question> q = datastore.find()
				.type(Question.class)
				.addFilter("base_url", FilterOperator.EQUAL, getUrl())
				.now();
		int count=0;
		while(q.hasNext()) {
			
			Question question = q.next();
			ArrayList<Answer> answers = question.getAnswers();
			if(answers!=null) {
				for(Answer a : answers) {
					datastore.delete(a.getInterface());
					datastore.delete(a);
				}
			}
			
			datastore.delete(question.getInterface());
			datastore.delete(question);
			count++;
		}		
		
		return Response.ok("Deleted "+count).build();
	}
	
	protected void addQuestion(Question question) throws Exception {
		
		if(question.getQuestion_id()==null || question.getQuestion_id().equals(""))
			throw new Exception("No question id is set");
		
		if(question.getQuestion_text()==null || question.getQuestion_text().equals(""))
			throw new Exception("No question text is set");
		
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore();
		
		String url = getUrl();
		String question_url = url+"/questions/";
		question.setBase_url(url);
		question.setQuestion_url(question_url+question.getQuestion_id());
		if(question.getQuestion_text().endsWith(".wav")) {
			if(question.getAnswers()!=null) {
				for(Answer answer : question.getAnswers()) {

					if(answer.getCallback()!=null)
						answer.setCallback(question_url+answer.getCallback());
				}
			}
		} else {
			question.setQuestion_expandedtext(question.getQuestion_text());
			question.setQuestion_text(question_url+question.getQuestion_id());
			
			if(question.getAnswers()!=null) {
				for(Answer answer : question.getAnswers()) {
					
					answer.setAnswer_expandedtext(answer.getAnswer_text());
					answer.setAnswer_text(url+"/answers/"+answer.getAnswer_id());
					if(answer.getCallback()!=null)
						answer.setCallback(question_url+answer.getCallback());
				}
			}
		}
		
		datastore.store(question);
	}
	
	public Question getQuestion(String id) {
		
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore();

		Question question = datastore.load(Question.class, getUrl()+"/questions/"+id);
		
		return question;
	}
	
	public Answer getAnswer(String id) {
		
		AnnotationObjectDatastore datastore = new AnnotationObjectDatastore();
		Answer answer = datastore.load(Answer.class, id);
		
		return answer;
	}
	
	private void linkAnswers() {
		
	}
	
}
