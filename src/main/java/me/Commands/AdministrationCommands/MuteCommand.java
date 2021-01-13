package me.Commands.AdministrationCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MuteCommand extends Command {
    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Mutes a user for *minutes* minutes.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "mute @user minutes`";
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
        Member member = event.getMessage().getMentionedMembers().get(0);
        args = args.substring(member.getAsMention().length() + 1);
        int minutes;
        if (args.startsWith(" ")) {
            args = args.substring(1);
        }
        try {
            minutes = Integer.parseInt(args);
        } catch (Exception exception) {
            event.getChannel().sendMessageFormat("You might enter something wrong!").queue();
            return;
        }
        try {
            event.getGuild().mute(member, true).deadline(minutes).queue(
                    any -> event.getChannel().sendMessageFormat("Muted %s!", member.getAsMention()).queue(),
                    error -> event.getChannel().sendMessageFormat("Error: %s!", error.getMessage()).queue()
            );
        } catch (Exception exception) {
            event.getChannel().sendMessageFormat("Cannot mute a user with error: `%s!`", exception.getMessage()).queue();
        }
    }
}
