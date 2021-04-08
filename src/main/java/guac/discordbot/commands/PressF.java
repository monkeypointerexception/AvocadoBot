package guac.discordbot.commands;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PressF implements CommandExecutor {
	
	@Command(aliases = {"&pressf", "&f" }, description = "Pay respects to user provided input",
			usage = "&pressf Adobe Flash Player || &f Adobe Flash Player")
	 
	public void pressFCommand(String[] args, User user, TextChannel channel) {
		if(user.isBot()) {return;}
		if(args.length == 0) {
			channel.sendMessage("There is nothing to pay respects to :pensive:");
			return;
		}
		String pressf = "";
		for(String s : args) {
			pressf += s + " ";
		}
		pressf = "**" + pressf + "**";
		ArrayList<Long> reacted = new ArrayList<Long>();
		String str = pressf;
		CompletableFuture<Message> a = channel.sendMessage("Everyone!!! " + user.getMentionTag() + 
				" has asked us to pay respects to " + pressf);
			a.thenAccept(message -> { message.addReaction("🇫");
				message.addReactionAddListener(reactEvent -> {
					try {
						if(reactEvent.requestUser().get().isYourself() 
							|| !reactEvent.getEmoji().equalsEmoji("🇫") 
							|| reacted.contains(reactEvent.getUserId())) { 
								return;
						}
					} catch (InterruptedException | ExecutionException e1) {
						e1.printStackTrace();
					}
					reacted.add(reactEvent.getUserId());
					String person;
					try {
						person = reactEvent.requestUser().get().getMentionTag();
						channel.sendMessage(person + " has payed their respects to " + str);
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}).removeAfter(5, TimeUnit.MINUTES);
			});
		}
	}