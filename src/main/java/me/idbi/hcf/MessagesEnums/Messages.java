package me.idbi.hcf.MessagesEnums;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.CustomFiles.ConfigManager;
import me.idbi.hcf.CustomFiles.MessagesFile;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum Messages {

    PREFIX("&8[&2HCF&a+&8] &7>"),
    PREFIX_CMD("&r[&2HCF&a+&r] &r"),
    RELOAD("&aSuccessfully configuration files reloaded!"),

    CHAT_PREFIX_FACTION("&8[&6%faction_name%&8] &e%player% &f> &7%message%"),
    CHAT_PREFIX_WITHOUT_FACTION("&e%player% &f> &7%message%"),
    // Errors
    NO_PERMISSION("%prefix% &cYou don't have permission!"),
    NO_PERMISSION_IN_FACTION("%prefix% &cYou need a higher rank to use this command!"),
    YOU_CANT_DO("%prefix% &eYou can't do this &c&o%faction_name% &eplace!"),
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
    FACTION_CREATON("%prefix% &2&o%faction_name% &awas created by &2&o%player%&a!"),
    FACTION_DISBAND("%prefix% &2&o%faction_name% &awas disbanded by &2&o%player%&a!"),
    FACTION_LEAVE("%prefix% &eYou leaved from faction!"),
    FACTION_CLAIM("%prefix% nem tudom, hogy mi az a claim! :)"),
    FACTION_CLAIM_PRICE("%prefix% &aPrice: &2&o$%price% &afor &2&o%blocks%"),

    //Claim actions
    FACTION_CLAIM_DECLINE("%prefix% &cYou successfully rejected the claim."),
    FACTION_CLAIM_ACCEPT("%prefix% &aYou successfully claimed"),
    FACTION_CLAIM_INVALID_ZONE("%prefix% &cInvalid claiming zone!"),
    FACTION_CLAIM_OVERLAP("%prefix% &cYour claim can't overlap an existing claim!"),
    FACTION_CLAIM_TOO_SMALL("%prefix% &cYour claim is too small! (Min size: 4x4)"),
    FACTION_CLAIM_OVERLAP_PLUS_ONE("%prefix% &cYour claim can't overlap an existing claim! Skip a block between the two claims"),

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
    //PVP_QUIT_MESSAGE("&a&o%player% leaved "),

    // Zones interact You've exited
    LEAVE_ZONE("§eYou've exited from §6§o%zone_name%"),
    ENTERED_ZONE("§eYou entered to §6§o%zone_name%"),

    // Faction messages
    KICK_MESSAGE("&a%executor% &ekicked &a%player%&e from the faction!"),
    WITHDRAW_MESSAGE("%prefix% &eYou withdrew &6&o$%amount% &efrom faction bank!"),
    DEPOSIT_MESSAGE("%prefix% &eYou deposited &6&o$%amount% &eto faction bank!"),
    EXECUTOR_INVITE_MESSAGE("&eYou invited &6&o%player% &eto your faction!"),
    INVITED_INVITE_MESSAGE("&eYou invited to &6&o%faction_name% &efaction by &6&o%executor%&e!"),
    LEADER_LEAVING_FACTION("&cYou can't leave this faction, bc you are the leader! Use &e&l/f disband &r&cinstead!"),
    NOT_LEADER("&cYou are not the faction leader!"),
    JOIN_FACTION_BC("&8[&a+&8] &7&l» &6&o%player%"),
    LEAVE_FACTION_BC("&8[&c-&8] &7&l» &6&o%player%"),
    FACTION_PLAYER_POSITION("&7-> &6&o%player%'s &eposition: &6&o%location_x%&e, &6&o%location_y%&e, &6&o%location_z%"),

    // Faction home
    SETHOME_MESSAGE("&aSuccessfully set home of your faction to your location!"),
    TELEPORT_TO_HOME("&eTeleportation will commenence in &6&o%time% seconds&e. &cDo not move!"),
    SUCCESSFULLY_TELEPORT("&eYou are successfully teleported to your faction's home!"),
    TELEPORT_CANCEL("&cYou moved, teleportation cancelled!"),
    DOESNT_HOME("&cYour faction doesn't have home!"),


    // Faction chat
    FACTION_CHAT("&7[&e%faction_name%&7] [&e%rank%&7] &6&o%player%&7: &e%message%"),
    FACTION_CHAT_TOGGLE_ON("%prefix% &aYou are now in &2&ofaction chat&a!"),
    FACTION_CHAT_TOGGLE_OFF("%prefix% &cYou are now in &c&oALL chat&c!"),

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

    SPAWN_CLAIM_SUCCESS("%prefix% &bYou successfully claimed the spawn!"),

    FACTION_ADMIN_WITHDRAW_BC("%prefix% &c&l%executor% &cdeposited &c&l%amount% &cto faction's bank!"),
    FACTION_ADMIN_DEPOSIT_BC("%prefix% &c&l%executor% &cwithdrew &c&l%amount% &cfrom faction's bank!"),
    ADMIN_SET_PLAYERFACTION("%prefix%&1 You successfully put &6&o%player% in the %faction_name% faction"),
    SET_FACTION_NAME("%prefix% &eFaction's name changed by &6&o%executor% &eto &6&o%faction_name%&e!"),

    ADMIN_SET_FACTION_NAME("%prefix% &1You have successfully changed the name of the faction to &e%faction_name%"),
    GIVE_MONEY("%prefix% &eYou got &6&o%amount% &efrom &6&o%executor%&e!"),
    TAKE_MONEY("%prefix% &6&o%executor%&e took &6&o%amount% &efrom you!"),

    SET_FACTION_LEADER_BY_ADMIN("%prefix% &6&o%executor%&e set &6&o%player% &eto faction leader!"),
    DELETE_FACTION_BY_ADMIN("%prefix% &6&o%executor%&e deleted the &6&o%faction_name% &efaction!"),

    CANT_DAMAGE_ADMIN("%prefix% &cYou can't attack while you are in duty mode"),
    CANT_DAMAGE_PROTECTED_AREA("%prefix% &cYou can't attack while you are in a protected area!"),
    COMBAT_MESSAGE("%prefix% &7You are now in combat &a[%sec% sec]"),
    TEAMMATE_DAMAGE("%prefix% &cYou can't damage your teammate!"),

    DEATHBAN_KICK("%prefix%\n&cYou are deathbanned for %sec%"),

    NO_DEATHBAN_KICK("%prefix%\n&cThe server disabled the deathban!"),

    KILL_MESSAGE_BROADCAST("&4&l%killer%&f[%killer_kills%] slained &4&l%victim%&f[%victim_kills%] using [&b&o%killer_weapon%&f]"),

    KILL_MESSAGE_BROADCAST_WITHOUT_VICTIM("&4&l%victim%&f[%victim_kills%] died"),

    MAX_MEMBERS_REACHED("&4&lYou can't invite more people to the faction because it's full!"),

    ERROR_WHILE_EXECUTING("%prefix% &4&lAn error occurred while running the command! Please check the LOG file, and report it!"),
    KILL_MESSAGE_FACTION("&4&l%killer%&f killed &4&l%victim%"),

    ENCHANT_CONFIRM_BUTTON("&aConfirm"),
    ENCHANT_CANCEL_BUTTON("&cCancel"),
    ENCHANT_NOT_ENOUGH_XP("%prefix% &cYou don't have enough exp!"),
    CONFIRM_BUTTON_LORE("&6Costs: &e&n%xp_level% level"),

    BARD_DONT_HAVE_ENOUGH_ENERGY("%prefix% &cYou don't have enough energy to activate this. &4&l[Required %amount%]"),

    BARD_USED_POWERUP("%prefix% &6You used &b%effect% &6for &b&o%amount%&r&6. [&aBuffed teammates: &b&o%count%&r&6]"),

    //STUCK stepbro

    STUCK_STARTED("%prefix% &aStuck timer started! You are getting teleported to a safe zone in &o&e%amount%&r&a seconds! Don't move!"),
    STUCK_FINISHED("%prefix%  &bYou are successfully teleported to a safe zone!"),
    STUCK_INTERRUPTED("%prefix% &cYou interrupted the stuck timer!"),

    CLAIM_POS_START("%prefix% &bClaim start pos: &a&n%loc%"),
    CLAIM_POS_END("%prefix% &bClaim end pos: &a&n%loc%"),

    // KOTH SOTW EOTW
    ENABLE_EOTW("%prefix% &aEOTW started!"),
    // koth
    UPDATED_KOTH_REWARDS("%prefix% &aSuccessfully saved rewards!"),
    CLAIM_KOTH_REWARDS("%prefix% &aYou claimed KOTH rewards!"),
    KOTH_INVALID_NAME("%prefix% &cThis koth doesn't exits!"),
    KOTH_SUCESS_CREATE("%prefix% &aSuccessfully created the &6&o%faction_name%&a koth!"),
    KOTH_CREATED("%prefix% &aYou have successfully created a koth named &6&o%faction_name%&a!"),
    KOTH_CAPTURING_STARTED("%prefix% &aSomeone started to occupy the &6&o%faction_name%&a koth!"),
    KOTH_CAPTURING_ENDED("%prefix% &aThe occupation of koth &6&o%faction_name%&a was abandoned!"),
    KOTH_CAPTURE_TIMER("%prefix% &aSomeone is capturing kitty koth! (%format_time%)"),
    WARZONE_NO_PERMISSION("%prefix% &eYou can't do this in the warzone!"),
    CANT_TELEPORT_TO_SAFEZONE("%prefix% &cYou cant teleport to a protected zone while you are in pvp tag");


    private String msg, defaultMsg;
    private String message;
    private String prefix;

    Messages(String msg) {
        this.msg = msg;
        this.defaultMsg = msg;
        this.message = msg;
    }

    public Messages getMessage() {
        message = ChatColor.translateAlternateColorCodes('&', msg
                .replace("%newline%", "\n")
                .replace("%prefix%",  ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.msg)));
        return this;
    }

    public Messages setMessage(String message) {
        this.message = msg.replace("%message%", message);
        this.msg = this.message;
        return this;
    }

    private String getPrefix() {
        //return ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.);
        return "";
    }

    public Messages getDefaultMessage() {
        defaultMsg = ChatColor.translateAlternateColorCodes('&', defaultMsg
                .replace("%newline%", "\n")
                .replace("%prefix%",  ChatColor.translateAlternateColorCodes('&', prefix)));
        return this;
    }

    public String queue() {
        message = msg.replace("%prefix%", ChatColor.translateAlternateColorCodes('&', this.prefix));
        load();
        return ChatColor.translateAlternateColorCodes('&', message
                .replace("%newline%", "\n")
        );
    }

    public Messages repDeath(Player victim, Player killer) {
        message = msg.replace("%victim%", victim.getName()).replace("%killer%", killer.getName());
        this.msg = message;
        return this;
    }
    public Messages repBardEffects(Player bard, String effect,String members) {
        message = msg.replace("%bard%", bard.getName()).replace("%effect%", effect).replace("%count%",members);
        this.msg = message;
        return this;
    }
    public Messages repLoc(int x, int z) {
        message = msg.replace("%loc%", "X:"+x + " Y:"+z);
        this.msg = message;
        return this;
    }
    public Messages repDeathWithoutKiller(Player victim) {
        String victim_kills = playertools.getMetadata(victim,"kills");
        message = msg.replace("%victim%", victim.getName())
                .replace("%victim_kills%",victim_kills);
        this.msg = message;
        return this;
    }
    public Messages repDeathWithKills(Player victim, Player killer) {
        String weaponName = (killer.getItemInHand().hasItemMeta()) ?
                (killer.getItemInHand().getItemMeta().hasDisplayName()
                        ? killer.getItemInHand().getItemMeta().getDisplayName()
                        : killer.getItemInHand().getType().name().toLowerCase())
                : killer.getItemInHand().getType().name().toLowerCase();
        String killer_kills = playertools.getMetadata(killer,"kills");
        String victim_kills = playertools.getMetadata(victim,"kills");
        message = msg.replace("%victim%", victim.getName())
                .replace("%killer%", killer.getName())
                .replace("%killer_kills%",killer_kills)
                .replace("%victim_kills%",victim_kills)
                .replace("%killer_weapon%", weaponName);
        this.msg = message;
        return this;
    }

    public Messages repPlayer(Player p) {
        message = msg.replace("%player%", p.getName());
        this.msg = message;
        return this;
    }
    public Messages setDisplayName(Player p) {
        if(p.getDisplayName() == null) {
            message = msg.replace("%player_displayname%", p.getName());
        } else {
            message = msg.replace("%player_displayname%", p.getDisplayName());
        }
        this.msg = message;
        return this;
    }
    public Messages repTime_formatted(int seconds) {
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        message = msg.replace("%format_time%", String.format("%02d:%02d",MM,SS));
        this.msg = message;
        return this;
    }
    public Messages repDeathTime() {
        message = msg.replace("%player%",String.valueOf(Integer.parseInt(ConfigLibrary.Death_time_seconds.getValue()) / 60));
        this.msg = message;
        return this;
    }

    public Messages setZone(String zoneName) {
        message = msg.replace("%zone_name%", zoneName);
        this.msg = message;
        return this;
    }

    public Messages repCoords(int x, int y, int z) {
        message = msg.replace(msg, msg
                .replace("%location_x%", x + "")
                .replace("%location_y%", y + "")
                .replace("%location_z%", z + ""));
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
        message = msg.replace("%item%", (item.getType().name().charAt(0) + item.getType().name().substring(1).toLowerCase()).replace("_", " "));
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
            this.prefix = msgs.getString("PREFIX");
            return;
        }

        msg = msgs.getString(this.toString());

        this.prefix = msgs.getString("PREFIX");
    }

    public void save() {
        FileConfiguration msgs = MessagesFile.getMessages();

        msgs.set(this.toString(), msg);

        MessagesFile.saveMessages();
    }
}
