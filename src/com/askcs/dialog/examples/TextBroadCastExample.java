package com.askcs.dialog.examples;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.askcs.dialog.sdk.QuestionBuilder;
import com.askcs.dialog.sdk.model.Question;

public class TextBroadCastExample extends HttpServlet {

	private static final long serialVersionUID = 900693350795921277L;

	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String servletURL = req.getRequestURL().toString().replace(req.getRequestURI(), req.getServletPath());
		String reqURL = req.getRequestURI().substring(1);
		String[] parts = reqURL.trim().split("/");
		String result = "{}";
		String message = req.getParameter("message");
		String responder = req.getParameter("responder");
		
		if(message!=null) {
			String questionNo = "1"; // Set a question number. This is to point from one question to another
			String type = Question.QUESTION_TYPE_COMMENT; // Type referral will send a message with expecting an answer.
			
			message = URLDecoder.decode(message, "UTF8");
			
			Question q = new Question(questionNo, message, type);
			
			if(parts.length==1) {
				
				result = QuestionBuilder.build(q,servletURL, responder);
			}
		}
		
		resp.getWriter().print(result);
	}
}
