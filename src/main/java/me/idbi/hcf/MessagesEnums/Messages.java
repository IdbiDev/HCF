package me.idbi.hcf.MessagesEnums;

import me.idbi.hcf.CustomFiles.ConfigManager;
import me.idbi.hcf.CustomFiles.MessagesFile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum Messages {

    PREFIX("&8[&2HCF&a+&8] &7>"),
    RELOAD("&aSuccessfully configuration files reloaded!"),

    // Errors
    NO_PERMISSION("%prefix% &cYou don't have permission!"),
    NO_PERMISSION_IN_FACTION("%prefix% &cYou need a higher rank to use this command!"),
    NOT_ENOUGH_SLOT("%prefix% &cYou don't have enough space!"),
    NOT_FOUND_PLAYER("%prefix% &cCan't find this player"),
    NOT_FOUND_FACTION("%prefix% &cCan't find this faction"),

    UNKNOWN_COMMAND("%prefix% &cUnknown command. Use &c&o/faction &ccommand to see the commands!"),
    TOO_MANY_ARGS("%prefix% &cToo many arguments!"),
    NOT_A_NUMBER("%prefix% &cPlease type a valid number"),

    CANT_DEMOTE("%prefix% &cYou can't demote &c&o%player%"),
    CANT_PROMOTE("%prefix% &cYou can't promote &c&o%player%"),

    NOT_FILE("%prefix% &cCan't find this file!"),

    DONT_HAVE_ITEM("%prefix% &cYou don't have enough item in your inventory!"),

    // Errors - faction
    EXISTS_FACTION_NAME("%prefix% &cThis faction name already exists!"),
    YOU_ALREADY_IN_FACTION("%prefix% &cYou are already in faction!"),
    NOT_IN_FACTION("%prefix% &cYou are not in a faction!"),
    PLAYER_IN_FACTION("%prefix% &cThis player already in a faction!"),

    ALREADY_INVITED("&cThis player is already invited"),
    NOT_INVITED("%prefix% &cYou are not invited to this faction!"),

    NO_FACTION_EXISTS("%prefix% &cThis faction does not exists!"),

    // Commands
    FACTION_CREATON("%prefix% &aSuccessfully faction creation!"),//ok
    FACTION_LEAVE("%prefix% &eYou leaved from faction!"),
    FACTION_CLAIM("%prefix% nem tudom, hogy mi az a claim! :)"),

    //Claim actions
    FACTION_CLAIM_DECLINE("%prefix% &cYou successfully rejected the claim."),
    FACTION_CLAIM_ACCEPT("%prefix% &aYou successfully claimed"),
    FACTION_CLAIM_INVALID_ZONE("%prefix% &cInvalid claiming zone!"),

    // Commands - invite
    INVITED_BY("&eYou invited by &6&o%executor% &eto &6&o%faction_name%&e!"),
    INVITED_PLAYER("&eYou invited &6&o%player% &eto the faction!"),
    FACTION_INVITE_BROADCAST("&6&o%executor% &einvited &6&o%player% &eto the faction!"),

    // Faction join and quit
    BC_JOIN_MESSAGE("&6&o%player% &ejoined to the faction!"),
    BC_LEAVE_MESSAGE("&6&o%player% &eleaved from the faction!"),
    JOIN_MESSAGE("&eYou joined to the faction!"),
    LEAVE_MESSAGE("&aYou leaved from the faction!"),

    // Faction bank
    FACTION_BANK_DEPOSIT("%prefix% &aSuccessfully deposited &2&o$%amount% &ato your faction bank."),
    FACTION_BANK_WITHDRAW("%prefix% &aSuccessfully withdrew &2&o$%amount% &afrom your faction bank."),
    FACTION_BANK_NOT_ENOUGH("%prefix% &cYour faction doesn't have enough money!"),
    FACTION_BANK_NUMBER_ERROR("%prefix% &cPlease enter a valid number!"),
    NOT_ENOUGH_MONEY("%prefix% &cYou don't have enough money!"),

    // Server broadcasts
    FACTION_CREATED("&6&o%faction_name% &esuccessfully created by &6&o%player%&e!"), // Sexy lesz :D azám :D
    DEATH_MESSAGE("&a&o%victim% &ekilled by &a&o%killer%&e."),

    // Zones interact You've exited
    LEAVE_ZONE("§eYou've exited from §6§o%zone_name%"),
    ENTERED_ZONE("§eYou entered to §6§o%zone_name%"),

    // Faction messages
    KICK_MESSAGE("&a%executor% &ekicked &a%player%&e from the faction!"),
    WITHDRAW_MESSAGE("%prefix% &eYou withdrew &6&o$%amount% &efrom faction bank!"),
    DEPOSIT_MESSAGE("%prefix% &eYou deposited &6&o$%amount% &eto faction bank!"),
    EXECUTOR_INVITE_MESSAGE("&eYou invited &6&o%player% &eto your faction!"),
    INVITED_INVITE_MESSAGE("&eYou invited to &6&o%faction_name% &efaction by &6&o%executor%&e!"),

    JOIN_FACTION_BC("&8[&a+&8] &7&l» &6&o%player%"),
    LEAVE_FACTION_BC("&8[&c-&8] &7&l» &6&o%player%"),

    // Faction home
    SETHOME_MESSAGE("&aSuccessfully set home of your faction to your location!"),
    TELEPORT_TO_HOME("&eTeleportation will commenence in &6&o%time% seconds&e. &cDo not move!"),
    SUCCESSFULLY_TELEPORT("&eYou are successfully teleported to your faction's home!"),
    TELEPORT_CANCEL("&cYou moved, teleportation cancelled!"),
    DOESNT_HOME("&cYour faction doesn't have home!"),

    // Faction chat
    FACTION_CHAT("&7[&e%faction_name%&7] &6%player%&f: &a%message%"),

    // Faction permissions messages
    PROMOTE_MESSAGE("&6&o%executor% &epromoted you to &6&o%rank%&e!"),
    DEMOTE_MESSAGE("&6&o%executor% &edemoted you to &6&o%rank%&e!"),

    EXECUTOR_PROMOTE_MESSAGE("&eYou promoted &6&o%player% &eto &6&o%rank%&e!"),
    EXECUTOR_DEMOTE_MESSAGE("&eYou demoted &6&o%player% &eto &6&o%rank%&e!"),

    FACTION_CREATE_RANK("%prefix% &aSuccessfully rank creation!"),
    FACTION_RANK_EXISTS("%prefix% &cThis rank is exists!"),

    // Console error
    NOT_VALID_BLACKLISTED_BLOCKS("§cInvalid material type in config. Excepted material: %material%"),
    SIGN_SHOP_MATERIAL("§cInvalid material in the sign shop."),

    // Designs
    STATUS_DESIGN_ONLINE("&aOnline"),
    STATUS_DESIGN_OFFLINE("&cOffline"),
    CATEGORY_DESIGN("&e%category%:"),
    MEMBER_LIST_DESIGN_ONLINE("&7▬ &a%member% &f[&7%member_kill%&f]"),
    MEMBER_LIST_DESIGN_OFFLINE("&7▬ &7%member% &f[&7%member_kill%&f]"),
    
    // Sign Shop message
    SIGN_SHOP_BOUGHT("%prefix% &aYou bought &2&o%amount%x %item% &afor &2&o$%price%&a."),
    SIGN_SHOP_SOLD("%prefix% &aYou sold &2&o%amount%x %item% &afor &2&o$%price%&a."),
    
    // Admin Duty
    ADMIN_DUTY_ON("%prefix% &aDuty mode: &2&oon"),
    ADMIN_DUTY_OFF("%prefix% &cDuty mode: &4&ooff"),
    FREEZE_PLAYER_ON("%prefix% &aYou froze by &2&o%executor%"),
    FREEZE_PLAYER_OFF("%prefix% &cYou unfreeze by &4&o%executor%"),
    FREEZE_EXECUTOR_ON("%prefix% &aYou froze &2&o%player%"),
    FREEZE_EXECUTOR_OFF("%prefix% &cYou unfreeze &4&o%player%"),

    FACTION_ADMIN_WITHDRAW_BC("%prefix% &c&l%executor% &cdeposited &c&l%amount% &cto faction's bank!"),
    FACTION_ADMIN_DEPOSIT_BC("%prefix% &c&l%executor% &cwithdrew &c&l%amount% &cfrom faction's bank!"),

    SET_FACTION_NAME("%prefix% &eFaction's name changed by &6&o%executor% &eto &6&o%faction_name%&e!"),

    GIVE_MONEY("%prefix% &eYou got &6&o%amount% &efrom &6&o%executor%&e!"),
    TAKE_MONEY("%prefix% &6&o%executor%&e took &6&o%amount% &efrom you!"),

    SET_FACTION_LEADER_BY_ADMIN("%prefix% &6&o%executor%&e set &6&o%player% &eto faction leader!"),
    DELETE_FACTION_BY_ADMIN("%prefix% &6&o%executor%&e deleted the &6&o%faction_name% &efaction!");

    private String msg, defaultMsg;
    private String message;

    Messages(String msg) {
        this.msg = msg;
        this.defaultMsg = msg;
        this.message = msg;
    }

    public Messages getMessage() {
        message = ChatColor.translateAlternateColorCodes('&', msg
                .replace("%newline%", "\n")
                .replace("%prefix%", Messages.PREFIX.msg));
        return this;
    }

    public Messages getDefaultMessage() {
        defaultMsg = ChatColor.translateAlternateColorCodes('&', defaultMsg
                .replace("%newline%", "\n")
                .replace("%prefix%", Messages.PREFIX.msg));
        return this;
    }

    public String queue() {
        if (message.equalsIgnoreCase(defaultMsg)) {
            msg = defaultMsg;
            message = defaultMsg;
            return ChatColor.translateAlternateColorCodes('&', msg
                    .replace("%newline%", "\n")
                    .replace("%prefix%", Messages.PREFIX.msg));
        }
        msg = defaultMsg;
        return ChatColor.translateAlternateColorCodes('&', message
                .replace("%newline%", "\n")
                .replace("%prefix%", Messages.PREFIX.msg));
    }

    public Messages repDeath(Player victim, Player killer) {
        message = msg.replace("%victim%", victim.getName()).replace("%killer%", killer.getName());
        this.msg = message;
        return this;
    }

    public Messages repPlayer(Player p) {
        message = msg.replace("%player%", p.getName());
        this.msg = message;
        return this;
    }

    public Messages setZone(String zoneName) {
        message = msg.replace("%zone_name%", zoneName);
        this.msg = message;
        return this;
    }

    public Messages setFaction(String factionName) {
        message = msg.replace(msg, msg.replace("%faction_name%", factionName));
        this.msg = message;
        return this;
    }

    public Messages setAmount(String amount) {
        message = msg.replace("%amount%", amount);
        this.msg = message;
        return this;
    }

    public Messages setMessage(Player p, String message) {
        this.message = msg.replace("%message%", message).replace("%player%", p.getName());
        this.msg = message;
        return this;
    }


    public Messages setRank(Player p, String rank) {
        message = msg.replace("%rank%", rank).replace("%player%", p.getName());
        this.msg = message;
        return this;
    }

    public Messages repExecutor(Player executor) {
        message = msg.replace("%executor%", executor.getName());
        this.msg = message;
        return this;
    }

    public Messages setPrice(int price) {
        message = msg.replace("%price%", String.valueOf(price));
        this.msg = message;
        return this;
    }

    public Messages setItem(ItemStack item) {
        message = msg.replace("%item%", item.getType().name().substring(0, 1) + item.getType().name().substring(1).toLowerCase());
        this.msg = message;
        return this;
    }

    public Messages repPlayerExecutor(Player p, Player executor) {
        message = msg.replace("%executor%", executor.getName()).replace("%player%", p.getName());
        this.msg = message;
        return this;
    }

    public void load() {
        FileConfiguration msgs = ConfigManager.getManager().getMessages();

        if (msgs == null) return;

        if (msgs.getString(this.toString()) == null) {
            msgs.set(this.toString(), msg);
            save();
            return;
        }

        msg = msgs.getString(this.toString());
    }

    public void save() {
        FileConfiguration msgs = MessagesFile.getMessages();

        msgs.set(this.toString(), msg);

        MessagesFile.saveMessages();
    }
}
