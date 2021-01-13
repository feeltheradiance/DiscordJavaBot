package me.Commands.AdministrationCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BanCommand extends Command {
    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Bans a user for *days* days.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "ban @user days reason`";
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
        int days;
        if (args.startsWith(" ")) {
            args = args.substring(1);
        }
        try {
            days = Integer.parseInt(args.substring(0, args.indexOf(" ")));
        } catch (Exception exception) {
            event.getChannel().sendMessageFormat("You might enter something wrong!").queue();
            return;
        }
        if (days > 7) {
            event.getChannel().sendMessageFormat("You can't ban a user for more than 7 days!").queue();
            return;
        }
        String reason = args.substring(Integer.toString(days).length());
        if (reason.startsWith(" ")) {
            reason = reason.substring(1);
        }
        event.getGuild().ban(member, days).reason(reason).queue(
                any -> event.getChannel().sendMessageFormat("Banned %s!", member.getAsMention()).queue(),
                error -> event.getChannel().sendMessageFormat("Cannot ban a user with error: `%s!`", error.getMessage()).queue()
        );
    }
}
