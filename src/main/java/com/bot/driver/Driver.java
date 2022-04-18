package com.bot.driver;
/*
Feature list:
- Top scoring posts of the week
- lowest scoring posts of the week
- most controversial posts of the week
- all of the above of the month
- Birthday announcer
 */
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import com.bot.service.Listener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.Timer;

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