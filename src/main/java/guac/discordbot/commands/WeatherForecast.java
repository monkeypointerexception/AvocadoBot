package guac.discordbot.commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.json.JSONObject;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import guac.discordbot.Keys;

public class WeatherForecast implements CommandExecutor {
	
	
	@Command(aliases = {"&weather", "&forecast", "&wt" }, 
			description = "Get the weather forecast for the user provided location", 
			usage = "&weather New York City || &wt Boston, Massachusetts")
	
	public void weatherCommand(String[] args, User user, TextChannel channel) throws IOException {
		if(user.isBot()) {return;}
		if(args.length == 0) {
			channel.sendMessage("No location provided :pensive:");
			return;
		}
		//add '+' btw spaces to make it url friendly
		for(int i = 0; i < args.length -1; i++) {
			if(!args[i].contains(",")) {
				args[i] = args[i].concat("+").strip();
			}
		}
		String locale = String.join("", args);
		String url = "https://api.openweathermap.org/data/2.5/weather?q="
				+ locale + "&units=imperial&APPID=" + Keys.wApi;
		try {
			//connect to openweathermap
			URL weatherUrl = new URL(url);
			URLConnection yc = weatherUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}	
			in.close();
			//extract data from json from openweathermap
			JSONObject weatherInfo = new JSONObject(response.toString());
			String country = weatherInfo.getJSONObject("sys").get("country").toString();
			locale = weatherInfo.getString("name");
			locale = locale.concat(", " + country);
			String temp = weatherInfo.getJSONObject("main").get("temp").toString();
			String humd = weatherInfo.getJSONObject("main").get("humidity").toString();
			String cloud = weatherInfo.getJSONObject("clouds").get("all").toString();
			String wind = weatherInfo.getJSONObject("wind").get("speed").toString();
			String des = weatherInfo.getJSONArray("weather").getJSONObject(0).get("description").toString();
			double celsius = (Double.parseDouble(temp) - 32)/1.8;
			String df = new DecimalFormat("###.##").format(celsius);
			//send as embed
			EmbedBuilder embed = new EmbedBuilder()
				.setTitle("Weather Forecast for " + locale)
				.addInlineField("Temperature", temp + "°F/" + df + "°C")
				.addInlineField("Humidity", humd + "%")
				.addInlineField("Description", des)
				.addInlineField("Cloudiness",cloud + "%")
				.addInlineField("Wind Speed", wind + " mph")
				.setColor(Color.CYAN)
				.setFooter("Requested by " + user.getDiscriminatedName(), user.getAvatar())
				.setAuthor("Data provided by OpenWeatherMap")
				.setTimestampToNow();
			String rainsnow = "";
			if(!weatherInfo.isNull("rain")) {
				rainsnow = weatherInfo.getJSONObject("rain").get("1h").toString();
				embed.addInlineField("Rain volume (1h)", rainsnow + " mm");
			}
			if(!weatherInfo.isNull("snow")) {
				rainsnow = weatherInfo.getJSONObject("snow").get("1h").toString();
				embed.addInlineField("Snow volume (1h)", rainsnow + " mm");
			}
			channel.sendMessage(embed);
		} catch (IOException e) {
			channel.sendMessage("invalid location");
		}
	}

}
