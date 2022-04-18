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
     * TODO: Find a way to link to the actual thread, instead of the head message of the thread when bad messages are deleted.
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
            if(channel.getId().equals("965430706274717746")){
                //if the attachment is not one of the allowed formats
                if(!message.getAttachments().isEmpty() && !allowedFileFormats.contains(message.getAttachments().get(0).getFileExtension())){
                    User badAuthor = message.getAuthor();
                    String badMessageText = message.getContentRaw();

                    badAuthor.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("#resume-review requires you to send an image of your resume in a .png or .jpg format. " +
                            "\n\nBecause of this, your message: \" " +badMessageText + "\" was deleted. Repost your message with a picture of your resume and we'd be happy to help!")).queue();
                    message.delete().queue();

                //If there are no attachments
                }else if(message.getAttachments().isEmpty()){
                    User badAuthor = message.getAuthor();
                    String badMessageText = message.getContentRaw();

                    badAuthor.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("#resume-review requires you to send an image of your resume in a .png or .jpg format. " +
                            "\n\nBecause of this, your message: \" " +badMessageText + "\" was deleted. Repost your message with a picture of your resume and we'd be happy to help!")).queue();                    message.delete().queue();

                //If this message is in reply to another message at the top-level instead of in a thread.
                }else if(message.getType().equals(MessageType.INLINE_REPLY)) {
                    User badAuthor = message.getAuthor();
                    String badMessageText = message.getContentRaw();
                    User goodAuthor = message.getReferencedMessage().getAuthor();
                    Message goodMessage = message.getReferencedMessage();

                    badAuthor.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("In an effort to keep clutter to a minimum in #resume-review," +
                            " we prefer to keep all replies in threads. \n\nYour message: \"" + badMessageText +
                            "\n\n was deleted. Feel free to repost it in " + goodAuthor.getName() + "'s thread here: " + goodMessage.getJumpUrl())).queue();
                    message.delete().queue();

                //Finally, create the thread.
                }else{
                    message.createThreadChannel(message.getAuthor().getName() + "'s Thread").queue();
                }
            }

            //#career-questions
            if(channel.getId().equals("650907499922456600")){

                //If the message is a reply to another message at the top-level instead of in a thread.
                if(message.getType().equals(MessageType.INLINE_REPLY)){
                    User badAuthor = message.getAuthor();
                    String badMessageText = message.getContentRaw();
                    User goodAuthor = message.getReferencedMessage().getAuthor();
                    Message goodMessage = message.getReferencedMessage();

                    badAuthor.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("In an effort to keep clutter to a minimum in #career-questions," +
                            " we prefer to keep all replies in threads. \n\nYour message: \n\n" + badMessageText +
                            "\n\n was deleted. Feel free to repost it in " + goodAuthor.getName() + "'s thread here: " + goodMessage.getJumpUrl())).queue();
                    message.delete().queue();
                }else{
                    message.createThreadChannel(message.getAuthor().getName() + "'s Thread").queue();
                }
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

        channel.getHistory().retrievePast(50).queue(messages -> {
            for(Message message: messages){
                if(message.getAuthor().equals(event.getUser())){
                    message.delete().queue();
                }
            }
        });
    }
}
