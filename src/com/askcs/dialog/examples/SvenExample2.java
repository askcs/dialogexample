package com.askcs.dialog.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.askcs.dialog.config.DialogSettings;
import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.Question;
import com.askcs.dialog.servlet.DialogAgent;
import com.askcs.dialog.state.StringStore;
import com.askcs.dialog.util.ParallelInit;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("sven2")
public class SvenExample2 extends DialogAgent {

	static final Logger log = Logger.getLogger(DialogAgent.class.getName());
	static final String SOUND_URL = "http://ask70.ask-cs.nl/~sven/test/";
	
	@Override
	protected void initQuestions() {

		// Create you questions
		Question question = new Question("1",SOUND_URL+"start.wav", Question.QUESTION_TYPE_COMMENT);

		// Give in the answer the id of the question you want to receive next.
		question.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer(SOUND_URL+"empty.wav", "10"))));
		
		try {
			addQuestion(question);
			
			question = new Question("10",SOUND_URL+"vraag.wav", Question.QUESTION_TYPE_COMMENT);
			question.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer(SOUND_URL+"empty.wav", "20"))));
			addQuestion(question);
			
			question = new Question("20",SOUND_URL+"empty.wav", Question.QUESTION_TYPE_COMMENT);
			question.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer(SOUND_URL+"empty.wav", "20"))));
			addQuestion(question);
			
			question = new Question("30",SOUND_URL+"bedankt.wav", Question.QUESTION_TYPE_COMMENT);
			addQuestion(question);
			
			
		} catch (Exception e) {
			log.warning("Failed to create a question: "+e.getMessage());
		}
	}

	@Override
	protected String getFirstQuestion(String responder) {
		ObjectMapper om = ParallelInit.getObjectMapper();
		String res="";
		try {
			res = om.writeValueAsString(addResponder(getQuestion("1"), responder));
		} catch(Exception ex) {
			
		}
		return res;
	}

	@Override
	protected String getUrl() {
		return DialogSettings.HOST+"/sven2";
	}

	@Override
	public Response answerQuestion(String answer_json, String question_no,
			String preferred_medium, String responder) {
		log.setLevel(Level.INFO);
		
		String res="";
		if(question_no.equals("10")) {
			try {
				log.info("Wait 10 secs");
				Thread.sleep(10000);
			} catch (Exception ex) {
				log.warning("Failed to wait???");
			}
		} else if(question_no.equals("20")) {
			
			String result = StringStore.getString("sven2_result");
			if(result!=null) {
				question_no="30";
				StringStore.dropEntity("sven2_result");
			} else {
				try {
					log.info("Wait 5 secs");
					Thread.sleep(5000);
				} catch (Exception ex) {
					log.warning("Failed to wait???");
				}
			}
		}
		
		Question question = getQuestion(question_no);
		//Question question = addResponder(getQuestion(question_no), responder);
		if(question!=null) {
			ObjectMapper om = ParallelInit.getObjectMapper();
			try {
				res = om.writeValueAsString(question);
			} catch(Exception ex) {
				log.warning("Failed to parse next question");
			}
		}
		
		return Response.ok(res).build();
	}
	
	@Path("result")
	@POST
	public Response setResult(String res) {
		if(res!=null) {
			StringStore.storeString("sven2_result", res);
		}
		
		return Response.ok("You entered: "+res).build();
	}
	
	protected Question addResponder(Question question, String responder) {
		
		if(!question.getQuestion_text().endsWith(".wav"))
			question.setQuestion_text(question.getQuestion_text()+"?responder="+responder);
		
		if(question.getAnswers()!=null) {
			for(Answer answer : question.getAnswers()) {
				
				answer.setCallback(answer.getCallback()+"?responder="+responder);
			}
		}
		
		return question;
	}
}