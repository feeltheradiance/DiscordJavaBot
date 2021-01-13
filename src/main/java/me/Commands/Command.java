package me.Commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getType();
    public abstract void executeCommand(GuildMessageReceivedEvent event, String args);
}
