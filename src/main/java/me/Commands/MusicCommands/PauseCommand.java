package me.Commands.MusicCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PauseCommand extends Command {

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "Pauses playing track.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "pause`";
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
        if (!MusicManager.getMusicManager().getGuildAudioPlayer(event.getGuild()).player.isPaused()) {
            MusicManager.getMusicManager().getGuildAudioPlayer(event.getGuild()).player.setPaused(true);
            event.getChannel().sendMessageFormat("Player is now paused!").queue();
        } else  {
                event.getChannel().sendMessageFormat("Player is already paused!").queue();
        }
    }
}
