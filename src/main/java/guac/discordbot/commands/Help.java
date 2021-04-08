package guac.discordbot.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
public class Help implements CommandExecutor {
	
	 @Command(aliases = {"&help", "&commands"}, description = "lists commands")
	 
	 public void onHelpCommand(User user, TextChannel channel) {
		 StringBuilder builder = new StringBuilder();
		 builder.append("```\n");
		 builder.append("LIST OF COMMANDS \n");
	     builder.append("Choose command: Choose user provided inputs seperated by commas \n");
	     builder.append("Examples: &choose sleep, walk, run around the field **OR** &ch sleep a lot, walk, run \n");
	     builder.append("\n");
	     builder.append("Counting poll command: Create poll based on user input and count the results after a given time (max 9 items) \n");
	     builder.append("Examples: &cpoll 20m,how are you?, good, bad **OR** &cp 2h, is 3 too much?, yes, no, maybe, depends \n");
	     builder.append("\n");
	     builder.append("Date Duration command: Calculates duration between today and user provided date \n");
	     builder.append("Usage: &duration YYYY-MM-DD **OR** &dur YYYY-MM-DD \n");
	     builder.append("\n");
	     builder.append("Joke command: Gets a joke (Warning some of these are really bad) \n");
	     builder.append("Usage: &joke \n");
	     builder.append("\n");
	     builder.append("Meme Channel command: Have the bot send memes into a channel at a specified interval in hours \n");
	     builder.append("Examples: &memech 3 memes **OR** &memech  1 r/programmerhumor \n");
	     builder.append("\n");
	     builder.append("Deactivate Meme Channel command: Deactivate Meme Channel \n");
	     builder.append("Usage: &deactivate \n");
	     builder.append("\n");
	     builder.append("Meme Command: Gets meme from a subreddit, if a sub is not provided, then r/memes will be used \n");
	     builder.append("Usage: &meme **OR** &meme r/dankmemes **OR** &meme programmerhumor \n");
	     builder.append("\n");
	     builder.append("PP Command: ... gets pp length \n");
	     builder.append("Usage: &pp \n");
	     builder.append("\n");
	     builder.append("Press F Command: Ask everyone to pay respects to user provided input \n");
	     builder.append("Examples: &pressf Adobe Flash Player **OR** &f Adobe Flash Player \n");
	     builder.append("\n");
	     builder.append("Regular Poll: Create a poll based on user input \n");
	     builder.append("Examples: &poll how are you?, good, bad \n");
	     builder.append("\n");
	     builder.append("Simple Calculator: Simple calculator \n");
	     builder.append("Examples: &calc (3*(4+5))/2 \n");
	     builder.append("\n");
	     builder.append("Spam Command: Spams in whatever channel you are currently in \n");
	     builder.append("Usage: &spam \n");
	     builder.append("\n");
	     builder.append("Trivia Command: sends an embed with a question for the user to answer \n");
	     builder.append("Usage: &trivia \n");
	     builder.append("\n");
	     builder.append("Trivia Leaderboard Command: gets top 5 in trivia leaderboard for the server \n");
	     builder.append("Usage: &lb **OR** &leaderboard \n");
	     builder.append("\n");
	     builder.append("Trivia Score Command: gets user's or mentioned users score \n");
	     builder.append("Usage: &score **OR** &score @guac \n");
	     builder.append("\n");
	     builder.append("Weather Command: Get the weather forecast for the user provided location \n");
	     builder.append("Usage: &weather New York City **OR** &wt Boston, Massachusetts \n");
	     builder.append("\n```");
	     user.sendMessage(builder.toString());
	     channel.sendMessage(user.getMentionTag() + " You have been DM'ed with list of commands");
	 }

}
