package me.Commands.MusicCommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class PlayingCommand extends Command {
    @Override
    public String getName() {
        return "playing";
    }

    @Override
    public String getDescription() {
        return "Gives info about playing track.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "playing`";
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
        AudioPlayer player = musicManager.getGuildAudioPlayer(event.getGuild()).player;
        event.getChannel().sendMessage(playingTrackEmbed(player)).queue();
    }

    public static MessageEmbed playingTrackEmbed(AudioPlayer player) {
        AudioTrack track = player.getPlayingTrack();
        EmbedBuilder embedMessage = new EmbedBuilder()
                .setTitle("Now playing")
                .setColor(Color.RED)
                .setDescription(track.getInfo().title)
                .setThumbnail("http://img.youtube.com/vi/" + track.getInfo().identifier + "/0.jpg")
                .addField("Channel", track.getInfo().author, true)
                .addField("Duration", String.format("%02d:%02d", track.getInfo().length/ 1000 / 60 % 60, track.getInfo().length/ 1000 % 60), true)
                .addField("Volume", player.getVolume() + "%", true);
        return embedMessage.build();
    }
}