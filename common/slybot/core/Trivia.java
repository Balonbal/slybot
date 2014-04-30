package slybot.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Trivia {
	
	private File file;
	private ArrayList<Question> list;
	
	public Trivia(String fileName) {
		//Prohibit directory change
		if (fileName.contains("\\") || fileName.contains("/")) {
			return;
		} else {
			file = new File(fileName);
		}
		//Check if it's an existing trivia
		if (file.exists()) {
			loadTrivia();
		} else {
			createTrivia();
		}
	}

	public void addQuestion(String category, String question, String answer) {
		Question q = new Question(category, question, answer);
		list.add(q);
	}
	
	private void createTrivia() {
		
	}
	
	private void loadTrivia() {
		
	}
}

class Question {
	
	String category;
	String question;
	String answer;
	
	public Question(String category, String question, String answer) {
		this.category = category;
		this.question = question;
		this.answer = answer;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String getHint(int num) {
		Random r = new Random();
		//Loop through the answer and generate a random visible character to the answer
		for (int i = 0; i < answer.replaceAll(" ", "").length(); i+=3) {
			//TODO finish this
		}
		return null;
	}
}
