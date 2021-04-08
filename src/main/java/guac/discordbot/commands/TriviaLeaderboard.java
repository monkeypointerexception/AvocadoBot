package guac.discordbot.commands;

import java.awt.Color;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class TriviaLeaderboard implements CommandExecutor {
	
	@Command(aliases = {"&leaderboard", "&lb"}, description = "gets top 5 in trivia leaderboard for the server", 
			usage = "&lb || &leaderboard")
	
	public void leaderBoardCommand(TextChannel channel, User user, Server server) {
		try {
			channel.sendMessage(getLeaderBoard(user,server.getIdAsString(),server.getName()));
		} catch (SQLException | URISyntaxException e) {
			channel.sendMessage("unable to execute request");
		}
	}
	public EmbedBuilder getLeaderBoard(User user, String serverId, String serverName) throws SQLException, URISyntaxException {
		Connection connection = Trivia.getConnection();
		Statement stmt = connection.createStatement();
		Color c = new Color(255,204,51).darker();
		EmbedBuilder lb = new EmbedBuilder()
				.setTitle(serverName + " LeaderBoard")
				.setColor(c);
		ResultSet rs = stmt.executeQuery("SELECT * FROM TRIVIA WHERE SERVERID = " + serverId +" ORDER BY SCORE DESC LIMIT 5");
		int i = 1;
		while(rs.next() && i <= 5) {
			String a = "";
			a += "**SCORE:** " + rs.getInt("SCORE") + " **|** ";
			a += "**NAME:** " + rs.getString("NAME");
			lb.addField(Integer.toString(i), a);
			i++;
		}
		lb.setFooter("Requested by "+ user.getDiscriminatedName(), user.getAvatar());
		return lb;
	}
}