package me.Commands.AdministrationCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnbanCommand extends Command {
    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "Unban a mentioned user.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "ban @user`";
    }

    @Override
    public String getType() {
        return "Administration";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event, String args) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageFormat("You must enter some arguments!").queue();
            return;
        }
        for (Guild.Ban ban : event.getGuild().retrieveBanList().complete()) {
            if (ban.getUser().getName().equals(args) || ban.getUser().getId().equals(args) || String.format("%#s", ban.getUser()).equals(args)) {
                event.getGuild().unban(ban.getUser()).queue();
                event.getChannel().sendMessageFormat("User %s is unbanned!", args).queue();
                return;
            }
        }
        event.getChannel().sendMessageFormat("User %s is not banned!", args).queue();
    }
}
