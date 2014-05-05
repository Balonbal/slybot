package slybot.trivia;

import java.util.Random;

public class Question {

	private String category;
	private String question;
	private String answer;
	String[] hints;
		
	public Question (String category, String question, String answer) {
		this(category, question, answer, generateHints(answer));
	}
	
	public Question (String category, String question, String answer, String[] hints) {
		this.category = category;
		this.question = question;
		this.answer = answer;
		this.hints = hints;
	}
	
	private static String[] generateHints(String answer) {
		
		//Make an array for the hints
		String[] r = new String[3];
		Random rand = new Random();
		char[] fullAnswer = answer.toCharArray();
		
		//Loop through the hint array
		for (int i = 0; i < r.length; i++) {
			//Set up a character array
			char[] s;
			
			//If it's the first itteration of the loop, fetch the string from answer (aka, no revealed characters)
			if (i == 0) {
				//Strip all characters and numbers from the original answer
				s = answer.replaceAll("[a-zA-Z0-9]", "*").toCharArray();
			}  else {
				//Fetch the answer with revealed characters
				s = r[i-1].toCharArray();
			}
			
			//Array for the position of revealed characters
			int[] rs = new int[(int) Math.ceil(s.length/3)];
			
			int j = 0;
			while (j < rs.length) {
				//Generate a position in the answer
				int pos = rand.nextInt(s.length);
				
				//Only select characters that are hidden
				if (s[pos] == '*') {
					
					boolean isInArray = false;
					//Make sure the character is not already selected to be revealed
					for (int k = 0; k < rs.length; k++) { 
						if (rs[k] == pos) { isInArray = true; }
					}
					if (!isInArray) {
						rs[j] = pos;
					}
				}
			}
			
			for (int k = 0; k < rs.length; k++) {
				//Override the revealed characters
				s[rs[k]] = fullAnswer[rs[k]];
			}
			
		}
		return r;
	}
	
	public String getCategory() {
		return category;
	}
		
	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
		
	public String getHint(int num) {
	
	if (num >= 0 && num < hints.length) {
		return hints[num];
	}
	
	return null;
	}

}
