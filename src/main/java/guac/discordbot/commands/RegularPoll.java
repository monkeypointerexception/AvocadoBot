package guac.discordbot.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.ArrayUtils;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class RegularPoll implements CommandExecutor {
	
	public static String numEmoji[] = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};
	
	@Command(aliases = {"&poll"}, description = "Create a poll based on user input", 
			usage = "&poll how are you?, good, bad || &poll is 3 too much?, yes, no, maybe, depends")
	
	public void regularPollCommand(User user, MessageCreateEvent event, TextChannel channel, Server server) {
		if(user.isBot()) {return;}
		
		String msg = event.getMessageContent();
		msg = msg.replaceFirst("&poll", "");
		String[] poll = msg.split(",");
		
		//error checking
		if(poll.length > 10) {
			channel.sendMessage("max number of choices is 9");
			return;
		}
		if(poll.length < 3) {
			channel.sendMessage("not enough choices");
			return;
		}
		for(int i = 0; i < poll.length; i++) {
			poll[i] = poll[i].strip();
		}
		String title = poll[0];
		Icon icon = user.getAvatar();
		//removes the title from poll array
		//simplifies process  
		poll = ArrayUtils.remove(poll, 0);
		int length = poll.length;
		//creates and formats embed message
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle(title)
				.setDescription("Please react with your choice :smiley:")
				.setAuthor(user.getDisplayName(server) + "'s poll", "http://google.com/", icon)
				.setColor(Color.GREEN)
				.setFooter("Requested by " + event.getMessageAuthor().getDiscriminatedName())
				.setTimestampToNow(); 
		
		for(int i = 0; i < length; i++) {
			embed.addField(numEmoji[i] , poll[i]);
		}
		//this is just me trying out lambda/futures/streams
		//get message as a future and add reactions
		CompletableFuture<Message> a = channel.sendMessage(embed);
		a.thenAccept(message -> { Arrays.stream(numEmoji).limit(length).forEach(emoji -> message.addReaction(emoji));
		});
	}
}