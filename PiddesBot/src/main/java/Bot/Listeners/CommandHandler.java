package Bot.Listeners;


import Utility.FileReader;
import sx.blah.discord.api.events.IListener;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

import sx.blah.discord.util.*;


import java.util.Calendar;


/**
 * @author Petter Månsson 2018-03-01
 */
public class CommandHandler implements IListener<MessageReceivedEvent> {

    private String law = FileReader.readFile("PidorTownLag.txt");
    private String commandList = FileReader.readFile("commandlist.txt");

    @Override
    public void handle(MessageReceivedEvent event) {
        System.out.println("MessageEvent");
        IMessage message = event.getMessage();
        IChannel channel = message.getChannel();
        String command = message.getContent();

        switch(command){
            case "!lag": lag(message,channel);
                break;
            case "!erdetfredag": fredag(message,channel);
                break;
            case "!commands": commandList(message,channel);
                break;

        }

    }

    private void lag(IMessage message, IChannel channel){
        try {

            new MessageBuilder(message.getClient()).withChannel(channel).withContent(law).build();
        } catch (RateLimitException e) {
            System.err.print("Sending messages too quickly!");
            e.printStackTrace();
        } catch (DiscordException e) {
            System.err.print(e.getErrorMessage());
            e.printStackTrace();
        } catch (MissingPermissionsException e) {
            System.err.print("Missing permissions for channel!");
            e.printStackTrace();
        }
    }

    private void commandList(IMessage message, IChannel channel){
        try {

            new MessageBuilder(message.getClient()).withChannel(channel).withContent(commandList).build();
        } catch (RateLimitException e) {
            System.err.print("Sending messages too quickly!");
            e.printStackTrace();
        } catch (DiscordException e) {
            System.err.print(e.getErrorMessage());
            e.printStackTrace();
        } catch (MissingPermissionsException e) {
            System.err.print("Missing permissions for channel!");
            e.printStackTrace();
        }
    }

    private void fredag(IMessage message, IChannel channel){
        Calendar cal = Calendar.getInstance();
        try {

            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                new MessageBuilder(message.getClient()).withChannel(channel).withContent("JA").build();
            }else{
                new MessageBuilder(message.getClient()).withChannel(channel).withContent("NAJ").build();
            }
        } catch (RateLimitException e) {
            System.err.print("Sending messages too quickly!");
            e.printStackTrace();
        } catch (DiscordException e) {
            System.err.print(e.getErrorMessage());
            e.printStackTrace();
        } catch (MissingPermissionsException e) {
            System.err.print("Missing permissions for channel!");
            e.printStackTrace();
        }

    }


}
