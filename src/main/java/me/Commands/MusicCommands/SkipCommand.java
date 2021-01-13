package me.Commands.MusicCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SkipCommand extends Command {

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips track, which is playing right now.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "skip`";
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
        if (musicManager.getGuildAudioPlayer(event.getGuild()).player.getPlayingTrack() != null) {
            event.getChannel().sendMessageFormat("Track skipped!").queue();
            musicManager.getGuildAudioPlayer(event.getGuild()).scheduler.nextTrack();
        } else {
                event.getChannel().sendMessageFormat("There is nothing to skip!").queue();
        }
    }
}
