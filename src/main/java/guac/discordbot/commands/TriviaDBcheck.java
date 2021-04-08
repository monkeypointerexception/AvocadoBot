package guac.discordbot.commands;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import guac.discordbot.AvocadoBot;

public class TriviaDBcheck implements CommandExecutor {
	
	@Command(aliases = {"&db"}, description = "only for bot owner", usage = "&db")
	
	public void checkDB(User user) throws SQLException, URISyntaxException, InterruptedException, ExecutionException {
		if(!user.isBotOwner()) {return;}
		Connection connection = Trivia.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM TRIVIA");
		while(rs.next()) {
			String a = "";
			a += "Id: " + rs.getLong("DISCORDID")+ " ";
			a += "Score: " + rs.getInt("SCORE") + " ";
			a += "Name: " + rs.getString("NAME") + " ";
			a += "Server ID: " + rs.getLong("SERVERID") + " ";
			new MessageBuilder().append(a).send(AvocadoBot.bot.getOwner().get());
		}
	}
}
/*
Trivia Table (
	Entry ID -> BIGINT **The Primary Key**
	Discord ID -> BIGINT
	SCORE -> INT
	NAME -> VARCHAR
	Server ID -> BIGINT
)
*/
//stmt.execute("CREATE TABLE TRIVIA(ENTRYID BIGINT PRIMARY KEY,DISCORDID BIGINT, SCORE INT,NAME VARCHAR, SERVERID BIGINT)");