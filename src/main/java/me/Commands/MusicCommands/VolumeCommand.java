package me.Commands.MusicCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class VolumeCommand extends Command {
    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getDescription() {
        return "Sets the player volume to value.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "volume value`";
    }

    @Override
    public String getType() {
        return "Music";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event, String args) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageFormat("You must enter some value!").queue();
            return;
        }
        int volume;
        try {
            volume = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessageFormat("You need to enter a digit!").queue();
            return;
        }
        if (volume < 0 || volume > 100) {
            event.getChannel().sendMessageFormat("Volume must be from 0%s to 100%s!", '%', '%').queue();
            return;
        }
        MusicManager.getMusicManager().getGuildAudioPlayer(event.getGuild()).player.setVolume(volume);
        event.getChannel().sendMessageFormat("Player volume set to %d%s!", volume, '%').queue();
    }
}
