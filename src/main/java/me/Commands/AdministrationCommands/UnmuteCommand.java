package me.Commands.AdministrationCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnmuteCommand extends Command {
    @Override
    public String getName() {
        return "unmute";
    }

    @Override
    public String getDescription() {
        return "Unmute a mentioned user.\n" +
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
        Member member = event.getMessage().getMentionedMembers().get(0);
        if (!args.substring(3).endsWith(member.getAsMention().substring(2))) {
            event.getChannel().sendMessageFormat("You might enter something wrong!").queue();
            return;
        }
        try {
            event.getGuild().mute(member, false).queue(
                    any -> event.getChannel().sendMessageFormat("Unmuted %s!", member.getAsMention()).queue(),
                    error -> event.getChannel().sendMessageFormat("%s is not muted!", member.getAsMention()).queue()
            );
        } catch (Exception exception) {
            event.getChannel().sendMessageFormat("Cannot unmute a user with error: `%s!`", exception.getMessage()).queue();
        }
    }
}
