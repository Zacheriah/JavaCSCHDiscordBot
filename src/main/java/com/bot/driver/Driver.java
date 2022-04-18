package com.bot.driver;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import com.bot.service.Listener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.EnumSet;

/**
 * This class creates the JDA object to be used elsewhere. Right now it just calls the listener to listen for events.
 * You will need to add a keystore.txt file to ./src/main/resources/keystore.txt
 */
public class Driver extends ListenerAdapter {
    static JDA jda;
    public static void main(String[] args) throws LoginException, ParseException {
        String token = "";

        Path path = Paths.get("./src/main/resources/keystore.txt");
        token = new String(Files.readAllBytes(path));
        //token = "Use this for testing if you don't have a keystore set up";
        jda = JDABuilder.createDefault(token, EnumSet.allOf(GatewayIntent.class))
                .setActivity(Activity.watching("my elo drop"))
                .addEventListeners(new Listener())
                .build();

    }
}