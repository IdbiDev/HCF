package me.idbi.hcf.Discord;

import club.minnced.discord.webhook.WebhookClientBuilder;
import me.idbi.hcf.CustomFiles.DiscordFile;

public class SetupBot {

    public static WebhookClientBuilder builder;

    public static void setup() {
        try {
            if(!DiscordFile.getDiscord().getBoolean("bot"))
                return;

            builder = new WebhookClientBuilder(DiscordFile.getDiscord().getString("webhook_url"));
            //builder.buildJDA().send(Embeds.kill(Bukkit.getPlayer("kbalu"), Bukkit.getPlayer("adbi20014")).build());
//
//            JDA jda = JDABuilder.createDefault(DiscordFile.getDiscord().getString("token"))
//                    .addEventListeners()
//                    .build();
//
//            jda.awaitReady();
//
//            mainGuild = jda.getGuildById(DiscordFile.getDiscord().getString("guild-id"));
//            if(mainGuild == null) {
//                Bukkit.getLogger().severe("Can't find guild!");
//                jda.shutdown();
//                return;
//            }
//            logChannel = mainGuild.getTextChannelById(DiscordFile.getDiscord().getString("log-channel-id"));
//            if(logChannel == null) {
//                mainGuild.createTextChannel("hcf-log").queue(textChannel -> {
//                    DiscordFile.getDiscord().set("log-channel-id", textChannel.getId());
//                    DiscordFile.saveDiscord();
//                });
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
