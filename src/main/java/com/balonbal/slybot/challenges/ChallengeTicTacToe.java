package com.balonbal.slybot.challenges;

import java.util.Random;

import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import com.balonbal.slybot.Main;

public class ChallengeTicTacToe extends Challenge {
	
	String[][] board;
	boolean hostTurn;
    boolean started = false;
	String hostIcon;
	String challengeIcon;

	public ChallengeTicTacToe(Channel chan, User initializer, User challenged, String[] parameters, int timeOutInSeconds) {
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
            Main.getBot().reply(getChannel(), null, getHost().getNick() + " and " + getChallengedUser().getNick() + " decide to settle their disputes by an epic game of Tic-Tac-Toe!");
            Main.getBot().reply(getChannel(), null, getHost().getNick() + " will be playing as " + hostIcon);
            Main.getBot().reply(getChannel(), null, getChallengedUser().getNick() + " will be playing as " + challengeIcon);
            printBoard();
            Main.getBot().reply(getChannel(), null, (hostTurn ? getHost() : getChallengedUser()).getNick() + " will start of this legendary battle!");
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
			if (u.getNick().equalsIgnoreCase(getChallengedUser().getNick())) {
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

                Main.getBot().reply(getChannel(), null,((hostTurn ? getHost() : getChallengedUser()).getNick()) + ": your turn!");

                addTime(Reference.TTT_NEXT_TURN_TIMEOUT);
			}
		}
	}
	
	public void printBoard() {
		getChannel().send().message("  0 1 2");
		for (int i = 0; i < board.length; i++) {
            Main.getBot().reply(channel, null, i + " " + colorize(board[i][0]) + "|" + colorize(board[i][1]) + "|" + colorize(board[i][2]));
		}
	}

    private String colorize(String characther) {
        return (characther.equals("X") ? Colors.RED : characther.equals("O") ? Colors.DARK_BLUE : Colors.PURPLE ) + characther + Colors.NORMAL;
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
        Main.getBot().reply(getChannel(), null, "The game is over!");
		if (winner.equalsIgnoreCase(hostIcon)) {
            Main.getBot().reply(channel, null, getHost().getNick() + " has defeated the noob " + getChallengedUser().getNick());
		} else if (winner.equalsIgnoreCase(challengeIcon)) {
            Main.getBot().reply(channel, null, getHost().getNick() + " fought until the bitter end, but could not keep up with " + getChallengedUser().getNick());
		} else {
            Main.getBot().reply(channel, null, "The game is a tie! Both players are officialy noobs.");
		}

        Main.getChallengeManager().removeChallenge(this);
	}
}
