package guac.discordbot.commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.text.StringEscapeUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;
import org.json.JSONArray;
import org.json.JSONObject;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import guac.discordbot.Keys;

public class Trivia implements CommandExecutor {
	
	public static String[] values = {"🅰️","🅱️","🇨","🇩"};
	
	@Command(aliases = {"&trivia"}, description = "sends an embed with a question for the user to answer", usage = "&trivia")
	
	public void triviaCommand(User user, TextChannel channel, Server server) throws SQLException, URISyntaxException {
		if(user.isBot()) {return;}
		
		String str = "", q = "", c = "";
		ArrayList<String> ans = new ArrayList<String>();
		try {
			str = connectToApi();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//parse data from the json url
		JSONObject triv = new JSONObject(str);
		q = StringEscapeUtils.unescapeHtml4(triv.getJSONArray("results").getJSONObject(0).get("question").toString());
		c = StringEscapeUtils.unescapeHtml4(triv.getJSONArray("results").getJSONObject(0).get("correct_answer").toString());
		JSONArray arr = triv.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers");
		for(int i = 0; i < arr.length(); i++) {
			ans.add(StringEscapeUtils.unescapeHtml4(arr.getString(i)));
		}
		ans.add(c);
		deployEmbed(channel, q, c, ans, user,server.getIdAsString());
		
	}
	public String connectToApi() throws IOException {
		String url = "https://opentdb.com/api.php?amount=1";
		URL trivURL;
		//connect to trivia api
		trivURL = new URL(url);
		URLConnection yc = trivURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		}	
		in.close();
		//close connection
		return response.toString();
	}
	
	public void deployEmbed(TextChannel ch,String q, String c, ArrayList<String> ans, User user, String serverId) {
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(Color.MAGENTA)
			.setTitle(q)
			.setDescription("You have 1 minute. After 1 minute has passed I will no longer accept responses")
			.setFooter("Requested by "+ user.getDiscriminatedName() + " | Questions provided by OpenTDB", user.getAvatar())
			.setTimestampToNow();
		Collections.shuffle(ans);
		String[] correct = new String[1];
		for(int i = 0; i < ans.size(); i++) {
			embed.addField(values[i], ans.get(i));
			if(ans.get(i).equals(c)) {
				correct[0] = values[i];
			}
		}
		CompletableFuture<Message> a = ch.sendMessage(embed);
		//add reactions to message using lambda
		a.thenAccept(message1 -> { Arrays.stream(values).limit(ans.size()).forEach(values -> message1.addReaction(values));
		@SuppressWarnings("rawtypes")
		//bypass effectively final by using an array
		ListenerManager[] listen = new ListenerManager[1];
			listen[0] = message1.addReactionAddListener(reactEvent -> {
				//count reactions	
				try {
					if(!reactEvent.requestUser().get().equals(user)) {return;}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				if(reactEvent.getEmoji().equalsEmoji(correct[0])) {
					new MessageBuilder().append("That is correct " + user.getMentionTag()).send(ch);
					listen[0].remove();
					try {
						updateUserScore(user.getIdAsString(), user.getDiscriminatedName(), serverId);
					} catch (URISyntaxException | SQLException e) {
						e.printStackTrace();
					}
				} else {
					listen[0].remove();
					new MessageBuilder().append("That is incorrect " + user.getMentionTag())
						.appendNewLine().append("Correct answer was " + c).send(ch);
				}
			}).removeAfter(1, TimeUnit.MINUTES);
		});
	}
	
	public void updateUserScore(String userId, String name, String serverId) throws URISyntaxException, SQLException {
		int x = 0;
		Connection connection = getConnection();
		Statement stmt = connection.createStatement();
		
		if(getUserScore(userId,serverId).isEmpty()) {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM TRIVIA");
			rs.next();
		    int entry = rs.getInt(1) + 1;
			stmt.execute("INSERT INTO TRIVIA(ENTRYID,DISCORDID,SCORE,NAME,SERVERID) VALUES "
					+ "(" + entry +","+ userId +",1,'"+ name + "',"+ serverId + ")");
		    stmt.close();
		    connection.close();
		    return;
		}
		ResultSet rs = stmt.executeQuery("SELECT * FROM TRIVIA WHERE DISCORDID = "+ userId + "AND SERVERID = " + serverId);
		while(rs.next()) {
			x = rs.getInt("SCORE");
			x++;
			stmt.execute("UPDATE TRIVIA SET SCORE = "+ x +"WHERE DISCORDID = "+ userId + "AND SERVERID = " + serverId);
			stmt.execute("UPDATE TRIVIA SET NAME = "+ "'"+ name + "'" +"WHERE DISCORDID = "+ userId + "AND SERVERID = " + serverId);
			//System.out.println("score updated");
		}
        rs.close();
        stmt.close();
        connection.close();
	}	
	
	public static String getUserScore(String userId, String serverId) throws URISyntaxException, SQLException {
		String a = "";
		Connection connection = getConnection();
		Statement stmt = connection.createStatement();
			
		ResultSet rs = stmt.executeQuery("SELECT SCORE FROM TRIVIA WHERE DISCORDID = "+ userId + "AND SERVERID = "+ serverId);
		while(rs.next()) {
			a = rs.getString("SCORE");
		}
        rs.close();
        stmt.close();
        connection.close();
		return a;
	}	
	
	public static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(Keys.dbu);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
	}
}