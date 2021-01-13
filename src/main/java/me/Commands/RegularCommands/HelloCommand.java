package me.Commands.RegularCommands;

import me.Commands.Command;
import me.JavaBotConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelloCommand extends Command {
    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String getDescription() {
        return "Says hello to bot or user if you entered any args.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "hello [@user]`";
    }

    @Override
    public String getType() {
        return "Regular";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event, String args) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageFormat("Hello %s, it's nice to see you!", event.getAuthor()).queue();
        } else {
            if (!event.getMessage().getMentionedMembers().isEmpty()) {
                if (args.split("\\s+").length > event.getMessage().getMentionedMembers().size()) {
                    event.getChannel().sendMessageFormat("You entered something wrong!").queue();
                    return;
                }
                StringBuilder members = new StringBuilder();
                for (Member member : event.getMessage().getMentionedMembers()) {
                    members.append(member.getAsMention()).append(", ");
                }
                event.getChannel().sendMessageFormat("%s said hello to %s!", event.getAuthor(), members.substring(0, members.length() - 2)).queue();
            } else {
                event.getChannel().sendMessageFormat("You need to mention some member instead of: %s!", args).queue();
            }
        }
    }
}
