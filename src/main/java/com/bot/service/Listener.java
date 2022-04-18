package com.bot.service;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Listener extends ListenerAdapter {
    /** This method will automatically create threads in the #resume-review and #career-questions channels, as well as
     * enforce formatting in #resume-review.
     *
     * TODO: Add !faq commands for frequently asked questions. For examples:
     *       - !trimodal - links to tri-modal salaries
     *       - !blind75 - links to the blind75
     *       - etc, whatever is suggested. Should be relatively easy to add to.
     *
     * @param event - the actual message object being send
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = message.getChannel();

        String[] formats = new String[]{"png", "jpg"};
        Set<String> allowedFileFormats = new HashSet<>(Arrays.asList(formats));

        if(message.isFromType(ChannelType.TEXT)){
            //#resume-review
            if(message.getTextChannel().getId().equals("339595755851612161")){
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

            //TODO: add feature to check if user is replying to another person, should be possible with message.getInteractions() just haven't figured it out yet.
            //#career-questions
            if(message.getTextChannel().getId().equals("340166684675407872")){
                message.createThreadChannel(message.getAuthor().getName() + "'s Thread").queue();
            }
        }
    }

    /** When a user leaves this method will check the last 50 messages in #share-your-content or a potential new marketing
     * channel and see if they have posted in there recently. If they have, it will delete that message.
     *
     * @param event - Someone leaving the server will trigger this event.
     *              Whether they have been kicked, banned, or have left of their own accord
     */
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event){

        //channelId = #share-your-content OR #market-research, or I could do one for each
        TextChannel channel = event.getGuild().getTextChannelById("343186849348583463");
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
