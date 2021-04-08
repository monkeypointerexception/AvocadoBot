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
import org.json.JSONArray;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class Memes implements CommandExecutor {
	
	@Command(aliases = {"&meme"}, description = "Gets meme from a subreddit, if a sub is not provided, then r/memes will be used", 
			usage = "&meme || &meme r/dankmemes || &meme programmerhumor")
	
	public void memeCommand(String[] args, User user, TextChannel channel) {
		if(user.isBot()) {return;}
		String url = "";
		String sub = "r/memes";
		//get subreddit
		if(args.length == 0) {
			url = "https://ssl.reddit.com/r/memes/random/.json";
		} else {
			sub = args[0].toLowerCase();
			if(sub.contains("r/")) {
				url = "https://ssl.reddit.com/" + sub + "/random/.json";
			} else {
				url = "https://ssl.reddit.com/r/" + sub + "/random/.json";
				sub = "r/" + sub;
			}
		}
		try {
			connectToRedditAndMeme(url,sub,user,channel);
		} catch (IOException e) {
			channel.sendMessage("meme unavailable :( or you entered an invalid subreddit :thinking:");
		}
	}
	public void connectToRedditAndMeme(String url, String sub, User user,TextChannel ch) throws IOException {
		URL theUrl = new URL(url);
		URLConnection yc = theUrl.openConnection();
		yc.addRequestProperty("User-agent", "AvocadoBot");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		}	
		in.close();
		
		try {
			JSONArray memeJSON = new JSONArray(response.toString());
			String memeLink = "";
			memeLink = memeJSON.getJSONObject(0).getJSONObject("data")
				.getJSONArray("children").getJSONObject(0).getJSONObject("data")
				.get("url_overridden_by_dest").toString();
			//build embed
			EmbedBuilder embed = new EmbedBuilder()
				.setColor(Color.ORANGE)
				.setTitle("Courtesy of " + sub)
				.setImage(memeLink)
				.setFooter("Requested by "+ user.getDiscriminatedName(), user.getAvatar())
				.setTimestampToNow();
			ch.sendMessage(embed);
		} catch (org.json.JSONException e) {
			ch.sendMessage("meme unavailable :( or you entered an invalid subreddit :thinking:");
		}
		return;
	}
}