package Bot;


import Utility.FileReader;
import sx.blah.discord.api.events.IListener;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

import sx.blah.discord.util.*;


import java.util.Calendar;


/**
 * @author Petter Månsson 2018-03-01
 */
public class CommandHandler implements IListener<MessageEvent> {

    private String law = FileReader.readFile("PidorTownLag.txt");

    @Override
    public void handle(MessageEvent event) {

        IMessage message = event.getMessage(); // Gets the message from the event object NOTE: This is not the content of the message, but the object itself
        IChannel channel = message.getChannel(); // Gets the channel in which this message was sent.

        if (message.getContent().equals("!Lag")) {
            lag(message,channel);
        }
        if(message.getContent().toLowerCase().contains("!erdetfredag")) {
            fredag(message,channel);
        }

    }

    private void lag(IMessage message, IChannel channel){
        try {
            // Builds (sends) and new message in the channel that the original message was sent with the content of the original message.
            new MessageBuilder(message.getClient()).withChannel(channel).withContent(law).build();
        } catch (RateLimitException e) { // RateLimitException thrown. The bot is sending messages too quickly!
            System.err.print("Sending messages too quickly!");
            e.printStackTrace();
        } catch (DiscordException e) { // DiscordException thrown. Many possibilities. Use getErrorMessage() to see what went wrong.
            System.err.print(e.getErrorMessage()); // Print the error message sent by Discord
            e.printStackTrace();
        } catch (MissingPermissionsException e) { // MissingPermissionsException thrown. The bot doesn't have permission to send the message!
            System.err.print("Missing permissions for channel!");
            e.printStackTrace();
        }
    }

    private void fredag(IMessage message, IChannel channel){
        Calendar cal = Calendar.getInstance();

        try {
            // Builds (sends) and new message in the channel that the original message was sent with the content of the original message.
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                new MessageBuilder(message.getClient()).withChannel(channel).withContent("JA").build();
            }else{
                new MessageBuilder(message.getClient()).withChannel(channel).withContent("NAJ").build();
            }
        } catch (RateLimitException e) { // RateLimitException thrown. The bot is sending messages too quickly!
            System.err.print("Sending messages too quickly!");
            e.printStackTrace();
        } catch (DiscordException e) { // DiscordException thrown. Many possibilities. Use getErrorMessage() to see what went wrong.
            System.err.print(e.getErrorMessage()); // Print the error message sent by Discord
            e.printStackTrace();
        } catch (MissingPermissionsException e) { // MissingPermissionsException thrown. The bot doesn't have permission to send the message!
            System.err.print("Missing permissions for channel!");
            e.printStackTrace();
        }

    }


}
