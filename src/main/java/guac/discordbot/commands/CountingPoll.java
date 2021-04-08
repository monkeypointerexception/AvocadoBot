package guac.discordbot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class CountingPoll implements CommandExecutor {
	
	public static String numEmoji[] = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};
	
	@Command(aliases = {"&cpoll"}, 
			description = "Create poll based on user input and count the results after a given time (max 9 items)", 
			usage = "&cpoll 20m,how are you?, good, bad || &cp 2h, is 3 too much?, yes, no, maybe, depends", async = true)
	
	public void countPollCommand(MessageCreateEvent event, User user,TextChannel channel, Server server) {
		if(user.isBot()) {return;}
		
		String msg = event.getMessageContent();
		msg = msg.replaceFirst("&cpoll", "");
		String[] poll = msg.split(",");
		
		//error checking
		if(poll.length > 11) {
			channel.sendMessage("max number of choices is 9");
			return;
		}
		if(poll.length < 4) {
			channel.sendMessage("not enough choices");
			return;
		}
		for(int i = 0; i < poll.length; i++) {
			poll[i] = poll[i].strip();
		}
		//here check how long the poll will be up for
		if(getTime(poll[0]) == -1) {
			channel.sendMessage("Please type in time correctly");
			return;
		}
		long time = getTime(poll[0]);
		String title = poll[1];
		Icon icon = user.getAvatar();
		//removes the title and time from poll array
		//simplifies process  
		poll = ArrayUtils.remove(poll, 0);
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
		
		//this HashMap will contain the emoji and the choice
		//will be used at the end to get the choice with the emoji key
		HashMap<String,String> emojiANDpoll = new HashMap<String,String>();
		for(int i = 0; i < length; i++) {
			embed.addField(numEmoji[i] , poll[i]);
			emojiANDpoll.put(numEmoji[i], poll[i]);
		}
		//creates a hashmap of emojis with the amount of times its been reacted to
		//will be used to keep track of the winner of the poll
		//adds emojis to message and hashmap
		HashMap<String,Integer> emoteCount = new HashMap<String,Integer>();
		
		//this is just me trying out lambda/futures/streams
		
		//get message as a future and add reactions
		CompletableFuture<Message> a = channel.sendMessage(embed);
		a.thenAccept(message -> { Arrays.stream(numEmoji).limit(length).forEach(emoji -> message.addReaction(emoji));
			
			//use stream to add values to emote hashMap
			Arrays.stream(numEmoji).limit(length).forEach(emoji -> emoteCount.put(emoji, 0));
			
			//Reaction Listener to count the amount of reactions
			message.addReactionAddListener(reactEvent -> {
				try {
					if(reactEvent.requestUser().get().isYourself()) {return;}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				//check if emoji is unicode
				String s; 
				if(!reactEvent.getEmoji().asUnicodeEmoji().isPresent()) {
					return;
				} else {
					s = reactEvent.getEmoji().asUnicodeEmoji().get();
				}
				//check if emoji is an actual choice then put it in
				if(emoteCount.containsKey(s)){
					emoteCount.put(s, emoteCount.get(s) + 1);
				}
			}).removeAfter(time, TimeUnit.SECONDS);
			//Reaction Remove Listener to remove an emoji count if reaction is removed
			message.addReactionRemoveListener(removeEvent -> {
				try {
					if(removeEvent.requestUser().get().isYourself()) {return;}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				//check if emoji is unicode
				String s; 
				if(!removeEvent.getEmoji().asUnicodeEmoji().isPresent()) {
					return;
				} else {
					s = removeEvent.getEmoji().asUnicodeEmoji().get();
				}
				//check if emoji is an actual choice
				if(emoteCount.containsKey(s)){
					emoteCount.put(s, emoteCount.get(s) - 1);
				}
			}).removeAfter(time, TimeUnit.MINUTES);
		});
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(new Poll(emoteCount, emojiANDpoll, channel, user.getMentionTag(), title), time, TimeUnit.SECONDS);
	}
	
	//gets total time in seconds
	public long getTime(String time) {
		long total = 0;
		String str = "";
		
		for(int i = 0; i < time.length(); i++) {
			if(Character.isLetter(time.charAt(i))) {
				//get time in seconds and reset str
				switch(time.charAt(i)) {
					case 'w':
						total += (Long.parseLong(str))*604800;
					case 'd':
						total += (Long.parseLong(str))*86400;
						str = "";
						break;
					case 'h':
						total += (Long.parseLong(str))*3600;
						str = "";
						break;
					case 'm':
						total += (Long.parseLong(str))*60;
						str = "";
						break;
					case 's':
						total += (Long.parseLong(str));
						str = "";
						break;
					default:
						return -1;
				}
			} else {
				//add to str to get correct number when parsed
				str += time.charAt(i);
			}
		}
		return total;
	}	
}
class Poll implements Runnable {
	HashMap<String,Integer> emojiCount;
	HashMap<String,String> emojipoll;
	TextChannel channel;
	String user;
	String title;
	//emojiCount contains number of votes an emoji has
	//emojipoll contains emoji and the choice
	public Poll(HashMap<String,Integer> emojiCount, HashMap<String,String> emojipoll, TextChannel channel, String user, String title) {
		this.emojiCount = emojiCount;
		this.emojipoll = emojipoll;
		this.channel = channel;
		this.user = user;
		this.title = title;
	}
	@Override
	public void run() {
		//get max value and creatse arraylist to hold the emoji(s)
		int max = Collections.max(emojiCount.values());
		List<String> keys = new ArrayList<>();
		//gets emoji or emojis that have the highest number of votes
		for (Entry<String, Integer> entry : emojiCount.entrySet()) {
			if (entry.getValue() == max) {
				keys.add(entry.getKey());
			}
		}
		java.time.LocalDateTime.now();
		String time = Calendar.getInstance().getTime().toString();
		String ans = "";
		//if there is only one winner
		//uses the emoji from previous hashMap as a key to get the poll choice value
		if(keys.size() == 1) {
			ans = emojipoll.get(keys.get(0));
			new MessageBuilder().append("**"+ans+"**" + " is the winner of " + user + "'s poll: **"
					+ title + "**").appendNewLine().append("(Counted on " + time +")").send(channel);
			//multiple winners
		} else {
			//iterate through list and get all the correct choices
			for (String emoji : keys) {
				if(emojipoll.containsKey(emoji)) {
				    ans += emojipoll.get(emoji) + ", ";
				}
			}
			//removes comma at the end
			ans = ans.substring(0, ans.length() - 2);
			new MessageBuilder().append("There is a tie between **" + ans + "** in " + user + "'s poll: **"
					+ title + "**").appendNewLine().append("(Counted on " + time +")").send(channel);
		}
	}
}