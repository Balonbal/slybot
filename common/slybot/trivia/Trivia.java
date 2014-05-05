package slybot.trivia;

import java.io.File;
import java.util.ArrayList;

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
