import Bot.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

/**
 * @author Petter Månsson 2018-03-01
 */
public class Main {

    private static final String TOKEN="NDE2OTA0Mjg3NTU4MzAzNzQ0.DXl2XA.SbBYsBSZXX_ZSr9CtyNxeosJ0tw";
    public static void main(String[] args) {
        CommandHandler message =  new CommandHandler();
        IDiscordClient bot = Client.createClient(TOKEN, true);

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        playerManager.setFrameBufferDuration(30);
        AudioPlayer player = playerManager.createPlayer();
        AudioProvider provider = new AudioProvider(player);
        TrackScheduler scheduler = new TrackScheduler(player);
        player.addListener(scheduler);

        EventDispatcher dispatcher = bot.getDispatcher(); // Gets the EventDispatcher instance for this client instance
        dispatcher.registerListener(new InterfaceListener());
        dispatcher.registerListener(new GreetingListener());
        dispatcher.registerListener(message);
        dispatcher.registerListener(new AudioListener(scheduler,playerManager,provider,bot));


    }
}
