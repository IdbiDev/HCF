package me.idbi.hcf.Discord;

import me.idbi.hcf.CustomFiles.DiscordFile;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

public class SetupBot {

    public static Guild mainGuild;
    public static TextChannel logChannel;

    public static void setup() {
        try {
            if(!DiscordFile.getDiscord().getBoolean("bot"))
                return;

            JDA jda = JDABuilder.createDefault(DiscordFile.getDiscord().getString("token"))
                    .addEventListeners()
                    .build();

            jda.awaitReady();

            mainGuild = jda.getGuildById(DiscordFile.getDiscord().getString("guild-id"));
            if(mainGuild == null) {
                Bukkit.getLogger().severe("Can't find guild!");
                jda.shutdown();
                return;
            }
            logChannel = mainGuild.getTextChannelById(DiscordFile.getDiscord().getString("log-channel-id"));
            if(logChannel == null) {
                mainGuild.createTextChannel("hcf-log").queue(textChannel -> {
                    DiscordFile.getDiscord().set("log-channel-id", textChannel.getId());
                    DiscordFile.saveDiscord();
                });
            }
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
