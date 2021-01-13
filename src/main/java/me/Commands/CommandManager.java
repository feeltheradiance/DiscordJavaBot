package me.Commands;

import me.Commands.AdministrationCommands.*;
import me.Commands.MusicCommands.*;
import me.Commands.RegularCommands.HelloCommand;
import me.Commands.RegularCommands.HelpCommand;
import me.JavaBotConfig;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class CommandManager {
    private static final HashMap<String, Command> commands = new LinkedHashMap<>();
    private static final Set<String> commandTypes = new LinkedHashSet<>();

    public CommandManager() {
        addCommand(new BanCommand());
        addCommand(new KickCommand());
        addCommand(new MuteCommand());
        addCommand(new UnbanCommand());
        addCommand(new UnmuteCommand());

        addCommand(new LyricsCommand());
        addCommand(new PauseCommand());
        addCommand(new PlayCommand());
        addCommand(new PlayingCommand());
        addCommand(new PlaylistCommand());
        addCommand(new ResumeCommand());
        addCommand(new SkipCommand());
        addCommand(new StopCommand());
        addCommand(new VolumeCommand());

        addCommand(new HelloCommand());
        addCommand(new HelpCommand(this));
    }

    public void handleCommand(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()) {
            return;
        }
        String command = event.getMessage().getContentRaw().substring(JavaBotConfig.get("PREFIX").length()).split("\\s+")[0], args;
        if (event.getMessage().getContentRaw().contains(" ")) {
            args = event.getMessage().getContentRaw().substring((JavaBotConfig.get("PREFIX") + command).length() + 1);
        } else {
            args = "";
        }
        if (getCommands().contains(command)) {
            if (!isValidCommand(event, command)) {
                return;
            }
            commands.get(command).executeCommand(event, args);
        } else {
            event.getChannel().sendMessageFormat("There is no such a command!\nUse `%shelp` to get more information!",
                    JavaBotConfig.get("PREFIX")).queue();
        }
    }

    public Set<String> getCommands() {
        return commands.keySet();
    }

    public Set<String> getCommandTypes() {
        return commandTypes;
    }

    public Command getCommand(String command) {
        if (getCommands().contains(command)) {
            return commands.get(command);
        } else {
            return null;
        }
    }

    private void addCommand(Command command) {
        if (commands.containsKey(command.getName())) {
            throw new IllegalArgumentException("A command with this name already exists!");
        }
        commands.put(command.getName(), command);
        commandTypes.add(command.getType());
    }

    private boolean isValidCommand(GuildMessageReceivedEvent event, String command){
        switch (getCommand(command).getType()) {
            case "Music":
                if (!event.getChannel().getName().equalsIgnoreCase("music")) {
                    if (event.getGuild().getTextChannelsByName("music", true).isEmpty()) {
                        event.getChannel().sendMessageFormat("You need to create **music** text channel and then use this command in it!").queue();
                    } else {
                        event.getChannel().sendMessageFormat("You had to be in a **music** text channel!").queue();
                    }
                    return false;
                }
                if (!event.getMember().getVoiceState().inVoiceChannel()) {
                    event.getChannel().sendMessageFormat("You need to be in a voice channel to do this right now!").queue();
                    return false;
                }
                if (!event.getGuild().getAudioManager().isConnected()) {
                    if (event.getMessage().getContentRaw().split("\\s+")[0].equals(JavaBotConfig.get("PREFIX") + "play")) {
                        return true;
                    }
                    event.getChannel().sendMessageFormat("There is nothing playing right now, so you cant do this!").queue();
                    return false;
                }
                if (!event.getMember().getVoiceState().getChannel().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                    event.getChannel().sendMessageFormat("You need to be in the **%s** voice channel to do this right now!",
                            event.getGuild().getAudioManager().getConnectedChannel().getName()).queue();
                    return false;
                }
                return true;
            case "Administration":
                if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR, Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)) {
                    event.getChannel().sendMessageFormat("I don't have the permissions to use this command!").queue();
                    return false;
                }
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR, Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)) {
                    event.getChannel().sendMessageFormat("You don't have the permissions to use this command!").queue();
                    return false;
                }
                if (!event.getMessage().getMentionedMembers().isEmpty() && event.getMember().equals(event.getMessage().getMentionedMembers().get(0))) {
                    event.getChannel().sendMessageFormat("You cannot use this command on yourself!").queue();
                    return false;
                }
                if (!command.equals("unban") && event.getMessage().getMentionedMembers().isEmpty()) {
                    event.getChannel().sendMessageFormat("You might have entered something wrong!").queue();
                    return false;
                }
                return true;
            default:
                return true;
        }
    }
}
