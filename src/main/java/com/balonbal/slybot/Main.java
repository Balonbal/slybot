package com.balonbal.slybot;

import com.balonbal.slybot.config.BotConfig;
import com.balonbal.slybot.core.ChallengeManager;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.core.SlyConfiguration;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    static CommandListener commandListener;
    static CommandHandler commandHandler;
    static LinkListener linkListener;
    static SlyConfiguration sc;
	static ChallengeManager cm;
	static SlyBot slybot;
	static String nick;
	
	public static void main(String[] args) {

        sc = new BotConfig();
        sc.initialize();
        initLoggers();
        commandHandler = new CommandHandler();
        commandListener = new CommandListener(commandHandler);
        linkListener = new LinkListener();
        cm = new ChallengeManager();
		
		//Set up local variables for network and channel
		String network = null;
		String[] channels = null;
		String nickPass = null;

		//Check if default values are set
		if (!Settings.botnick.equals("")) {
			nick = Settings.botnick;
		}
		if (!Settings.network.equals("")) {
			network = Settings.network;
		}
		if (!Settings.channels[0].equals("")) {
			channels = Settings.channels;
		}
		if (!Settings.nickpass.equals("")) {
			nickPass = Settings.nickpass;
		}
		
		if (nick == null || network == null || channels == null) {
			//Check if VM-arguments are enough to connect
			switch (args.length) {
			case 4:
				nickPass = args[3];
			case 3:
				nick = args[2];
			case 2:
				channels = new String[] { args[1] };
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
				channels = s.nextLine().split(", ");
			case 2:
				System.out.print("Enter a nick for the bot: ");
				nick = s.nextLine();
			case 3:
				System.out.print("Enter the password for nickserv (leave blank for none): ");
				nickPass = s.nextLine();
			}
				
			if (Settings.botnick.equals("")) {
				System.out.print("Do you want to use \"" + nick + "\" as the default nick? (yes/no): ");
				if (s.nextLine().equalsIgnoreCase("yes")) {
					sc.changeSetting("botnick", nick);
				}
			}
			if (Settings.nickpass.equals("") && !Settings.botnick.equals("")) {
				System.out.print("Do you want to add \"" + nickPass + "\" as the default nickserv password? (yes/no): ");
				if (s.nextLine().equalsIgnoreCase("yes")) {
					sc.changeSetting("nickpass", nickPass);
				}
			}
			if (Settings.network.equals("")) {
				System.out.print("Do you want to add \"" + network + "\" as the default network? (yes/no): ");
				if (s.nextLine().equalsIgnoreCase("yes")) {
					sc.changeSetting("default_network", network);
				}
			}
			if (Settings.channels[0].equals("")) {
				System.out.print("Do you want to add \"" + channels[0] + "\" to the default channels? (yes/no): ");
				if (s.nextLine().equalsIgnoreCase("yes")) {
					sc.appendSetting("default_channels", ",", channels[0]);
				}
			}
		
		
			
			s.close(); //close the input
		}
		
		Builder<SlyBot> config = new Builder<SlyBot>()
			.setName(nick) //Set the nick of the bot.
			.setAutoNickChange(true) //Automatically change nick when the current one is in use
			.setCapEnabled(true) //Enable CAP features
                .addListener(commandListener) //This class is a commandListener, so add it to the bots known listeners
                .addListener(linkListener)
                .addListener(new ChallengeListener())
                .addListener(new LoggerListener())
                .addListener(new AliasListener())
                .setServerHostname(network);
		
		if (nickPass != null && !nickPass.equals("")) {
			config.setNickservPassword(nickPass);
		}
        
		for (String chan: channels) {
			config.addAutoJoinChannel(chan);
		}
		

		Configuration<SlyBot> configuration = config.buildConfiguration();
		//create the bot with the defined config
		slybot = new SlyBot(configuration);
		
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

    private static void initLoggers() {
        //TODO add loggers that dosen't spend hours
    }

    public static CommandListener getCommandListener() {
        return commandListener;
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
