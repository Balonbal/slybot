package slybot;

import java.io.IOException;
import java.util.Scanner;

import org.pircbotx.Configuration;
import org.pircbotx.exception.IrcException;

import slybot.core.ChallengeManager;
import slybot.core.SlyConfiguration;
import slybot.lib.Settings;

public class Main {
	
	static SlyConfiguration sc;
	static ChallengeManager cm;
	static SlyBot slybot;
	static String nick;
	
	public static void main(String[] args) {
		
		sc = new SlyConfiguration();
		sc.initialize();
		cm = new ChallengeManager();
		
		//Set up local variables for network and channel
		String network = null;
		String channel = null;
		String nickPass = null;
		
		//Check if VM-arguments are enough to connect
		switch (args.length) {
		case 4:
			nickPass = args[3];
		case 3:
			nick = args[2];
		case 2:
			channel = args[1];
		case 1:
			network = args[0];
			
		}
		//if not, ask for user input on the missing variables
			Scanner s = new Scanner(System.in);
			switch (args.length) {
			case 0:
				System.out.print("Please select a network: ");
				network = s.nextLine();
			case 1:
				System.out.print("Please select a channel to join: ");
				channel = s.nextLine();
			case 2:
				System.out.print("Enter a nick for the bot: ");
				nick = s.nextLine();
			case 3:
				System.out.print("Enter the password for nickserv (leave blank for none): ");
				nickPass = s.nextLine();
			}
			
			
			s.close(); //and close the input
		
		Configuration config = new Configuration.Builder()
		.setName(nick) //Set the nick of the bot.
        .setAutoNickChange(true) //Automatically change nick when the current one is in use
        .setCapEnabled(true) //Enable CAP features
        .addListener(new SlyListener()) //This class is a listener, so add it to the bots known listeners
        .setServerHostname(network)
        .addAutoJoinChannel(channel)
        .setNickservPassword(nickPass)
        .buildConfiguration();

		//create the bot with the defined config
		slybot = new SlyBot(config);
		
		System.out.println("This bot is owned by " + Settings.owner);
		
		try {
			//Start the bot
			slybot.startBot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String getNick() {
		return nick;
	}
	
	public static SlyBot getBot() {
		return slybot;
	}
	
	public static SlyConfiguration getConfig() {
		return sc;
	}
	
	public static ChallengeManager getChallengeManager() {
		return cm;
	}

}
