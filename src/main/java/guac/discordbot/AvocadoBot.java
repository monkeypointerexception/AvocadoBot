package guac.discordbot;

import java.net.MalformedURLException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;

import guac.discordbot.commands.Choose;
import guac.discordbot.commands.CountingPoll;
import guac.discordbot.commands.DateDuration;
import guac.discordbot.commands.Deactivate;
import guac.discordbot.commands.Help;
import guac.discordbot.commands.Invite;
import guac.discordbot.commands.Joke;
import guac.discordbot.commands.MemeChannel;
import guac.discordbot.commands.PP;
import guac.discordbot.commands.PressF;
import guac.discordbot.commands.RegularPoll;
import guac.discordbot.commands.SimpleCalculator;
import guac.discordbot.commands.Spam;
import guac.discordbot.commands.Trivia;
import guac.discordbot.commands.TriviaDBcheck;
import guac.discordbot.commands.TriviaLeaderboard;
import guac.discordbot.commands.TriviaScore;
import guac.discordbot.commands.WeatherForecast;
import guac.discordbot.commands.Memes;

public class AvocadoBot {
	
	public static DiscordApi bot;
	public static CommandHandler handler;
	
	public static void main(String[] args) throws MalformedURLException {
		
		bot = new DiscordApiBuilder().setToken(Keys.tok).login().join();
		bot.updateActivity(ActivityType.LISTENING, " to &help");
		//Register command executor
		//using Sdcf4j, discord command framework
		handler = new JavacordHandler(bot);
		handler.registerCommand(new Invite());
		handler.registerCommand(new DateDuration());
		handler.registerCommand(new PressF());
		handler.registerCommand(new Choose());
		handler.registerCommand(new Spam());
		handler.registerCommand(new PP());
		handler.registerCommand(new SimpleCalculator());
		handler.registerCommand(new WeatherForecast());
		handler.registerCommand(new CountingPoll());
		handler.registerCommand(new RegularPoll());
		handler.registerCommand(new Memes());
		handler.registerCommand(new Joke());
		handler.registerCommand(new Trivia());
		handler.registerCommand(new TriviaLeaderboard());
		handler.registerCommand(new TriviaScore());
		handler.registerCommand(new TriviaDBcheck());
		handler.registerCommand(new MemeChannel());
		handler.registerCommand(new Deactivate());
		handler.registerCommand(new Help());
		
		Spam.fillSpamArray();
		
	}
}