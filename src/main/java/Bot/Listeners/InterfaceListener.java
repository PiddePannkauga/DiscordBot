package Bot.Listeners;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author Petter Månsson 2018-03-01
 */
public class InterfaceListener implements IListener<ReadyEvent> {

    @Override
    public void handle(ReadyEvent event) {
        try {
            event.getClient().changeUsername(System.getenv("BOT_NAME"));
        } catch (RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }
}
