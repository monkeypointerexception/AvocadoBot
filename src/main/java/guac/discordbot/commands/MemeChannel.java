package guac.discordbot.commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.json.JSONArray;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class MemeChannel implements CommandExecutor {
	
	public static HashMap<String,ScheduledExecutorService> schedule = new HashMap<String,ScheduledExecutorService>();
	
	@Command(aliases = {"&memech"}, description = "Have the bot send memes into a channel at a specified interval in hours", 
			usage = "&memech 3 memes || &memech  1 r/programmerhumor")
	
	public void activateMemeChannel(String[] args, User user, MessageAuthor e, TextChannel ch) {
		if(!user.isBotOwner() || user.isBot() || !e.canKickUsersFromServer()) {return;}
		if(schedule.containsKey(ch.getIdAsString())) {
			ch.sendMessage("There is a meme channel already active you dingus");
			return;
		}
		if(args.length < 2) {
			ch.sendMessage("Please include a number and a subreddit");
			return;
		}
		if(!args[0].matches("[0-9]+")) {
			ch.sendMessage("First argument should be a number");
			return;
		}
		long time = Long.parseLong(args[0]);
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new ActivateChannel(ch, args[1]), 0, time, TimeUnit.HOURS);
        schedule.put(ch.getIdAsString(), executorService);
	}
}
class ActivateChannel implements Runnable {
	TextChannel channel;
	String sub;
	
	public ActivateChannel(TextChannel channel, String sub) {
		this.channel = channel;
		this.sub = sub;
	}

	@Override
	public void run() {
		sub = sub.toLowerCase();
		String url = "";
		if(sub.contains("r/")) {
			url = "https://ssl.reddit.com/" + sub + "/random/.json";
		} else {
			url = "https://ssl.reddit.com/r/" + sub + "/random/.json";
			sub = "r/" + sub;
		}
		StringBuffer response = new StringBuffer();
		try {
			URL theUrl = new URL(url);
			URLConnection yc = theUrl.openConnection();
			yc.addRequestProperty("User-agent", "AvocadoBot");
		
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}	
			in.close();
		} catch (IOException e) {
			channel.sendMessage("please contact guac if this error arises");
		}
		
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
				.setTimestampToNow();
			channel.sendMessage(embed);
		} catch (org.json.JSONException e) {
			channel.sendMessage("meme unavailable :( or you entered an invalid subreddit :thinking:, please try the command again");
			MemeChannel.schedule.get(channel.getIdAsString()).shutdown();
			MemeChannel.schedule.remove(channel.getIdAsString());
		}		
	}
}
