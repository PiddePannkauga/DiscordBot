package Bot.Listeners;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiLoader;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.api.internal.json.objects.EmojiObject;
import sx.blah.discord.handle.impl.events.user.PresenceUpdateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IPresence;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author Petter Månsson 2018-03-01
 */
public class GreetingListener implements IListener<PresenceUpdateEvent> {

    private int greetingsMessageCounter = 0;
    private static int ALLOWED_NUMBER_OF_GREETINGS = 1;
    private String mayor = "Knackehaxan";

    @Override
    public void handle(PresenceUpdateEvent presenceUpdateEvent) {

        IPresence presence = presenceUpdateEvent.getNewPresence();
        IChannel channel = presenceUpdateEvent.getClient().getChannels().get(0);
        IEmoji pochinkipidde = presenceUpdateEvent.getClient().getGuilds().get(0).getEmojiByName("Pochinkipidde");
        String user = presenceUpdateEvent.getUser().getName();

        boolean isMayor = checkMayor(user);

        if(isMayor){
            resetGreetingsCounter(presence);
            if(greetingsMessageCounter < ALLOWED_NUMBER_OF_GREETINGS) {
                try {

                    new MessageBuilder(presenceUpdateEvent.getClient()).withChannel(channel).withContent(pochinkipidde + " B0RGMästarN^ har frälsat oss med sin heliga närvaro! " + pochinkipidde).build();
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
                greetingsMessageCounter++;
            }
        }

    }

    private boolean checkMayor(String user){
        if(user.equals(mayor)){
            return true;
        }else{
            return false;
        }
    }

    private void resetGreetingsCounter(IPresence presence){
        boolean switchedToOffline = presence.getStatus().equals(StatusType.OFFLINE);
        if(switchedToOffline){
            greetingsMessageCounter = 0;
        }
    }
}
