package me.MusicUtils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private static MusicManager musicManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager playerManager;

    public MusicManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public void loadAndPlay(TextChannel channel, VoiceChannel voiceChannel, String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                EmbedBuilder embedMessage = new EmbedBuilder()
                        .setTitle("Added to queue")
                        .setColor(Color.RED)
                        .setDescription(audioTrack.getInfo().title)
                        .setThumbnail("http://img.youtube.com/vi/" + audioTrack.getInfo().identifier + "/0.jpg")
                        .addField("Channel", audioTrack.getInfo().author, true)
                        .addField("Duration", String.format("%02d:%02d", audioTrack.getInfo().length/ 1000 / 60 % 60, audioTrack.getInfo().length/ 1000 % 60), true)
                        .addField("Position", Integer.toString(musicManager.scheduler.getQueue().size() + 1), true);
                channel.sendMessage(embedMessage.build()).queue();
                play(channel.getGuild(), musicManager, voiceChannel, audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                EmbedBuilder embedMessage = new EmbedBuilder()
                        .setTitle("Playlist added to queue")
                        .setColor(Color.RED)
                        .setThumbnail("http://img.youtube.com/vi/" + audioPlaylist.getTracks().get(0).getInfo().identifier + "/0.jpg");
                int count = 1;
                StringBuilder playlist = new StringBuilder();
                for (AudioTrack audioTrack: audioPlaylist.getTracks() ) {
                    play(channel.getGuild(), musicManager, voiceChannel, audioTrack);
                    if (count <= 10) {
                        playlist.append(count).append(". ").append(audioTrack.getInfo().title).append("\n");
                    }
                    count++;
                }
                embedMessage.addField("Playlist", playlist.toString(), false);
                if (count > 10) {
                    embedMessage.setFooter("and " + (count-10) + " more");
                }
                channel.sendMessage(embedMessage.build()).queue();
            }

            @Override
            public void noMatches() {
                channel.sendMessageFormat("Nothing found by %s", trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessageFormat("Could not play: %s", e.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, VoiceChannel voiceChannel,AudioTrack audioTrack) {
        connectToVoiceChannel(guild.getAudioManager(), voiceChannel);
        musicManager.scheduler.queue(audioTrack);
    }

    private static void connectToVoiceChannel(AudioManager audioManager, VoiceChannel voiceChannel) {
        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(voiceChannel);
        }
    }

    public static MusicManager getMusicManager() {
        if (musicManager == null) {
            musicManager = new MusicManager();
        }
        return musicManager;
    }

    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);
        if(musicManager == null) {
            musicManager = new GuildMusicManager(playerManager, guild);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }
}
