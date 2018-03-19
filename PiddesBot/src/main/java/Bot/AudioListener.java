package Bot;


import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;

/**
 * @author Petter Månsson 2018-03-03
 */
public class AudioListener implements IListener<MessageReceivedEvent> {

    private AudioPlayerManager playerManager;
    private TrackScheduler trackScheduler;
    private AudioProvider provider;
    private IDiscordClient bot;


    public AudioListener(TrackScheduler trackScheduler, AudioPlayerManager playerManager, AudioProvider provider, IDiscordClient bot){
        this.trackScheduler = trackScheduler;
        this.playerManager = playerManager;
        this.provider = provider;
        this.bot = bot;
    }

    @Override
    public void handle(MessageReceivedEvent messageEvent) {
        IMessage message = messageEvent.getMessage();
        IUser user = message.getAuthor();
        IGuild guild = bot.getGuilds().get(0);
        String author = user.getName();

        guild.getAudioManager().setAudioProvider(provider);

        if(message.getContent().toLowerCase().contains("!musicjoin")){
            IVoiceChannel voice = messageEvent.getAuthor().getVoiceStateForGuild(guild).getChannel();
            voice.join();


        }
        if(message.getContent().toLowerCase().contains("!kö")){
            String[] command = message.getContent().split(" ");
            String song = command[1];
            playerManager.loadItem(song, new AudioLoadResultHandler() {
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


                });


        }
        if(message.getContent().toLowerCase().contains("!skip")){
            trackScheduler.play();
        }
    }

    private void fetchTitle(){

    }

}
