package guac.discordbot.commands;

import java.net.URISyntaxException;
import java.sql.SQLException;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class TriviaScore implements CommandExecutor {
	
	@Command(aliases = {"&score"}, description = "gets user's or mentioned users score", usage = "&score")
	
	public void scoreCommand(User user, TextChannel channel, MessageCreateEvent event, Server server) {
		if(user.isBot()) {return;}
		String a = "";
		String id = "";
		String mention = "";
		if(!event.getMessage().getMentionedUsers().isEmpty()) {
			id = event.getMessage().getMentionedUsers().get(0).getIdAsString();
			mention = event.getMessage().getMentionedUsers().get(0).getMentionTag();
		} else {
			id = event.getMessageAuthor().getIdAsString();
			mention = event.getMessageAuthor().asUser().get().getMentionTag();
		}
		
		try {
			a = Trivia.getUserScore(id,server.getIdAsString());
		} catch (URISyntaxException | SQLException e) {
			channel.sendMessage("unable to get "+mention+"'s score");
		}

		if(a.isEmpty()) {
			new MessageBuilder().append(mention+" has not answered any questions correctly yet").send(channel);
		} else {
			new MessageBuilder().append(mention+" has answered "+ a + " question(s) correctly").send(channel);
		}
		
	}
}