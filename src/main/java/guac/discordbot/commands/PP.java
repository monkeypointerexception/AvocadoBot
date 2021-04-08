package guac.discordbot.commands;

import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PP implements CommandExecutor{
	
	@Command(aliases = {"&pp" }, description = "... get pp length",usage = "&pp")
	
	public String ppCommand(User user) {
		int a = user.getDiscriminatedName().length()/2;
		int x = (Character.getNumericValue(user.getIdAsString().charAt(a)));
		if((a*2) % 2 == 0) {
			x += Character.getNumericValue(user.getIdAsString().charAt((a*2)/3));
		}
		if(x == 0) {return "no pp available :pensive:";}
		String pp = "8";
		pp += "=".repeat(x);
		if(x == 1) {
			return user.getMentionTag() + " has a pp length of " + x + " an inch: " + (pp += "D");
		}
		return user.getMentionTag() + " has a pp length of " + x + " inches: " + (pp += "D");
	}

}
