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
    @Override
    public void handle(PresenceUpdateEvent presenceUpdateEvent) {

        IPresence presence = presenceUpdateEvent.getNewPresence();
        IChannel channel = presenceUpdateEvent.getClient().getChannels().get(0);
        IEmoji emoji = presenceUpdateEvent.getClient().getGuilds().get(0).getEmojiByName("Pochinkipidde");
        String user = presenceUpdateEvent.getUser().getName();
        System.out.println(presenceUpdateEvent.getUser().getName());
        if(presence.getStatus().equals(StatusType.ONLINE)&& user.equals("Knackehaxan")){
            try {
                // Builds (sends) and new message in the channel that the original message was sent with the content of the original message.
                new MessageBuilder(presenceUpdateEvent.getClient()).withChannel(channel).withContent(emoji+ " B0RGMästarN^ har frälsat oss med sin heliga närvaro! " + emoji).build();
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
}
