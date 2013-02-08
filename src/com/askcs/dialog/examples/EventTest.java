package com.askcs.dialog.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.askcs.dialog.config.DialogSettings;
import com.askcs.dialog.sdk.QuestionBuilder;
import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.EventCallback;
import com.askcs.dialog.sdk.model.EventPost;
import com.askcs.dialog.sdk.model.Question;
import com.askcs.dialog.servlet.DialogAgent;
import com.askcs.dialog.util.ParallelInit;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("eventtest")
public class EventTest extends DialogAgent {

static final Logger log = Logger.getLogger(EventTest.class.getName());
	
	@Override
	protected void initQuestions() {
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String getFirstQuestion(String responder) {
		String questionID="1";
		Question question = loadQuestion(questionID);
		
		return QuestionBuilder.build(question, getUrl(), responder);
	}	

	@Override
	protected String getUrl() {
		return DialogSettings.HOST+"/eventtest";
	}

	@SuppressWarnings("deprecation")
	@Override
	public Response answerQuestion(String answer_json, String question_no,
			String preferred_medium, String responder) {
		
		String res="";
		
		Question q = loadQuestion(question_no);
		if(q!=null)
			res = QuestionBuilder.build(q, getUrl(), responder);
			
		return Response.ok(res).build();
	}
	
	@SuppressWarnings("deprecation")
	@Path("timeout")
	@POST
	public Response getTimeout(String json) {
		String res="";
		
		try {
			ObjectMapper om = ParallelInit.getObjectMapper();
			EventPost event = om.readValue(json, EventPost.class);
			
			// Maxtime: 737
			Question q = new Question("90","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=737.wav", Question.QUESTION_TYPE_COMMENT);
			q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", event.getQuestion_id()))));
			
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
			
			res = QuestionBuilder.build(q, getUrl(), event.getResponder());
			
		} catch(Exception ex) {
		}
		
		return Response.ok(res).build();
	}
	
	@SuppressWarnings("deprecation")
	@Path("exception")
	@POST
	public Response getException(String json) {
		String res="";
		
		try {
			ObjectMapper om = ParallelInit.getObjectMapper();
			EventPost event = om.readValue(json, EventPost.class);
			
			// Incorrect: 738
			Question q = new Question("91","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=738.wav", Question.QUESTION_TYPE_COMMENT);
			q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", event.getQuestion_id()))));
			
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
			
			res = QuestionBuilder.build(q, getUrl(), event.getResponder());
			
		} catch(Exception ex) {
		}
		
		return Response.ok(res).build();
	}
			
	
	@Path("hangup")
	@POST
	public Response getHangup() {
		String res="";
		
		return Response.ok(res).build();
	}
	
	private Question loadQuestion(String questionNo) {
		
		Question q=null;
		if(questionNo.equals("1")) {
			q = new Question("1","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=1255.wav", Question.QUESTION_TYPE_CLOSED); // Press 1 for yes, 2 for no.
			q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "10"),
															new Answer("", "11"))));
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"),
																			new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout"),
																			new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception"))));
		} else if(questionNo.equals("10")) {
			q = new Question("10","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=1254.wav", Question.QUESTION_TYPE_REFERRAL); // Connect to other person
			q.setUrl("tel:0643002549");
			q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "3"))));
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"),
																			new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout"),
																			new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception"))));
		} else if(questionNo.equals("11")) {
			q = new Question("11","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=1254.wav", Question.QUESTION_TYPE_REFERRAL); // Connect to other person
			q.setUrl("tel:0647771234");
			q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "3"))));
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"),
																			new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout"),
																			new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception"))));
		} else if(questionNo.equals("3")) {
			q = new Question("3","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=727.wav", Question.QUESTION_TYPE_COMMENT); // Thank you
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
		}
		
		return q;
	}
}
