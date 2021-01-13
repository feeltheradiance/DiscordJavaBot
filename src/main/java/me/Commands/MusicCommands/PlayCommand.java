package me.Commands.MusicCommands;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import me.Commands.Command;
import me.JavaBotConfig;
import me.MusicUtils.MusicManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PlayCommand extends Command {
    private final YouTube youTube;

    public PlayCommand() {
        YouTube tryYouTube = null;
        try {
            tryYouTube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName("JavaBot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.youTube = tryYouTube;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a track from url or search.\n" +
                "Usage: `" + JavaBotConfig.get("PREFIX") + "play url or search`";
    }

    @Override
    public String getType() {
        return "Music";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event, String args) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageFormat("You must enter url or search!").queue();
            return;
        }
        if (!args.startsWith("https://")) {
            String foundYouTube = searchYouTube(args);
            if (foundYouTube != null) {
                MusicManager.getMusicManager().loadAndPlay(event.getChannel(), event.getMember().getVoiceState().getChannel(), foundYouTube);
            } else {
                event.getChannel().sendMessageFormat("Nothing found by %s", args).queue();
            }
        } else {
            MusicManager.getMusicManager().loadAndPlay(event.getChannel(), event.getMember().getVoiceState().getChannel(), args);
        }
    }

    private String searchYouTube(String args) {
        try {
            List<SearchResult> results = youTube.search().list("id,snippet")
                    .setQ(args)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(JavaBotConfig.get("YOUTUBE"))
                    .execute()
                    .getItems();
            if (!results.isEmpty()) {
                return "https://www.youtube.com/watch?v=" + results.get(0).getId().getVideoId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}