package slybot.challenges;

import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.lib.Reference;

public class ChallengeTickTackToe extends MultiTurnChallenge {
	
	String[][] board;
	boolean hostTurn;
	String hostIcon;
	String challengeIcon;

	public ChallengeTickTackToe(Channel chan, User initializer, String challenged, String[] parameters, int timeOutInSeconds) {
		super(chan, initializer, challenged, "TickTackToe" , parameters, timeOutInSeconds, Reference.TTT_NEXT_TURN_TIMEOUT);
		board = new String[][] { 
				{ "null", "null", "null" },
				{ "null", "null", "null" },
				{ "null", "null", "null" } 
				};
		
		//Choose a random person to make the first turn
		Random r = new Random();
		hostTurn = (r.nextBoolean());
		
		//Set the icon for each player
		if (hostTurn) {
			hostIcon = "X";
			challengeIcon = "O";
		} else {
			hostIcon = "O";
			challengeIcon = "X";
		}
	}
	
	@Override
	public void initialize() {
		getChannel().send().message(getHost().getNick() + " and " + getChallengedUser() + " decide to settle their disputes by an epic game of Tick Tack Toe!");
		getChannel().send().message(getHost().getNick() + " will be playing as " + hostIcon);
		getChannel().send().message(getChallengedUser()+ " will be playing as " + challengeIcon);
		printBoard();
		getChannel().send().message((hostTurn?getHost().getNick():getChallengedUser()) + " will start of this legendary battle!");
	}
	
	private boolean updateBoard(int x, int y) {
		//Check if the position is occupied
		if (board[y][x] == "null") {
			//put the icon in the board
			board[y][x] = (hostTurn?hostIcon:challengeIcon);
			System.out.println("true");
			return true;
		}
		return false;
	}
	
	public boolean doTurn(User u, String params[]) {
		int x=-1, y=-1;
		
		if (hostTurn) {
			if (u.getNick().equalsIgnoreCase(getHost().getNick())) {
				x = Integer.parseInt(params[0]);
				y = Integer.parseInt(params[1]);
			}
		} else {
			if (u.getNick().equalsIgnoreCase(getChallengedUser())) {
				x = Integer.parseInt(params[0]);
				y = Integer.parseInt(params[1]);
			}
		}
		
		System.out.println("cp -a " + x + "," + y);
		
		if (updateBoard(x,y)) {
			printBoard();
			
			System.out.println("cp abba");
			//Get the results from this move
			String s = getResults();
			System.out.println("cp a ");
			if (!s.equalsIgnoreCase("null")) {
				end(s);
			} else {
			System.out.println("cp b");
			hostTurn = !hostTurn;
			
			getChannel().send().message((hostTurn ? getHost().getNick() : getChallengedUser()) + ": your turn!");
			return true;
			}
		}
		return false;
	}
	
	public void printBoard() {
		System.out.println("printing board");
		for (String[] s: board) {
			getChannel().send().message((s[0].equals("null") ? "_" : s[0]) + "|" + (s[1].equals("null") ? "_" : s[1]) + "|" + (s[2].equals("null") ? "_" : s[2]));
		}
	}
	
	public String getResults() {
		
		System.out.println("cp a ");
		for (int i = 0; i < board.length; i++) {
			//Check the board vertically
			if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && board[i][0] != "null") {
				return board[i][0];
			//check the board horizontally
			} else if (board[0][i].equals(board[1][i]) &&  board[1][i].equals(board[2][i]) && board[0][i] != "null") {
				return board[0][i];
			}
		}
		
		//check the board diagonally
		if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && board[0][0] != "null") {
			return board[0][0];
		} else if (board[2][0].equals(board[1][1]) && board[2][0].equals(board[0][2]) && board[2][0] != "null") {
			return board[2][0];
		}
		
		//Check if the board is full
		boolean full = true;
		for (String[] s: board) {
			for (String str: s) {
				if (str.equalsIgnoreCase("null")) {
					full = false;
				}
			}
		}
		
		if (full) {
			return "full";
		}
		System.out.println("cp a ");
		return "null";
	}

	@Override
	public User run(SlyBot bot, User usera, User userb, Channel channel,
			String[] params) {
		return null;
	}

	@Override
	public String getDescription() {
		return "Tick Tack Toe";
	}
	
	@Override
	public void end(String winner) {
		completed = true;
		getChannel().send().message("The game is over!");
		if (winner.equalsIgnoreCase(hostIcon)) {
			getChannel().send().message(getHost().getNick() + " has defeated the noob " + getChallengedUser());
		} else if (winner.equalsIgnoreCase(challengeIcon)) {
			getChannel().send().message(getHost().getNick() + " fought until the bitter end, but could not keep up with " + getChallengedUser());
		} else {
			getChannel().send().message("The game is a tie! Both players are officialy noobs.");
		}
		Main.getChallengeManager().removeChallenge(this);
	}

}
