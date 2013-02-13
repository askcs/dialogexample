package com.askcs.dialog.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
	protected String getFirstQuestion(String preferred_medium, String responder) {
		String questionID="1";
		Question question = loadQuestion(questionID, preferred_medium);
		
		return QuestionBuilder.build(question, getUrl(), responder);
	}	

	@Override
	protected String getUrl() {
		return DialogSettings.HOST+"/eventtest";
	}

	@Override
	public Response answerQuestion(String answer_json, String question_no,
			String preferred_medium, String responder) {
		
		String res="";
		
		Question q = loadQuestion(question_no, preferred_medium);
		if(q!=null)
			res = QuestionBuilder.build(q, getUrl(), preferred_medium, responder);
			
		return Response.ok(res).build();
	}
	
	@Path("timeout")
	@POST
	public Response getTimeout(String json, @QueryParam("preferred_medium") String preferred_medium) {
		String res="";
		Question q=null;
		if(preferred_medium==null) {
			preferred_medium="";	
		}
			
		try {
			ObjectMapper om = ParallelInit.getObjectMapper();
			EventPost event = om.readValue(json, EventPost.class);
			
			if(preferred_medium.endsWith("wav")) {
				if(event.getQuestion_id().equals("10") || event.getQuestion_id().equals("11")) {
					
					q = new Question("92","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=488.wav", Question.QUESTION_TYPE_COMMENT);
					q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
				// Maxtime: 737
				} else {
					q = new Question("90","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=737.wav", Question.QUESTION_TYPE_COMMENT);
					q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", event.getQuestion_id()))));
					q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
				}
			} else {
				q = new Question("90","That weird??", Question.QUESTION_TYPE_COMMENT);
				q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", event.getQuestion_id()))));
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
			}
			
			res = QuestionBuilder.build(q, getUrl(), preferred_medium, event.getResponder());
			
		} catch(Exception ex) {
		}
		
		return Response.ok(res).build();
	}
	
	@Path("exception")
	@POST
	public Response getException(String json, @QueryParam("preferred_medium") String preferred_medium) {
		log.setLevel(Level.INFO);
		log.info("Received exception: "+json);
		String res="";
		
		if(preferred_medium==null)
			preferred_medium="";
		
		try {
			ObjectMapper om = ParallelInit.getObjectMapper();
			EventPost event = om.readValue(json, EventPost.class);
			
			Question q=null;
			if(preferred_medium.endsWith("wav")) {
				if(event.getQuestion_id().equals("10") || event.getQuestion_id().equals("11")) {
					q = new Question("92","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=488.wav", Question.QUESTION_TYPE_COMMENT);
					q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
					// Incorrect: 738
				} else {
					q = new Question("91","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=738.wav", Question.QUESTION_TYPE_COMMENT);
					q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", event.getQuestion_id()))));
					q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
				}
			} else {
				if(event.getQuestion_id().equals("10") || event.getQuestion_id().equals("11")) {
					q = new Question("92","I am not sure what to say right now", Question.QUESTION_TYPE_COMMENT);
					q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
					// Incorrect: 738
				} else {
					q = new Question("91","You selected an invalid option. Please select a presented option.", Question.QUESTION_TYPE_COMMENT);
					q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", event.getQuestion_id()))));
					q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup"))));
				}
			}
			res = QuestionBuilder.build(q, getUrl(), preferred_medium, event.getResponder());
			
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
	
	private Question loadQuestion(String questionNo, String preferred_medium) {
		
		if(preferred_medium==null)
			preferred_medium="";
		
		Question q=null;
		if(preferred_medium.endsWith("wav")) {
			if(questionNo.equals("1")) {
				q = new Question("1","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=1255.wav", Question.QUESTION_TYPE_CLOSED); // Press 1 for yes, 2 for no.
				q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "10"),
																new Answer("", "11"))));
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception?preferred_medium="+preferred_medium))));
			} else if(questionNo.equals("10")) {
				q = new Question("10","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=1254.wav", Question.QUESTION_TYPE_REFERRAL); // Connect to other person
				q.setUrl("tel:0103032422");
				q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "3"))));
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception?preferred_medium="+preferred_medium))));
			} else if(questionNo.equals("11")) {
				q = new Question("11","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=1254.wav", Question.QUESTION_TYPE_REFERRAL); // Connect to other person
				q.setUrl("tel:0647771234");
				q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "3"))));
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception?preferred_medium="+preferred_medium))));
			} else if(questionNo.equals("3")) {
				q = new Question("3","http://ask70.ask-cs.nl/~ask/timeout/debug/getwav.php?id=727.wav", Question.QUESTION_TYPE_COMMENT); // Thank you
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium))));
			}
		} else {
			if(questionNo.equals("1")) {
				q = new Question("1","Are you coming to my party?", Question.QUESTION_TYPE_CLOSED); // Press 1 for yes, 2 for no.
				q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("Yes", "10"),
																new Answer("No", "11"))));
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception?preferred_medium="+preferred_medium))));
			} else if(questionNo.equals("10")) {
				q = new Question("10","Thanks, see you there!", Question.QUESTION_TYPE_COMMENT); // Connect to other person
				//q.setUrl("tel:0103032422");
				//q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "3"))));
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception?preferred_medium="+preferred_medium))));
			} else if(questionNo.equals("11")) {
				q = new Question("11","Thanks, Too bad!", Question.QUESTION_TYPE_COMMENT); // Connect to other person
				//q.setUrl("tel:0647771234");
				//q.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("", "3"))));
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_TIMEOUT,getUrl()+"/timeout?preferred_medium="+preferred_medium),
																				new EventCallback(EventCallback.EVENT_TYPE_EXCEPTION,getUrl()+"/exception?preferred_medium="+preferred_medium))));
			} else if(questionNo.equals("3")) {
				q = new Question("3","Uhm???", Question.QUESTION_TYPE_COMMENT); // Thank you
				q.setEvent_callbacks(new ArrayList<EventCallback>(Arrays.asList(new EventCallback(EventCallback.EVENT_TYPE_HANGUP,getUrl()+"/hangup?preferred_medium="+preferred_medium))));
			}
		}
		
		return q;
	}
}
