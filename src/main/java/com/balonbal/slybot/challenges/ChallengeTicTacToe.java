package com.balonbal.slybot.challenges;

import java.util.Random;

import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.User;

import com.balonbal.slybot.Main;

public class ChallengeTicTacToe extends Challenge {
	
	String[][] board;
	boolean hostTurn;
    boolean started = false;
	String hostIcon;
	String challengeIcon;

	public ChallengeTicTacToe(Channel chan, User initializer, String challenged, String[] parameters, int timeOutInSeconds) {
		super(chan, initializer, challenged, parameters, timeOutInSeconds);
		board = new String[][] { 
				{ "_", "_", "_" },
				{ "_", "_", "_" },
				{ "_", "_", "_" } 
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
        if (!started) {
            getChannel().send().message(getHost().getNick() + " and " + getChallengedUser() + " decide to settle their disputes by an epic game of Tic-Tac-Toe!");
            getChannel().send().message(getHost().getNick() + " will be playing as " + hostIcon);
            getChannel().send().message(getChallengedUser() + " will be playing as " + challengeIcon);
            printBoard();
            getChannel().send().message((hostTurn ? getHost().getNick() : getChallengedUser()) + " will start of this legendary battle!");
            addTime(Reference.TTT_NEXT_TURN_TIMEOUT);
            started = true;
        }
	}
	
	private boolean updateBoard(int x, int y) {
		//Check if the position is occupied
		if (board[y][x].equals("_")) {
			//put the icon in the board
			board[y][x] = (hostTurn?hostIcon:challengeIcon);
			return true;
		}
		return false;
	}
	
	public void proccessTurn(User u, String params[]) {
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
			if (!s.equalsIgnoreCase("_")) {
				end(s);
			} else {
                hostTurn = !hostTurn;

                getChannel().send().message((hostTurn ? getHost().getNick() : getChallengedUser()) + ": your turn!");

                addTime(Reference.TTT_NEXT_TURN_TIMEOUT);
			}
		}
	}
	
	public void printBoard() {
		getChannel().send().message("  0 1 2");
		for (int i = 0; i < board.length; i++) {
			getChannel().send().message(i + " " + board[i][0] + "|" + board[i][1] + "|" + board[i][2]);
		}
	}
	
	public String getResults() {
		
		for (int i = 0; i < board.length; i++) {
			//Check the board vertically
			if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals("_")) {
				return board[i][0];
			//check the board horizontally
			} else if (board[0][i].equals(board[1][i]) &&  board[1][i].equals(board[2][i]) && !board[0][i].equals("_")) {
				return board[0][i];
			}
		}
		
		//check the board diagonally
		if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("_")) {
			return board[0][0];
		} else if (board[2][0].equals(board[1][1]) && board[2][0].equals(board[0][2]) && !board[2][0].equals("_")) {
			return board[2][0];
		}
		
		//Check if the board is full
		boolean full = true;
		for (String[] s: board) {
			for (String str: s) {
				if (str.equals("_")) {
					full = false;
				}
			}
		}
		
		if (full) {
			return "full";
		}
		return "_";
	}

	@Override
	public void run() {
	}

	@Override
	public String getDescription() {
		return "Tic-Tac-Toe";
	}
	
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
