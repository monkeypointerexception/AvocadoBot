package guac.discordbot.commands;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class Spam implements CommandExecutor {
	
	public static ArrayList<Long> spammed = new ArrayList<Long>();
	
	public static URL spamMemeArray[] = new URL[7];
	
	@Command(aliases = {"&spam"}, description = "Spam in whatever channel this is called in", usage = "&spam", async = true)
	
	public void spamCommand(TextChannel channel, User user, Server server) throws InterruptedException, IOException {
		if(user.isBot()) {return;}
		//discourage people from using it again
		if(spammed.contains(user.getId())){
			channel.sendMessage("haven't you already learned your lesson?"); 
			return;
		} else {
			spammed.add(user.getId());
		}
		//:)
		if(spammed.size() == 10) {
			spammed.clear();
		}
		channel.sendMessage("**Currently spamming " + user.getMentionTag() + 
			"'s DMs**. This could have been a lot worse, "
			+ "but Discord only lets me send 5 messages every 5 seconds :angry:");
		
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				new MessageBuilder().addAttachment(spamMemeArray[j])
				.append(":weary: ".repeat(20) + user.getMentionTag()).send(user);
			}
		}
		user.sendMessage("I am terribly sorry for any inconvience I may have caused");
		user.sendMessage("I hope you have a great day :smiley:");
		
		int random = ThreadLocalRandom.current().nextInt(1, 12);
		String name = Files.readAllLines(Paths.get("src/main/java/guac/discordbot/commands/nickname.txt")).get(random);
		user.updateNickname(server, name, "atempting to spam");
	}
	public static void fillSpamArray() throws MalformedURLException {
		spamMemeArray[0] = new URL("https://cdn.discordapp.com/attachments/727990364828598337/741697024709361777/image0.jpg");
		spamMemeArray[1] = new URL("https://cdn.discordapp.com/attachments/727990364828598337/741697025019478144/image1.jpg");
		spamMemeArray[2] = new URL("https://cdn.discordapp.com/attachments/727990364828598337/741697025367605458/image2.jpg");
		spamMemeArray[3] = new URL("https://cdn.discordapp.com/attachments/727990364828598337/741697025652949123/image3.jpg");
		spamMemeArray[4] = new URL("https://cdn.discordapp.com/attachments/727990364828598337/741697025917190153/image4.jpg");
		spamMemeArray[5] = new URL("https://cdn.discordapp.com/attachments/727990364828598337/741697026416312350/image5.jpg");
		spamMemeArray[6] = new URL("https://cdn.discordapp.com/attachments/727990364828598337/741697026705850419/image6.jpg");
	}	
}
