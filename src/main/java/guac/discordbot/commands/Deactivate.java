package guac.discordbot.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class Deactivate implements CommandExecutor {
	
	@Command(aliases = {"&deactivate"}, description = "Deactivate Meme Channel", usage = "&deactivate")
	
	public void deactivateChannel(User user, TextChannel channel, MessageAuthor e) {
		if(!user.isBotOwner() || user.isBot() || !e.canKickUsersFromServer()) {return;}
		if(!MemeChannel.schedule.containsKey(channel.getIdAsString())) {
			channel.sendMessage("MemeChannel command isn't even active here you dingus :angry:");
		} else {
			MemeChannel.schedule.get(channel.getIdAsString()).shutdown();
			MemeChannel.schedule.remove(channel.getIdAsString());
			channel.sendMessage("Understandable. Meme Channel has been deactivated. Have a great day!!! :slight_smile:");
		}
	}
}
