package me.Commands.MusicCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import java.awt.*;

public class LyricsCommand extends Command {
    @Override
    public String getName() {
        return "lyrics";
    }

    @Override
    public String getDescription() {
        return "Shows lyrics of the current playing track.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "lyrics`";
    }

    @Override
    public String getType() {
        return "Music";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event, String args) {
        if (!args.isEmpty()) {
            event.getChannel().sendMessageFormat("You don't need any arguments to use this command!").queue();
            return;
        }
        AudioTrack audioTrack = MusicManager.getMusicManager().getGuildAudioPlayer(event.getGuild()).player.getPlayingTrack();
        MusixMatch musixMatch = new MusixMatch(JavaBotConfig.get("MUSIXMATCH"));
        String trackSearch = audioTrack.getInfo().title;
        Track track;
        if (trackSearch.contains(" - ")) {
            String trackAuthor = trackSearch.substring(0, trackSearch.indexOf("-") - 1),
                    trackName = trackSearch.substring(trackSearch.indexOf("-") + 1);
            try {
                track = musixMatch.getMatchingTrack(trackName, trackAuthor);
            } catch (Exception e) {
                event.getChannel().sendMessageFormat("Nothing found by %s!", trackSearch).queue();
                return;
            }
        } else {
            try {
                track = musixMatch.getMatchingTrack(trackSearch, "");
            } catch (Exception e) {
                event.getChannel().sendMessageFormat("Nothing found by %s!", trackSearch).queue();
                return;
            }
        }
        TrackData trackData = track.getTrack();
        Lyrics lyrics;
        try {
            lyrics = musixMatch.getLyrics(trackData.getTrackId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(audioTrack.getInfo().title)
                .setColor(Color.RED)
                .addField("Lyrics", lyrics.getLyricsBody(), true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }
}
