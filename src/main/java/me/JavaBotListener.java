package me;

import me.Commands.CommandManager;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaBotListener extends ListenerAdapter {
    private static final CommandManager commandManager = new CommandManager();

    public Logger logger = LoggerFactory.getLogger(JavaBotListener.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("{} is ready to work!", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(JavaBotConfig.get("PREFIX"))) {
            commandManager.handleCommand(event);
        }
    }
}