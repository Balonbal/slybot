package com.balonbal.slybot;

import com.balonbal.slybot.config.BotConfig;
import com.balonbal.slybot.core.ChallengeManager;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.core.ConfigurationHandler;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.listeners.*;
import com.balonbal.slybot.util.sites.twitch.Twitch;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.exception.IrcException;

import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static CommandListener commandListener;
    static CommandHandler commandHandler;
    static LinkListener linkListener;
    static ConfigurationHandler configurationHandler;
	static ChallengeManager cm;
	static SlyBot slybot;
	static String nick = "slybot";

	public static void main(String[] args) {

        Twitch twitch = new Twitch();

        System.out.println("Starting slybot, this is version " + Main.class.getPackage().getImplementationVersion());

        BotConfig botConfig = new BotConfig();
        configurationHandler = new ConfigurationHandler();
        configurationHandler.addConfiguration(new File("configuration.yaml"), Reference.CONFIG_BOTCONFIG_ID, botConfig);
        commandHandler = new CommandHandler();
        commandListener = new CommandListener(commandHandler);
        linkListener = new LinkListener();
        cm = new ChallengeManager();
		
		//Set up local variables for network and channel
		String network = "";
        int port = 6667;
		ArrayList<String> channels = new ArrayList<String>();
		String nickPass = "";
        String serverpass = "";
        boolean ssl = false;

		//Check if default values are set
		if (!Settings.botnick.equals("")) {
			nick = Settings.botnick;
		}
		if (!Settings.network.equals("")) {
			network = Settings.network;
		}
		if (!Settings.channels.isEmpty()) {
			channels = Settings.channels;
		}
		if (!Settings.nickpass.equals("")) {
			nickPass = Settings.nickpass;
		}
        if (!Settings.serverPass.equals("")) {
            serverpass = Settings.serverPass;
        }

        if (nick.equals("") || network.equals("")) {
            System.out.println("No suitable configuration found, creating new...");
            //if not, ask for user input on the missing variables
            Scanner s = new Scanner(System.in);

            System.out.print("Please select a network []: ");
            network = s.nextLine();
            System.out.print("Select a port [6667]: ");
            String p = s.nextLine();
            if (p.matches("\\d+")) {
                port = Integer.parseInt(p);
            }
            System.out.print("Server Password []:");
            serverpass = s.nextLine();
            System.out.print("Use SSL [No]: ");
            ssl = s.nextLine().toLowerCase().matches("y(|e|es)");
            System.out.print("Please select a channel to join []: ");
            channels.add(s.nextLine());
            System.out.print("Enter a nick for the bot [slybot]: ");
            nick = s.nextLine();
            System.out.print("Enter the password for nickserv []: ");
            nickPass = s.nextLine();

            s.close(); //close the input

            if (Settings.network.equals("")) Settings.network = network;
            Settings.port = port;
            if (Settings.serverPass.equals("")) Settings.serverPass = serverpass;
            Settings.ssl = ssl;
            if (Settings.channels.size() == 0) Settings.channels = channels;
            if (Settings.botnick.equals("")) Settings.botnick = (nick.equals("") ? "slybot" : nick);
            if (Settings.nickpass.equals("")) Settings.nickpass = nickPass;

            configurationHandler.saveAll();
        }

		Builder<SlyBot> config = new Builder<SlyBot>()
			.setName(nick) //Set the nick of the bot.
            .setRealName("SlyBot v" + Main.class.getPackage().getImplementationVersion())
			.setAutoNickChange(true) //Automatically change nick when the current one is in use
			.setCapEnabled(true) //Enable CAP features
                .addListener(commandListener)
                .addListener(linkListener)
                .addListener(new ChallengeListener())
                .addListener(new LoggerListener())
                .addListener(new AliasListener())
                .addListener(new ChannelListener())
            .setServerHostname(network)
            .setServerPort(Settings.port);

        if (Settings.ssl) {
            config.setSocketFactory(SSLSocketFactory.getDefault());
        }
		if (nickPass != null && !nickPass.equals("")) {
			config.setNickservPassword(nickPass);
		}
        
		for (String chan: channels) {
			config.addAutoJoinChannel(chan);
		}

        if (!Settings.serverPass.equals("")) {
            config.setLogin(nick);
            config.setServerPassword(Settings.serverPass);
        }

		Configuration<SlyBot> configuration = config.buildConfiguration();
		//create the bot with the defined config
		slybot = new SlyBot(configuration, (BotConfig) configurationHandler.getConfig(Reference.CONFIG_BOTCONFIG_ID));
		System.out.println("This bot is owned by " + Settings.owner);
		try {
			//Start the bot
			slybot.startBot();
		} catch (IOException | IrcException e) {
			e.printStackTrace();
		}

    }

    public static CommandListener getCommandListener() {
        return commandListener;
    }
	
	public static SlyBot getBot() {
		return slybot;
	}
	
	public static ConfigurationHandler getConfig() {
		return configurationHandler;
	}
	
	public static ChallengeManager getChallengeManager() {
		return cm;
	}

}
