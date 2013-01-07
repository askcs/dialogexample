package com.askcs.dialog.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.askcs.dialog.config.DialogSettings;
import com.askcs.dialog.sdk.model.Answer;
import com.askcs.dialog.sdk.model.Question;
import com.askcs.dialog.servlet.DialogAgent;
import com.askcs.dialog.sdk.QuestionBuilder;

@Path("sven")
public class SvenExample extends DialogAgent {

	static final Logger log = Logger.getLogger(DialogAgent.class.getName());
	
	@Override
	protected void initQuestions() {
	}

	@Override
	protected String getFirstQuestion(String responder) {
		Question question = new Question("1","Hoi hoe gaat het?", Question.QUESTION_TYPE_CLOSED);
		question.setAnswers(new ArrayList<Answer>(Arrays.asList(new Answer("Goed", "10"),new Answer("Niet Goed", "11"))));
		
		return QuestionBuilder.build(question, getUrl());
	}

	@Override
	protected String getUrl() {
		return DialogSettings.HOST+"/sven";
	}

	@Override
	public Response answerQuestion(String answer_json, String question_no,
			String preferred_medium, String responder) {
		
		String res="";
		
		if(question_no.equals("10")) {
			res = QuestionBuilder.build(new Question("10","Beter, geniet ervan!", Question.QUESTION_TYPE_COMMENT), getUrl());
		} else if(question_no.equals("11")) {
			res = QuestionBuilder.build(new Question("11","Dat is niet zo mooi!", Question.QUESTION_TYPE_COMMENT), getUrl());
		}
		
		return Response.ok(res).build();
	}
	
	
}
