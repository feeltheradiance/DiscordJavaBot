package me.Commands.RegularCommands;

import me.Commands.Command;
import me.Commands.CommandManager;
import me.JavaBotConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class HelpCommand extends Command {
    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows commands description.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "help [command]`";
    }

    @Override
    public String getType() {
        return "Regular";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event, String args) {
        if (args.isEmpty()) {
            EmbedBuilder embedMessage = new EmbedBuilder()
                    .setTitle("Command list")
                    .setColor(Color.GREEN)
                    .setDescription("Use `" + JavaBotConfig.get("PREFIX") + "help command` to see more info about command.");
            for (String commandType : commandManager.getCommandTypes()) {
                StringBuilder commands = new StringBuilder();
                for (String command : commandManager.getCommands()) {
                    if (commandManager.getCommand(command).getType().equals(commandType)) {
                        commands.append("`").append(JavaBotConfig.get("PREFIX")).append(command).append("`").append(", ");
                    }
                }
                commands.delete(commands.length() - 2, commands.length()).append("\n\n");
                embedMessage.addField(commandType, commands.toString(), false);
            }
            event.getChannel().sendMessage(embedMessage.build()).queue();
        } else {
            if (commandManager.getCommands().contains(args)) {
                event.getChannel().sendMessageFormat("%s", commandManager.getCommand(args).getDescription()).queue();
            } else {
                event.getChannel().sendMessageFormat("Nothing found by %s.", args).queue();
            }
        }
    }
}
