package Bot;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author Petter Månsson 2018-03-01
 */
public class InterfaceListener implements IListener<ReadyEvent> { // The event type in IListener<> can be any class which extends Event

    @Override
    public void handle(ReadyEvent event) { // This is called when the ReadyEvent is dispatched
        try {
            // This WILL work. The ReadyEvent has been fired and the bot is ready to interact with Discord.
            event.getClient().changeUsername("PidorTown Polis");
        } catch (RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }
}
