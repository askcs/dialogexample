package com.askcs.dialog.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.askcs.dialog.config.DialogSettings;
import com.askcs.dialog.sdk.DialogAgent;
import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.Question;
import com.askcs.util.ParallelInit;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("sven")
public class SvenExample extends DialogAgent {

	static final Logger log = Logger.getLogger(DialogAgent.class.getName());
	
	@Override
	protected void initQuestions() {

		// Create you questions
		Question question = new Question("1","Hoi hoe gaat het?", Question.QUESTION_TYPE_CLOSED);

		// Give in the answer the id of the question you want to receive next.
		question.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("Goed", "10"),new Answer("Niet Goed", "11"))));
		
		try {
			addQuestion(question);
			addQuestion(new Question("10","Beter, geniet ervan!", Question.QUESTION_TYPE_COMMENT));
			addQuestion(new Question("11","Dat is niet zo mooi!", Question.QUESTION_TYPE_COMMENT));
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
		return DialogSettings.HOST+"/sven";
	}

	@Override
	public Response answerQuestion(String answer_json, String question_no,
			String preferred_medium) {
		
		String res="";
		
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
