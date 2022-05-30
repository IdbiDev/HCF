package me.idbi.hcf.Discord;

import me.idbi.hcf.CustomFiles.DiscordFile;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;

public class LogLibrary {

    private static TextChannel channel = SetupBot.logChannel;

    public static void sendKill(Player killer, Player victim) {
        if(DiscordFile.getDiscord().getBoolean("kill"))
            channel.sendMessageEmbeds(Embeds.kill(killer, victim).build()).queue();
    }

    public static void sendDeath(Player died, Player killer, boolean fallDamage) {
        if(DiscordFile.getDiscord().getBoolean("death"))
            channel.sendMessageEmbeds(Embeds.death(died, killer, fallDamage).build()).queue();
    }

    public static void sendFactionCreate(Player executor, String factionName) {
        if(DiscordFile.getDiscord().getBoolean("faction_create"))
            channel.sendMessageEmbeds(Embeds.faction_create(executor, factionName).build()).queue();
    }

    public static void sendFactionDisband(Player executor, String factionName) {
        if(DiscordFile.getDiscord().getBoolean("faction_disband"))
            channel.sendMessageEmbeds(Embeds.faction_disband(executor, factionName).build()).queue();
    }

    public static void sendJoin(Player joiner, Player inviter, String factionName) {
        if(DiscordFile.getDiscord().getBoolean("faction_join_leave"))
            channel.sendMessageEmbeds(Embeds.faction_join(joiner, inviter, factionName).build()).queue();
    }

    public static void sendLeave(Player joiner, String factionName) {
        if(DiscordFile.getDiscord().getBoolean("faction_join_leave"))
            channel.sendMessageEmbeds(Embeds.faction_leave(joiner, factionName).build()).queue();
    }

    public static void sendBank_withdraw(Player executor, int amount) {
        if(DiscordFile.getDiscord().getBoolean("faction_bank"))
            channel.sendMessageEmbeds(Embeds.faction_bank_withdraw(executor, amount).build()).queue();
    }

    public static void sendBank_deposit(Player executor, int amount) {
        if(DiscordFile.getDiscord().getBoolean("faction_bank"))
            channel.sendMessageEmbeds(Embeds.faction_bank_deposit(executor, amount).build()).queue();
    }

    public static void sendFaction_createRank(Player executor, String rankName) {
        if(DiscordFile.getDiscord().getBoolean("faction_rank"))
            channel.sendMessageEmbeds(Embeds.faction_rank_create(executor, rankName).build()).queue();
    }

    public static void sendFaction_deleteRank(Player executor, String rankName) {
        if(DiscordFile.getDiscord().getBoolean("faction_rank"))
            channel.sendMessageEmbeds(Embeds.faction_rank_delete(executor, rankName).build()).queue();
    }

    public static void sendRank_setpermission(Player executor, String rankName, String factionName) {
        if(DiscordFile.getDiscord().getBoolean("faction_rank"))
            channel.sendMessageEmbeds(Embeds.faction_rank_setpermission(executor, rankName, factionName).build()).queue();
    }

    public static void sendRank_removepermission(Player executor, String rankName, String factionName) {
        if(DiscordFile.getDiscord().getBoolean("faction_rank"))
            channel.sendMessageEmbeds(Embeds.faction_rank_removepermission(executor, rankName, factionName).build()).queue();
    }

    public static void sendAdminDuty(Player admin, boolean mode) {
        if(DiscordFile.getDiscord().getBoolean("admin_duty"))
            channel.sendMessageEmbeds(Embeds.admin_duty(admin, mode).build()).queue();
    }

    public static void sendCommand(Player p, String command) {
        if(DiscordFile.getDiscord().getBoolean("commands"))
            channel.sendMessageEmbeds(Embeds.commands(p, command).build()).queue();
    }
}
