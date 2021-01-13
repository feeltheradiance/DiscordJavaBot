package me.Commands.MusicCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.GuildMusicManager;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class StopCommand extends Command {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stops the player and disconnects from the voice channel.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "stop`";
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
        GuildMusicManager musicManager = MusicManager.getMusicManager().getGuildAudioPlayer(event.getGuild());
        musicManager.player.stopTrack();
        musicManager.scheduler.clearQueue();
        event.getChannel().sendMessageFormat("Player has been stopped!").queue();
        event.getGuild().getAudioManager().closeAudioConnection();
    }
}
