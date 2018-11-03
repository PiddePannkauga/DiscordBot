package Bot.Listeners;


import Bot.Lavalplayer.AudioProvider;
import Bot.Lavalplayer.TrackScheduler;
import Bot.YoutubeApi.YoutubeApi;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Class handling all music related commands and events.
 *
 * @author Petter Månsson 2018-03-03
 */
public class AudioListener implements IListener<MessageReceivedEvent> {

    private AudioPlayerManager playerManager;
    private TrackScheduler trackScheduler;
    private AudioProvider provider;
    private IDiscordClient bot;
    private YoutubeApi youtubeApi = new YoutubeApi();


    public AudioListener(TrackScheduler trackScheduler, AudioPlayerManager playerManager, AudioProvider provider, IDiscordClient bot){
        this.trackScheduler = trackScheduler;
        this.playerManager = playerManager;
        this.provider = provider;
        this.bot = bot;
    }

    @Override
    public void handle(MessageReceivedEvent messageEvent) {
        IMessage message = messageEvent.getMessage();
        IGuild guild = bot.getGuilds().get(0);
        IChannel channel = message.getChannel();

        String[] command = message.getContent().split(" ");
        String botCommand = command[0];

        switch (botCommand) {
            case "!musicjoin":
                joinVoiceChannel(messageEvent, guild);
                break;
            case "!musicleave":
                leaveVoiceChannel(messageEvent, guild);
                break;
            case "!play":
                buildSongRequest(command);
                break;
            case "!pause":
                pause();
                break;
            case "!resume":
                resumeSong();
                break;
            case "!skip":
                skipSong();
                break;
            case "!title":
                fetchTitle(messageEvent);
                break;
            case "!queue":
                showQueue(message,channel);
                break;

        }

    }

    private void joinVoiceChannel(MessageEvent messageEvent, IGuild guild){
        guild.getAudioManager().setAudioProvider(provider);
        IVoiceChannel voice = messageEvent.getAuthor().getVoiceStateForGuild(guild).getChannel();
        voice.join();
    }

    private void leaveVoiceChannel(MessageEvent messageEvent, IGuild guild){
        IVoiceChannel voice = messageEvent.getAuthor().getVoiceStateForGuild(guild).getChannel();
        voice.leave();
    }

    private void queueSong(String songRequest){

        if(songRequest == null){
            resumeSong();
        }else{

            playerManager.loadItem(songRequest, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    trackScheduler.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        trackScheduler.queue(track);
                    }
                }

                @Override
                public void noMatches() {
                    // Notify the user that we've got nothing
                }

                @Override
                public void loadFailed(FriendlyException throwable) {
                    // Notify the user that everything exploded
                }


            });}
    }

    private void pause(){
        trackScheduler.pause();
    }

    private void resumeSong(){
        trackScheduler.resumeSong();
    }

    private void skipSong(){
        trackScheduler.play();
    }

    private void fetchTitle(MessageEvent message){
        new MessageBuilder(message.getClient()).withChannel(message.getChannel()).withContent(trackScheduler.peekTitle()).build();
    }

    private boolean isLink(String song){
        return song.contains("//www.");
    }

    private void buildSongRequest(String[] songRequest) {

        if (isLink(songRequest[1])) {
            queueSong(songRequest[1]);
        } else {

            StringBuilder requestString = new StringBuilder();
            for (int i = 1; i < songRequest.length; i++) {
                if (i == songRequest.length-1) {
                    requestString.append(songRequest[i]);
                } else {
                    requestString.append(songRequest[i] + "_");
                }
            }
            String readyToSearchSong = requestString.toString();
            queueSong(youtubeApi.youtubeURLfromSearch(readyToSearchSong));
        }
    }

    private void showQueue(IMessage message, IChannel channel){
        try {
            new MessageBuilder(message.getClient()).withChannel(channel).withContent(trackScheduler.showQueue()).build();
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
