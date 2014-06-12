package slybot.challenges;

import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.lib.Reference;

public class ChallengeTicTacToe extends MultiTurnChallenge {
	
	String[][] board;
	boolean hostTurn;
	String hostIcon;
	String challengeIcon;

	public ChallengeTicTacToe(Channel chan, User initializer, String challenged, String[] parameters, int timeOutInSeconds) {
		super(chan, initializer, challenged, "TicTacToe" , parameters, timeOutInSeconds, Reference.TTT_NEXT_TURN_TIMEOUT);
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
		getChannel().send().message(getHost().getNick() + " and " + getChallengedUser() + " decide to settle their disputes by an epic game of Tic-Tac-Toe!");
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
		
		if (updateBoard(x,y)) {
			printBoard();
			
			//Get the results from this move
			String s = getResults();
			if (!s.equalsIgnoreCase("null")) {
				end(s);
			} else {
			hostTurn = !hostTurn;
			
			getChannel().send().message((hostTurn ? getHost().getNick() : getChallengedUser()) + ": your turn!");
			return true;
			}
		}
		return false;
	}
	
	public void printBoard() {
		getChannel().send().message("  0 1 2");
		for (int i = 0; i < board.length; i++) {
			getChannel().send().message(i + " " + (board[i][0].equals("null") ? "_" : board[i][0]) + "|" + (board[i][1].equals("null") ? "_" : board[i][1]) + "|" + (board[i][2].equals("null") ? "_" : board[i][2]));
		}
	}
	
	public String getResults() {
		
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
		return "null";
	}

	@Override
	public User run(SlyBot bot, User usera, User userb, Channel channel,
			String[] params) {
		return null;
	}

	@Override
	public String getDescription() {
		return "Tic-Tac-Toe";
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
