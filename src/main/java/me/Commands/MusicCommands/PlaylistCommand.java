package me.Commands.MusicCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class PlaylistCommand extends Command {
    @Override
    public String getName() {
        return "playlist";
    }

    @Override
    public String getDescription() {
        return "Shows a playlist queue.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "playlist`";
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
        MusicManager musicManager = MusicManager.getMusicManager();
        if (musicManager.getGuildAudioPlayer(event.getGuild()).scheduler.getQueue().isEmpty() &&
                musicManager.getGuildAudioPlayer(event.getGuild()).player.getPlayingTrack() == null) {
            event.getChannel().sendMessageFormat("Playlist queue is empty!").queue();
        } else {
            EmbedBuilder embedMessage = new EmbedBuilder()
                    .setTitle("Playlist")
                    .setColor(Color.RED)
                    .setThumbnail("https://www.vhv.rs/dpng/d/29-290046_retro-music-png-transparent-png.png")
                    .addField("Now playing", musicManager.getGuildAudioPlayer(event.getGuild()).player.getPlayingTrack().getInfo().title, false);
            StringBuilder playlist = new StringBuilder();
            int count = 1;
            for (AudioTrack track: musicManager.getGuildAudioPlayer(event.getGuild()).scheduler.getQueue()) {
                if (count < 10) {
                    playlist.append(count).append(". ").append(track.getInfo().title).append("\n");
                }
                count++;
            }
            if (!playlist.toString().isEmpty()) {
                embedMessage.addField("Queue", playlist.toString(), false);
            } else {
                embedMessage.addField("Queue", "Queue is empty!", false);
            }
            if (count > 10) {
                embedMessage.setFooter("and " + (count-10) + " more");
            }
            event.getChannel().sendMessage(embedMessage.build()).queue();
        }
    }
}
