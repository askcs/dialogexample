package com.askcs.dialog.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.askcs.dialog.config.DialogSettings;
import com.askcs.dialog.sdk.QuestionBuilder;
import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.EventCallback;
import com.askcs.dialog.sdk.model.Question;
import com.askcs.dialog.servlet.DialogAgent;

@Path("reredirect")
public class ExtensiveRedirectExample extends DialogAgent {

	static final Logger log = Logger.getLogger(ExtensiveRedirectExample.class.getName());
	
	@Override
	protected void initQuestions() {
	}

	@Override
	protected String getFirstQuestion(String preferred_medium, String responder) {
		String questionID="1";
		Question question = loadQuestion(questionID);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("responder",responder);
		
		return QuestionBuilder.build(question, getUrl(), preferred_medium, params);
	}	

	@Override
	protected String getUrl() {
		return DialogSettings.HOST+"/reredirect";
	}

	@Override
	public Response answerQuestion(String answer_json, String question_no,
			String preferred_medium, String responder) {
		
		String res="";
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("responder",responder);
		
		Question q = loadQuestion(question_no);
		if(q!=null)
			res = QuestionBuilder.build(q, getUrl(), preferred_medium, params);
			
		return Response.ok(res).build();
	}
	
	@Path("timeout/{question_no}")
	@POST
	public Response getTimeout(@PathParam("question_no") String questionNo, String responder) {
		String res="";
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("responder",responder);
		
		Question q = loadQuestion(questionNo);
		if(q!=null)
			res = QuestionBuilder.build(q, getUrl(), null, params);
		
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
			q = new Question("1","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=488.wav", Question.QUESTION_TYPE_COMMENT);
			q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "2"))));
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"),
																			new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout/"+questionNo))));
		} else if(questionNo.equals("2")) {
			q = new Question("2","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=489.wav", Question.QUESTION_TYPE_REFERRAL);
			q.setUrl("tel:0643002549");
			q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "3"))));
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"),
																			new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout/"+questionNo))));
		} else if(questionNo.equals("3")) {
			q = new Question("3","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=490.wav", Question.QUESTION_TYPE_COMMENT);
			q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"),
																			new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout/"+questionNo))));
		}
		
		return q;
	}
}
