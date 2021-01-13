package me.Commands.MusicCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ResumeCommand extends Command {
    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getDescription() {
        return "Resumes playing track.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "resume`";
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
        if (MusicManager.getMusicManager().getGuildAudioPlayer(event.getGuild()).player.isPaused()) {
            MusicManager.getMusicManager().getGuildAudioPlayer(event.getGuild()).player.setPaused(false);
            event.getChannel().sendMessageFormat("Player is resumed!").queue();
        } else {
                event.getChannel().sendMessageFormat("Player is not paused!").queue();
        }
    }
}
