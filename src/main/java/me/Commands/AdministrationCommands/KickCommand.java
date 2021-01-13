package me.Commands.AdministrationCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class KickCommand extends Command {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kicks a user from this guild.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "kick @user reason`";
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
        String reason = args.substring(member.getAsMention().length() + 1);
        if (reason.startsWith(" ")) {
            reason = reason.substring(1);
        }
        event.getGuild().kick(member, reason).reason(reason).queue(
                any -> event.getChannel().sendMessageFormat("Kicked %s!", member.getAsMention()).queue(),
                error -> event.getChannel().sendMessageFormat("Cannot kick a user with error: `%s!`", error.getMessage()).queue()
        );
    }
}
