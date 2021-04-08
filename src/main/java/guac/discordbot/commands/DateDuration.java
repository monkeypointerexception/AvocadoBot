package guac.discordbot.commands;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class DateDuration implements CommandExecutor {
	
	@Command(aliases = {"&duration", "&dur" }, description = "Calculates duration between today and user provided date", 
			usage = "&duration YYYY-MM-DD || &dur YYYY-MM-DD")
	
    public String durationCommand(String[] args, User user) {
		if(user.isBot()) {return null;}
		
		if(args.length == 0) {
			return "No date provided :pensive:";
		}
		//if user provided date is not a date
		if(args[0].matches("[a-zA-Z]+")) {
			return "Please format date like this: YYYY-MM-DD 🙂";
		}
		
		try {
			LocalDate compared = LocalDate.parse(args[0].strip());
			if(compared.compareTo(LocalDate.now()) > 0) {
				return Duration.between(LocalDate.now().atTime(LocalTime.now()),
						compared.atStartOfDay()).toDays()
						+ " days until " + args[0].strip();
			} else {
				return "It has been " + Duration.between(compared.atStartOfDay(),
						LocalDate.now().atTime(LocalTime.now())).toDays()
						+ " days since " + args[0].strip();
			}
		} catch (DateTimeParseException e) {
			return "Please format date like this: YYYY-MM-DD 🙂";
		}
    }
}
