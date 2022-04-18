package com.bot.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Listener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = message.getChannel();

        String[] formats = new String[]{"png", "jpg"};
        Set<String> allowedFileFormats = new HashSet<>(Arrays.asList(formats));

        //Will throw weird errors without this outer if statement, but still work idk
        if(message.isFromType(ChannelType.TEXT)){
            //#resume-review
            if(message.getTextChannel().getId().equals("965430706274717746")){
                //System.out.println(message.getAttachments().get(0).getFileExtension());
                if(message.getAttachments().size() > 0 && !allowedFileFormats.contains(message.getAttachments().get(0).getFileExtension())){
                    User badAuthor = message.getAuthor();
                    badAuthor.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("#resume-review requires you to send images of your resume in a .png or .jpg format, Send us a picture of it and we'd be happy to help!")).queue();
                    message.delete().queue();

                }else if(message.getAttachments().size() == 0){
                    User badAuthor = message.getAuthor();
                    badAuthor.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("#resume-review requires you to send images of your resume in a .png or .jpg format, Send us a picture of it and we'd be happy to help!")).queue();
                    message.delete().queue();

                }else{
                    message.createThreadChannel(message.getAuthor().getName() + "'s Thread").queue();
                }
            }

            //Later add feature to check if user is replying to another person, should be possible with message.getInteractions() just haven't figured it out yet.
            //#career-questions
            if(message.getTextChannel().getId().equals("650907499922456600")){
                message.createThreadChannel(message.getAuthor().getName() + "'s Thread").queue();
            }
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event){
        //Get latest message
        //search past 50 messages (?)
        //If message.author = user, queue up message for deletion

        //channelId = #share-your-content OR #market-research, or I could do one for each
        TextChannel channel = event.getGuild().getTextChannelById("965430706274717746");
        String messageId = channel.getLatestMessageId();

        channel.getHistory().retrievePast(50).queue(messages -> {
            for(Message message: messages){
                if(message.getAuthor().equals(event.getUser())){
                    message.delete().queue();
                }
            }
        });

    }
}
