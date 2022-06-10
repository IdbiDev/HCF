package me.idbi.hcf.Discord;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Date;

public class Embeds {

    public static EmbedBuilder kill(Player killer, Player victim) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Kill")
                .setDescription("**" + victim.getName() + "** killed by **" + killer.getName() + "**.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder death(Player victim, Player killer, boolean fallDamage) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Death")
                .setDescription(
                        (killer == null
                                ? (fallDamage
                                ? "**" + victim.getName() + "** death by **fall damage**"
                                : "**" + victim.getName() + "** death")
                                : "**" + victim.getName() + "** killed by **" + killer.getName() + "**.")
                )
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_create(Player executor, String factionName) {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Faction create")
                .setDescription("**" + factionName + "** faction created by **" + executor.getName() + "**.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_disband(Player executor, String factionName) {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Faction disband")
                .setDescription("**" + factionName + "** faction disbanded by **" + executor.getName() + "**.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_join(Player joiner, Player inviter, String factionName) {
        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle("Faction join")
                .setDescription("**" + joiner.getName() + "** joined to the **" + factionName + "** faction. Invited by **" + inviter.getName() + "**.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_leave(Player leaver, String factionName) {
        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle("Faction leave")
                .setDescription("**" + leaver.getName() + "** leaved from the **" + factionName + "** faction.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_bank_withdraw(Player executor, int amount) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Faction bank: Withdraw")
                .setDescription("**" + executor.getName() + "** withdrew **$" + amount + "** from the faction's bank.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_bank_deposit(Player executor, int amount) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Faction bank: Deposit")
                .setDescription("**" + executor.getName() + "** deposited **$" + amount + "** to the faction's bank.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder admin_duty(Player admin, boolean mode) {
        return new EmbedBuilder()
                .setColor(Color.cyan)
                .setTitle("Admin Duty")
                .setDescription((mode
                        ? "**" + admin.getName() + "** duty mode on."
                        : "**" + admin.getName() + "** duty mode off."))
                .setTimestamp(new Date().toInstant());
    }


    public static EmbedBuilder faction_rank_create(Player executor, String rankName) {
        return new EmbedBuilder()
                .setColor(Color.white)
                .setTitle("Faction Rank Create")
                .setDescription("**" + executor.getName() + "** created **" + rankName + "**.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_rank_delete(Player executor, String rankName) {
        return new EmbedBuilder()
                .setColor(Color.white)
                .setTitle("Faction Rank Delete")
                .setDescription("**" + executor.getName() + "** deleted **" + rankName + "**.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_rank_setpermission(Player executor, String rankName, String permissionName) {
        return new EmbedBuilder()
                .setColor(Color.magenta)
                .setTitle("Faction Rank Set Permission")
                .setDescription("**" + executor.getName() + "** set **" + permissionName + "** permission to **" + rankName + "** rank.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder faction_rank_removepermission(Player executor, String rankName, String permissionName) {
        return new EmbedBuilder()
                .setColor(Color.magenta)
                .setTitle("Faction Rank Remove Permission")
                .setDescription("**" + executor.getName() + "** removed **" + permissionName + "** permission from **" + rankName + "** rank.")
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder commands(Player executor, String command) {
        return new EmbedBuilder()
                .setColor(Color.magenta)
                .setTitle("Command execute")
                .setDescription("**" + executor.getName() + "** executed **" + command + "**")
                .setTimestamp(new Date().toInstant());
    }
}
