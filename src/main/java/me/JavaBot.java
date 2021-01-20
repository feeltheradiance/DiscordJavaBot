package me;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class JavaBot {
    private static final EnumSet<GatewayIntent> gatewayIntents = EnumSet.of(
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_EMOJIS,
            GatewayIntent.GUILD_INVITES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_MESSAGE_TYPING
    );

    private static final EnumSet<CacheFlag> cacheFlags = EnumSet.of(
            CacheFlag.ACTIVITY,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.EMOTE,
            CacheFlag.MEMBER_OVERRIDES,
            CacheFlag.VOICE_STATE
    );

    public static void main(String[] args) throws LoginException {
        JDABuilder.createDefault(JavaBotConfig.get("TOKEN"))
                .addEventListeners(new JavaBotListener())
                .enableIntents(gatewayIntents)
                .enableCache(cacheFlags)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setActivity(Activity.watching(JavaBotConfig.get("PREFIX") + "help"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }
}
