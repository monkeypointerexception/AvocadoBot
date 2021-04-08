package guac.discordbot.commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.json.JSONObject;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class Joke implements CommandExecutor {
	
	@Command(aliases = {"&joke"}, description = "Gets a joke (Warning some of these are really bad)",usage = "&joke")
	
	public void jokeCommand(User user, TextChannel channel) throws IOException {
		if(user.isBot()) {return;}
		
		URL theUrl = new URL("https://icanhazdadjoke.com/");
		URLConnection yc = theUrl.openConnection();
		yc.addRequestProperty("User-agent", "DiscordBot");
		yc.addRequestProperty("Accept", "application/json");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		}	
		in.close();
		
		JSONObject memeJSON = new JSONObject(response.toString());
		String joke = memeJSON.get("joke").toString();
		
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle(joke)
				.setColor(Color.YELLOW)
				.addField("Provided by C653 Labs", "https://icanhazdadjoke.com/")
				.setFooter("Requested by " + user.getDiscriminatedName(), user.getAvatar());
		channel.sendMessage(embed);
	}
}