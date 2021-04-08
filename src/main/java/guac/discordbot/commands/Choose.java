package guac.discordbot.commands;

import java.util.concurrent.ThreadLocalRandom;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class Choose implements CommandExecutor {
	
	@Command(aliases = {"&choose", "&ch" }, description = "Choose between user provided inputs", 
			usage = "&choose sleep, walk, run around the field || &ch sleep a lot, walk, run")
	
	public void chooseCommand(String[] args, TextChannel channel, User user) {
		if(user.isBot()) {return;}
		String[] chosen = String.join(" ", args).split(",");
		if(chosen.length < 2) {
			channel.sendMessage("There aren't enough arguments, or you didn't seperate them with commas");
			return;
		}
		//String[] chosen = String.join(" ", args).split(",");
		for(int i = 0; i < chosen.length; i++) {
			chosen[i] = chosen[i].strip();
		}
  
		int rand = ThreadLocalRandom.current().nextInt(0, chosen.length);
		int randSwitch = ThreadLocalRandom.current().nextInt(1, 7);
		
    	switch (randSwitch) {
    		case 1:
    	  		channel.sendMessage("Personally, I believe " 
    	  			+ "``" + chosen[rand] + "``" + " is the best choice");
    	  		break;
    	  	case 2:
    	  		channel.sendMessage("``" + chosen[rand]+ "``" + " is a pretty good choice");
    	  		break;
    	  	case 3:
    	  		channel.sendMessage("I think you should try " + "``" + chosen[rand] + "``");
    	  		break;
    	  	case 4:
    	  		channel.sendMessage("How about " + " ``" + chosen[rand] + "``");
    	  		break;
    	  	case 5:
    	  		channel.sendMessage("Imagine needing a bot to make decisions for you lmao,"
    	  				+ " that being said, I choose ``" + chosen[rand] + "``");
    	  		break;
    	  	case 6:
    	  		channel.sendMessage("I choose " + "``" + chosen[rand] + "``");
    	  		break;
		}
		return;
	}

}
