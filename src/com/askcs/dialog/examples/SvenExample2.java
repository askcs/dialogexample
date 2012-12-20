package com.askcs.dialog.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.askcs.dialog.config.DialogSettings;
import com.askcs.dialog.sdk.DialogAgent;
import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.Question;
import com.askcs.dialog.sdk.util.ParallelInit;
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
			addQuestion(question);
		} catch (Exception e) {
			log.warning("Failed to create a question: "+e.getMessage());
		}
	}

	@Override
	protected Question getFirstQuestion() {
		return getQuestion("1");
	}

	@Override
	protected String getUrl() {
		return DialogSettings.HOST+"/sven2";
	}

	@Override
	public Response answerQuestion(String answer_json, String question_no,
			String preferred_medium) {
		
		String res="";
		if(question_no.equals("10")) {
			try {
				Thread.sleep(10000);
			} catch (Exception ex) {
				log.warning("Failed to wait???");
			}
		}
		
		Question question = getQuestion(question_no);
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
	
	
}