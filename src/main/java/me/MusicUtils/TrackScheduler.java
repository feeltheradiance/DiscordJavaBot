package me.MusicUtils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final Guild guild;
    private final TextChannel channel;
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.guild = guild;
        this.channel = guild.getTextChannelsByName("music", true).get(0);
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        player.setPaused(false);
        if (queue.isEmpty()) {
            channel.sendMessage("Seems like the playlist is empty, then I have to disconnect!").queue();
            if (guild.getAudioManager().isConnected()) {
                guild.getAudioManager().closeAudioConnection();
            }
            player.startTrack(null, false);
            return;
        }
        if (queue.element() != null) {
            channel.sendMessage(BuildEmbed(queue.element())).queue();
        }
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return this.queue;
    }

    public void clearQueue() {
        this.queue.clear();
    }

    private MessageEmbed BuildEmbed(AudioTrack Track) {
        EmbedBuilder embedMessage = new EmbedBuilder()
                .setTitle("Now playing")
                .setColor(Color.RED)
                .setDescription(Track.getInfo().title)
                .setThumbnail("http://img.youtube.com/vi/" + Track.getInfo().identifier + "/0.jpg")
                .addField("Channel", Track.getInfo().author, true)
                .addField("Duration", String.format("%02d:%02d", Track.getInfo().length/ 1000 / 60 % 60, Track.getInfo().length/ 1000 % 60), true)
                .addField("Volume", player.getVolume() + "%", true);
        return embedMessage.build();
    }
}