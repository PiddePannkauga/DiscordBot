package Bot.Lavalplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Handles all queueing of tracks and operations on the musicqueue.
 *
 * @author Petter Månsson 2018-03-03
 */
public class TrackScheduler extends AudioEventAdapter {

    private AudioPlayer player;
    private BlockingQueue<AudioTrack> trackQueue;
    private BlockingQueue<String> titleQueue = new LinkedBlockingDeque<>();


    public TrackScheduler(AudioPlayer player){
        this.player = player;
        player.setVolume(30);
        trackQueue = new LinkedBlockingDeque<>();
    }


    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            trackQueue.offer(track);
        }
        titleQueue.add(track.getInfo().title);
    }

    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(trackQueue.poll(), false);
        titleQueue.remove();

    }

    public void pause(){
        player.setPaused(true);
    }

    public void resumeSong(){
        player.setPaused(false);
        System.out.println(player.isPaused());
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    public void play(){
        player.setPaused(false);
        nextTrack();
    }

    public String peekTitle(){
        String title = titleQueue.peek();
        return title;
    }

    public String showQueue(){
        StringBuilder queue = new StringBuilder();

        queue.append("Currently playing: " + peekTitle() + "\n");
        for(AudioTrack queueObject : trackQueue) {
            long durationInMillis = queueObject.getDuration();
            long second = (durationInMillis / 1000) % 60;
            long minute = (durationInMillis / (1000 * 60)) % 60;
            long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

            if(hour <= 0){
                String duration = String.format("%02d:%02d", minute, second);
                queue.append(queueObject.getInfo().title + " | Duration: " + duration + "\n");
            }else {
                String duration = String.format("%02d:%02d:%02d", hour, minute, second);
                queue.append(queueObject.getInfo().title + " | Duration: " + duration + "\n");
            }

        }
        return queue.toString();
    }


}
