package com.askcs.dialog.examples;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.askcs.dialog.sdk.QuestionBuilder;
import com.askcs.dialog.sdk.model.Question;

public class RedirectExample extends HttpServlet {

	private static final long serialVersionUID = 6211945899338752375L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String servletURL = req.getRequestURL().toString().replace(req.getRequestURI(), req.getServletPath());
		
		@SuppressWarnings("unchecked")
		Map<String, String[]> params = req.getParameterMap();
		String phone = "0102250130";
		if(params.containsKey("phone")) {
			phone = params.get("phone")[0];
		}
		
		String prefMedium="audio/wav";
		if(params.containsKey("preferred_medium")) {
			prefMedium = params.get("preferred_medium")[0];
		}
		
		String reqURL = req.getRequestURI().substring(1);
		String[] parts = reqURL.trim().split("/");
		String result = "{}";
		
		// This agent only supports audio media
		if(prefMedium.endsWith("wav")) {
			String questionNo = "1"; // Set a question number. This is to point from one question to another
			String soundFile = ""; // The sound file must end with .wav and has to be a 8bit soundfile.
			String type = Question.QUESTION_TYPE_REFERRAL; // Type referral will redirect people
			
			Question q = new Question(questionNo, soundFile, type);
			q.setUrl("tel:"+phone); // Set the number to redirect to in the url
			
			if(parts.length==1) {
				
				result = QuestionBuilder.build(q,servletURL);
			}
		}
		
		resp.getWriter().print(result);
	}	
}
