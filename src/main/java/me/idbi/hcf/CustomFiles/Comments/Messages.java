package me.idbi.hcf.CustomFiles.Comments;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import me.idbi.hcf.CustomFiles.MessagesTool;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public enum Messages {

    prefix("&8[&2HCF&a+&8] &7>"),
    prefix_cmd("&r[&2HCF&a+&r] &r"),
    reload("%prefix% &aSuccessfully reloaded configuration files!"),
    cant_start_eotw("%prefix% &ceotw start failed! (No spawn location defined! Please use &n/a spawnclaim&f&c)"),
    chat_prefix_faction("&8[&6%faction_name%&8] &e%player% &f> &7%message%"),
    chat_prefix_without_faction("&e%player% &f> &7%message%"),
    // Errors
    no_permission("%prefix% &cYou don't have permission!"),
    no_permission_in_faction("%prefix% &cYou need a higher rank to use this command!"),
    you_cant_do("%prefix% &eYou can't do this at &c&o%faction_name%'s &eplace!"),
    not_enough_slot("%prefix% &cYou don't have enough space!"),
    not_found_player("%prefix% &cCan't find this player"),
    not_found_faction("%prefix% &cCan't find this faction"),

    unknown_command("%prefix% &cUnknown command. Use &c&o/faction &ccommand to see the commands!"),
    //TOO_MANY_ARGS("%prefix% &cToo many arguments!"),
    not_a_number("%prefix% &cPlease use a valid number"),

    cant_demote("%prefix% &cYou can't demote &c&o%player%"),
    cant_promote("%prefix% &cYou can't promote &c&o%player%"),

    not_file("%prefix% &cCan't find this file!"),

    dont_have_item("%prefix% &cYou don't have enough items in your inventory!"),

    // Errors - faction
    exists_faction_name("%prefix% &cThis faction name already exists!"),
    you_already_in_faction("%prefix% &cYou are already in faction!"),
    not_in_faction("%prefix% &cYou are not in a faction!"),
    player_in_faction("%prefix% &cThis player is already in a faction!"),

    uninvite_target("%prefix% &2&o%player%&c canceled your invitation!"),
    uninvite_executor("%prefix% &cYou canceled to invite &2&o%player%&c!"),
    already_invited("%prefix% &cThis player is already invited"),
    not_invited("%prefix% &cYou are not invited to this faction!"),

    no_faction_exists("%prefix% &cThis faction does not exists!"),

    already_invited_ally("%prefix% &cThis faction is already invited to your ally."),
    not_invited_ally("%prefix% &cThis faction didnt invited you to their ally."),
    //
    faction_invited_ally("%prefix% &2&o%faction_name% &awas invited to join this faction ally."),
    joined_ally("%prefix% &2&o%faction_name% &anow is your ally!"),

    // Commands
    faction_creation("%prefix% &2&o%faction_name% &awas created by &2&o%player%&a!"),
    faction_disband("%prefix% &2&o%faction_name% &awas disbanded by &2&o%player%&a!"),
    faction_claim_price("%prefix% &aPrice: &2&o$%price% &afor &2&o%blocks%"),

    //Claim actions
    faction_claim_decline("%prefix% &cYou successfully cancelled the claim!"),
    faction_claim_accept("%prefix% &aYou successfully claimed!"),
    faction_claim_invalid_zone("%prefix% &cInvalid claiming zone!"),
    faction_claim_overlap("%prefix% &cYour claim can't overlap an existing claim!"),
    faction_claim_too_small("%prefix% &cyour claim is too small! (Min size: 4x4)"),
    faction_claim_overlap_plus_one("%prefix% &cYour claim can't overlap an existing claim! Leave a block between the two claims"),


    // Commands - invite
    invited_by("%prefix% &6&o%executor% &einvited you to join &6&o%faction_name%&e!"),
    invited_player("%prefix% &eYou invited &6&o%player% &eto the faction!"),
    faction_invite_broadcast("%prefix% &6&o%executor% &einvited &6&o%player% &eto the faction!"),

    // Faction join and quit
    bc_join_message("%prefix% &6&o%player% &ejoined the faction!"),
    bc_leave_message("%prefix% &6&o%player% &eleft the faction!"),
    join_message("%prefix% &eYou joined the faction!"),
    leave_message("%prefix% &aYou left the faction!"),

    // Faction bank
    faction_bank_deposit("%prefix% &aSuccessfully deposited &2&o$%amount% &ain your faction's bank!"),
    faction_bank_withdraw("%prefix% &aSuccessfully withdrew &2&o$%amount% &afrom your faction's bank!"),
    faction_bank_not_enough("%prefix% &cYour faction doesn't have enough money!"),
    faction_bank_number_error("%prefix% &cPlease enter a valid number!"),
    not_enough_money("%prefix% &cYou don't have enough money!"),

    // Server broadcasts
    // FACTION_CREATED("&6&o%faction_name% &esuccessfully created by &6&o%player%&e!"), // Sexy lesz :D azám :D
    // DEATH_MESSAGE("&a&o%victim% &ekilled by &a&o%killer%&e."),
    //PVP_QUIT_MESSAGE("&a&o%player% leaved "),

    // Zones interact You've exited
    leave_zone("§eYou left §6§o%zone_name%"),
    entered_zone("§eYou entered §6§o%zone_name%"),

    // Faction messages
    kick_message("&%prefix% a%executor% &ekicked &a%player%&e from the faction!"),
    /*withdraw_message("%prefix% &eYou withdrew &6&o$%amount% &efrom faction bank!"),
    deposit_message("%prefix% &eYou deposited &6&o$%amount% &eto faction bank!"),
    executor_invite_message("&eYou invited &6&o%player% &eto your faction!"),
    invited_invite_message("&eYou invited to &6&o%faction_name% &efaction by &6&o%executor%&e!"),*/
    leader_leaving_faction("%prefix% &cYou can't leave this faction, because you are the leader! Use &e&l/f disband &r&cinstead!"),
    not_leader("%prefix% &cYou are not the faction leader!"),
    cant_kick_yourself("%prefix% &cYou can't kick yourself!"),
    cant_kick_leader("%prefix% &cYou can't the faction leader!"),
    join_faction_bc("&8[&a+&8] &7&l» &6&o%player%"),
    leave_faction_bc("&8[&c-&8] &7&l» &6&o%player%"),
    faction_player_position("%prefix% &7-> &6&o%player%'s &eposition: &6&o%location_x%&e, &6&o%location_y%&e, &6&o%location_z%"),
    faction_dont_have_claim("%prefix% &4Your faction does not have a claim yet!"),

    // Faction home
    sethome_message("%prefix% &aSuccessfully set home of your faction to your location!"),
    sethome_update_faction("%prefix% &7&l%player% &f&echanged the faction's home location! &g[&6&o%location_x%&e, &6&o%location_y%&e, &6&o%location_z%]"),
    teleport_to_home("&eTeleportation in &6&o%time% seconds&e. &cDo not move!"),
    successfully_teleport("%prefix% &eSuccessfully teleported to your faction's home!"),
    teleport_cancel("%prefix% &cYou moved, teleportation cancelled!"),
    doesnt_home("%prefix% &cYour faction doesn't have a home set!"),


    // Faction chat
    faction_chat("&7[&e%faction_name%&7] [&e%rank%&7] &6&o%player%&7: &e%message%"),
    faction_chat_toggle_on("%prefix% &aYou are now in &2&ofaction chat&a!"),
    faction_chat_toggle_off("%prefix% &cYou are now in &c&oALL chat&c!"),

    // Faction permissions messages
    promote_message("%prefix% &6&o%executor% &epromoted you to &6&o%rank%&e!"),
    demote_message("%prefix% &6&o%executor% &edemoted you to &6&o%rank%&e!"),

    executor_promote_message("%prefix% &eYou promoted &6&o%player% &eto &6&o%rank%&e!"),
    executor_demote_message("%prefix% &eYou demoted &6&o%player% &eto &6&o%rank%&e!"),

    faction_create_rank("%prefix% &aRank successfully created!"),
    faction_rank_exists("%prefix% &cThis rank already exists!"),

    // Console error
    not_valid_blacklisted_blocks("§cInvalid material type in config. Excepted material: %material%"),
    sign_shop_material("§cInvalid material in the sign shop."),

    // Designs
    status_design_online("&aOnline"),
    status_design_offline("&cOffline"),
    category_design("&7&l» &6%category%:"),
    member_list_design_online("&a%member% &f[&7%member_kill%&f]"),
    member_list_design_offline("&7%member% &f[&7%member_kill%&f]"),

    // Sign Shop message
    sign_shop_bought("%prefix% &aYou bought &2&o%amount%x %item% &afor &2&o$%price%&a."),
    sign_shop_sold("%prefix% &aYou sold &2&o%amount%x %item% &afor &2&o$%price%&a."),

    // Admin Duty
    admin_duty_on("%prefix% &aDuty mode: &2&oon"),
    admin_duty_off("%prefix% &cDuty mode: &4&ooff"),
    freeze_player_on("%prefix% &aYou got frozen by &2&o%executor%"),
    freeze_player_off("%prefix% &cYou got unfrozen by &4&o%executor%"),
    freeze_executor_on("%prefix% &aYou froze &2&o%player%"),
    freeze_executor_off("%prefix% &cYou unfroze &4&o%player%"),

    spawn_claim_success("%prefix% &bYou successfully claimed the spawn!"),

    faction_admin_withdraw_bc("%prefix% &c&l%executor% &cdeposited &c&l%amount% &cinto the faction's bank!"),
    faction_admin_deposit_bc("%prefix% &c&l%executor% &cwithdrew &c&l%amount% &cfrom the faction's bank!"),
    admin_set_playerfaction("%prefix%&1 You successfully put &6&o%player% in the %faction_name% faction"),
    set_faction_name("%prefix% &eFaction's name changed by &6&o%executor% &eto &6&o%faction_name%&e!"),
    admin_set_dtr("%prefix% &eYou have successfully set the DTR of &6&o%faction_name%&e faction to &6&o%new_dtr%"),
    admin_set_faction_name("%prefix% &1You have successfully changed the name of the faction to &e%faction_name%"),
    give_money("%prefix% &eYou got &6&o%amount% &efrom &6&o%executor%&e!"),
    take_money("%prefix% &6&o%executor%&e took &6&o%amount% &efrom you!"),

    set_faction_leader_by_admin("%prefix% &6&o%executor%&e set &6&o%player% &eto faction leader!"),
    delete_faction_by_admin("%prefix% &6&o%executor%&e deleted the &6&o%faction_name% &efaction!"),

    cant_damage_admin("%prefix% &cYou can't attack while you are in duty mode"),
    cant_damage_protected_area("%prefix% &cYou can't attack while you are in a protected area!"),
    combat_message("%prefix% &7You are now in combat &a[%sec% sec]"),
    teammate_damage("%prefix% &cYou can't damage your teammate!"),

    deathban_kick("%prefix%\n&cYou are deathbanned for %sec%"),

    no_deathban_kick("%prefix%\n&cThe server disabled the deathban!"),

    kill_message_broadcast("&4&l%killer%&f[%killer_kills%] slained &4&l%victim%&f[%victim_kills%] using [&b&o%killer_weapon%&f]"),

    kill_message_broadcast_without_victim("&4&l%victim%&f[%victim_kills%] died"),

    max_members_reached("%prefix% &4&lYou can't invite more people to the faction because it's full!"),

    error_while_executing("%prefix% &4&lAn error occurred while running the command! Please check the LOG file, and report it!"),
    kill_message_faction("&4&l%killer%&f killed &4&l%victim%"),

    enchant_confirm_button("&aConfirm"),
    enchant_cancel_button("&cCancel"),
    enchant_not_enough_xp("%prefix% &cYou don't have enough exp!"),
    confirm_button_lore("&6Costs: &e&n%xp_level% level"),

    bard_dont_have_enough_energy("%prefix% &cYou don't have enough energy to activate this. &4&l[Required %amount%]"),

    bard_used_powerup("%prefix% &6You used &b%effect% &6for &b&o%amount%&r&6. [&aBuffed teammates: &b&o%count%&r&6]"),

    cant_damage_while_pvptimer("%prefix% &cYou can't attack while you have a PvP timer on you!"),
    cant_damage_while_pvptimer_victim("%prefix% &cThis player has a PvP timer!"),
    stuck_started("%prefix% &aStuck timer started! You are getting teleported to a safe zone in &o&e%amount%&r&a seconds! Don't move!"),
    stuck_finished("%prefix%  &bYou successfully teleported to a safe zone!"),
    stuck_interrupted("%prefix% &cYou interrupted the stuck timer!"),

    claim_pos_start("%prefix% &bClaim start position: &a&n%loc%"),
    claim_pos_end("%prefix% &bClaim end position: &a&n%loc%"),

    // KOTH SOTW EOTW
    enable_eotw("%prefix% &aEOTW started!"),

    // koth
    updated_koth_rewards("%prefix% &aSuccessfully saved rewards!"),
    claim_koth_rewards("%prefix% &aYou claimed KOTH rewards!"),
    koth_invalid_name("%prefix% &cThis KOTH doesn't exits!"),
    koth_sucess_create("%prefix% &aSuccessfully created the &6&o%faction_name%&a koth!"),
    koth_created("%prefix% &aYou have successfully created a KOTH named &6&o%faction_name%&a!"),
    koth_capturing_started("%prefix% &aSomeone started to occupy the &6&o%faction_name%&a KOTH!"),
    koth_capturing_ended("%prefix% &aThe occupation of KOTH &6&o%faction_name%&a was interrupted!"),
    koth_capture_timer("%prefix% &asomeone is capturing &6&o%faction_name%&a (%format_time%)"),

    warzone_no_permission("%prefix% &eYou can't do this in the Warzone!"),

    // GUI
    gui_rank_created("%prefix% &bYou have successfully created rank: &b&o%rank%!"),
    gui_rank_change("%prefix% &bYou have successfully changed the rank of &b&o%player%&b to: &b&o%rank%!"),
    gui_rank_already_have("%prefix% &cThis user already have this rank."),
    gui_rename_text("Enter rank name"),
    gui_rename_faction_text("Enter faction name"),
    gui_invite_player("Enter a player name"),
    gui_create_rank_text("Enter rank name"),
    gui_invalid_type_text("Invalid name!"),
    gui_bad_word("Don't use forbidden words."),
    gui_priority_saved("%prefix% &aYou successfully saved the changes!"),
    rename_faction("%prefix% §aSuccessfully renamed faction "),

    // Hover things
    hover_join("&7Click here to join!"),
    hover_accept("&7Click here to accept!"),

    // Staff messages
    staff_chat("&8[&eStaffChat&8] &b%player%&7: &e%message%"),
    staff_chat_on("%prefix% &aStaff chat enabled!"),
    staff_chat_off("%prefix% &cStaff chat disabled!"),

    vanish_enabled("%prefix% &aVanish enabled!"),
    vanish_disable("%prefix% &cVanish disabled!"),
    not_in_duty("%prefix% &cYou are not in staff duty!"),

    // Custom timers
    customt_created("%prefix% §aCustom timer successfully created!"),
    customt_deleted("%prefix% §aCustom timer successfully deleted!"),
    customt_edited("%prefix% §aCustom timer successfully edited!"),
    customt_no_active_timer("%prefix% §cNo available active timers!"),
    customt_not_found("%prefix% §cThis timer not found!"),
    already_created("%prefix% §aThis name already in use!"),



    sotw_start_title("SOTW STARTED"),
    sotw_start_subtitle("Kawaii"),
    eotw_start_title("EOTW STARTED"),
    eotw_start_subtitle("UwU"),
    cant_teleport_to_safezone("%prefix% &cYou can't teleport to a protected zone while you are in PvP tag"),
    missing_argument("%prefix% &cThis command requires &none or more&f&c argument!"),

    faction_show(Arrays.asList(
            "&8&m        &c %faction_name% &f[%faction_status%&f] &8&m        ",
            "&7&l» &eLeader: &a%leader_name%",
            " ",
            "&6Members &7[%members_online%/%members_count%]",
            "%members_list_categories_and_members%",
            " ",
            "&7┌─",
            "&7│ &eBalance: &a$%faction_balance%",
            "&7│ &eDTR: &a%faction_dtr%&7/&a%faction_max_dtr% &eRaidable: &a%faction_raidable%",
            "&7│ &eDTR Regenerating: %faction_dtr_regen",
            "&7│ &eHome Location: &a%faction_pos%",
            "&7├ &eKills: &a%faction_kills%",
            "&7├ &eDeaths: &a%faction_deaths%",
            "&7└─"
    )),

    /*CLAIM_INFO(Arrays.asList(
            "%prefix% &6Claiming information:",
            "&7&l» &ePress &6&o[RIGHT] &eclick on the ground, to place one of the positions!", // pos1
            "&7&l» &ePress &6&o[LEFT] &eclick on the ground, to place the other position!", // pos2
            "&eTo accept the claim, press &6&oSHIFT + RIGHT &eclick!", // right click
            "&eTo discard the claim, press &6&oSHIFT + LEFT &eclick!" // left click
    )),*/

    claim_info(Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),

    spawn_claim(Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),

    koth_claim(Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),

    claim_info_admin(Arrays.asList(
            "%prefix% &6Spawn claiming information:",
            "&7&l» &ePress &6&o[RIGHT] &eclick on the ground, to place one of the positions!", // pos1
            "&7&l» &ePress &6&o[LEFT] &eclick on the ground, to place the other position!", // pos2
            "&eTo accept the claim, use &b&n/a spawnclaim claim", // right click
            "&eTo discard the claim, use &c&n/a spawnclaim end" // left click
    )),

    commands(Arrays.asList(
            "§9/f create <Name> §7- Create your faction",
            "§9/f show [Faction] §7- Show faction",
            "§9/f claim §7- Claiming",
            "§9/f invite <Player> §7- Invite player to your faction",
            "§9/f join §7- Join to a faction",
            "§9/f leave §7- Leave from your faction",
            "§9/f deposit <Amount> §7- Deposit money to your faction bank",
            "§9/f withdraw <Amount> §7- Withdraw money from faction bank",
            "§9/f sethome §7- Sets a home to your faction",
            "§9/f home §7- Teleport to faction's home",
            "§9/f reload [file] §7- Reload files"
    )),

    koth_commands(Arrays.asList(
            "§9/koth create <Name> §7- Create the koth",
            "§9/koth setcapturezone <Name> §7- Claim the koth place (CAPTURE ZONE)",
            "§9/koth setnatrualzone <Name> §7- Claim the koth place (NEUTRAL ZONE)",
            "§9/koth setreward §7- Set the koth reward with GUI"

    )),

    admin_commands(Arrays.asList(
            "§9/admin duty §7- Entering duty mode",
            "§9/admin deposit <Faction> <Amount> §7- Deposit x amount money to a faction",
            "§9/admin withdraw <Faction> <Amount> §7- Withdraw x amount money from a faction",
            "§9/admin freeze <Player> §7- Freeze/Unfreeze a player",
            "§9/admin kick <Player> §7- Kick a player from the server",
            "§9/admin setfaction <Player> <Faction> §7- Add the player to a faction",
            "§9/admin removefaction <Player> <Faction> §7- Remove the player from a faction",
            "§9/admin eotw §7- Starts the EOTW (End Of The World) (Time in the config file)",
            "§9/admin deletefaction <Faction> §7- Delete the selected faction",
            "§9/admin setleader <Faction> <newLeader> §7- Set the player to a faction leader",
            "§9/admin givemoney <Player> <Amount> §7- Gives x amount money to a player",
            "§9/admin takemoney <Player> <Amount> §7- Takes x amount money from a player",
            "§9/admin setfactionname <Faction> <Name> §7- Sets a faction to a new name",
            "§9/admin spawnclaim <state> §7- Starts the spawn claiming method!"
    ));

    public String section;
    public String message, defaultMessage, playerMessage;
    public List<String> listMessages, defaultListMessages, playerListMessages;
    public String[] comments;
    public String thisPrefix;

    Messages(String message) {
        this.message = message;
        this.defaultMessage = message;
        this.playerMessage = message;
    }

    Messages(String section, String message) {
        this.section = section;
        this.message = message;
        this.defaultMessage = message;
        this.playerMessage = message;
    }

    Messages(String message, String[] comments) {
        this.message = message;
        this.defaultMessage = message;
        this.comments = comments;
        this.playerMessage = message;
    }

    Messages(String section, String message, String[] comments) {
        this.section = section;
        this.message = message;
        this.defaultMessage = message;
        this.comments = comments;
        this.playerMessage = message;
    }

    Messages(List<String> listMessages) {
        this.listMessages = listMessages;
        this.defaultListMessages = listMessages;
        this.playerListMessages = listMessages;
    }

    Messages(String section, List<String> listMessages) {
        this.section = section;
        this.listMessages = listMessages;
        this.defaultListMessages = listMessages;
        this.playerListMessages = listMessages;
    }

    Messages(List<String> listMessages, String[] comments) {
        this.listMessages = listMessages;
        this.defaultListMessages = listMessages;
        this.comments = comments;
        this.playerListMessages = listMessages;
    }

    Messages(String section, List<String> listMessages, String[] comments) {
        this.section = section;
        this.listMessages = listMessages;
        this.defaultListMessages = listMessages;
        this.comments = comments;
        this.playerListMessages = listMessages;
    }

    public Messages language(Player p) {
        String language = MessagesTool.getPlayerLanguage(p);
        String prefix = MessagesTool.getLanguageMessages(language).getString("prefix");
        if(this.isString()) {
            String configMessage = MessagesTool.getLanguageMessages(language).getString(this.toString());
            if(configMessage != null) {
                this.playerMessage = ChatColor.translateAlternateColorCodes('&', configMessage.replace("%prefix%", prefix));
                return this;
            } else
                MessagesTool.updateMessageFiles();
        } else {
            List<String> returnList = new ArrayList();
            List<String> configList = MessagesTool.getLanguageMessages(language).getStringList(this.toString());

            if(!configList.isEmpty()) {
                for (String message : configList) {
                    returnList.add(
                            ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", prefix))
                    );
                }
                this.playerListMessages = returnList;
            } else {
                MessagesTool.updateMessageFiles();
                for (String message : this.listMessages) {
                    returnList.add(
                            ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", prefix))
                    );
                }
                this.playerListMessages = returnList;
            }
            return this;
        }
        this.playerMessage = ChatColor.translateAlternateColorCodes('&', this.message.replace("%prefix%", this.thisPrefix));
        return this;
    }

    public String queue() {
        if(this.playerMessage == null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }

        String tempMessage = this.playerMessage;
        this.playerMessage = this.message;
        return ChatColor.translateAlternateColorCodes('&', tempMessage);
    }

    public List<String> queueList() {
        List<String> tempList = new ArrayList<>();
        for (String s : this.playerListMessages) {
            tempList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        this.playerListMessages = this.listMessages;
        return tempList;
    }

    public Messages setFaction(Faction faction) {
        if(this.isString()) {
            this.playerMessage = this.playerMessage.replaceAll("%faction_name%", faction.name);
        }

        return this;
    }

    public Messages setFaction(String faction) {
        if(this.isString()) {
            this.playerMessage = this.playerMessage.replaceAll("%faction_name%", faction);
        }
        return this;
    }

    public Messages setRank(String rank) {
        playerMessage = playerMessage.replace("%rank%", rank);
        return this;
    }

    public Messages setCoords(int x, int y, int z) {
        message = playerMessage
                .replace("%location_x%", x + "")
                .replace("%location_y%", y + "")
                .replace("%location_z%", z + "");
        return this;
    }

    public Messages setDisplayName(Player p) {
        if(p.getDisplayName() == null) {
            playerMessage = playerMessage.replace("%player_displayname%", p.getName());
        } else {
            playerMessage = playerMessage.replace("%player_displayname%", p.getDisplayName());
        }
        return this;
    }

    public Messages setBardEffects(Player bard, String effect, String members) {
        playerMessage = playerMessage.replace("%bard%", bard.getName()).replace("%effect%", effect).replace("%count%",members);
        return this;
    }

    public Messages setItem(ItemStack item) {
        playerMessage = playerMessage.replace("%item%", (item.getType().name().charAt(0) + item.getType().name().substring(1).toLowerCase()).replace("_", " "));
        return this;
    }

    public Messages setLoc(int x, int z) {
        playerMessage = playerMessage.replace("%loc%", "X:"+x + " Y:"+z);
        return this;
    }

    public Messages setPrice(int price) {
        playerMessage = playerMessage.replace("%price%", String.valueOf(price));
        return this;
    }

    public Messages setExecutor(Player executor) {
        playerMessage = playerMessage.replace("%executor%", executor.getName());
        return this;
    }

    public Messages setDeath(Player victim, Player killer) {
        playerMessage = playerMessage.replace("%victim%", victim.getName()).replace("%killer%", killer.getName());
        return this;
    }


    public Messages setDeathWithoutKiller(Player victim) {
        String victim_kills = playertools.getMetadata(victim,"kills");
        playerMessage = playerMessage.replace("%victim%", victim.getName())
                .replace("%victim_kills%",victim_kills);
        return this;
    }

    public Messages setVictimWithKills(Player victim, Player killer) {
        String weaponName = (killer.getItemInHand().hasItemMeta()) ?
                (killer.getItemInHand().getItemMeta().hasDisplayName()
                        ? killer.getItemInHand().getItemMeta().getDisplayName()
                        : killer.getItemInHand().getType().name().toLowerCase())
                : killer.getItemInHand().getType().name().toLowerCase();
        String killer_kills = playertools.getMetadata(killer,"kills");
        String victim_kills = playertools.getMetadata(victim,"kills");
        playerMessage = playerMessage.replace("%victim%", victim.getName())
                .replace("%killer%", killer.getName())
                .replace("%killer_kills%",killer_kills)
                .replace("%victim_kills%",victim_kills)
                .replace("%killer_weapon%", weaponName);
        return this;
    }

    public Messages setMessage(String message) {
        this.playerMessage = playerMessage.replace("%message%", message);
        return this;
    }

    public Messages setFormattedTime(int seconds) {
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        playerMessage = playerMessage.replace("%format_time%", String.format("%02d:%02d",MM,SS));
        return this;
    }

    public Messages setZone(String zoneName) {
        playerMessage = playerMessage.replace("%zone_name%", zoneName);
        return this;
    }

    public void load() {
        SimpleConfig msgs = ConfigManager.getEnglishMessages();

        if (msgs == null) return;

        if (msgs.getString((this.section == null ? "" : this.section + ".") + this.toString()) == null) {
            Object obj = this.message;
            if(this.listMessages != null)
                obj = this.listMessages;

            if(this.section == null) {
                if (this.comments == null)
                    msgs.set(this.toString(), obj);
                else
                    msgs.set(this.toString(), obj, this.comments);
            } else {
                if (this.comments == null)
                    msgs.set(section + "." + this.toString(), obj);
                else
                    msgs.set(section + "." + this.toString(), obj, this.comments);
            }

            save();
            this.thisPrefix = msgs.getString("prefix");
            return;
        }

        this.message = msgs.getString((this.section == null ? "" : this.section + ".") + this.toString());

        this.thisPrefix = msgs.getString("prefix");
    }

    public void loadOthers(SimpleConfig config) {
        SimpleConfig msgs = config;

        if (msgs == null) return;

        if (msgs.getString((this.section == null ? "" : this.section + ".") + this.toString()) == null) {
            Object obj = this.message;
            if(this.listMessages != null)
                obj = this.listMessages;

            if(this.section == null) {
                if (this.comments == null)
                    msgs.set(this.toString(), obj);
                else
                    msgs.set(this.toString(), obj, this.comments);
            } else {
                if (this.comments == null)
                    msgs.set(section + "." + this.toString(), obj);
                else
                    msgs.set(section + "." + this.toString(), obj, this.comments);
            }

            config.saveConfig();
            // save();
            return;
        }
    }

    public void save() {
        SimpleConfig msgs = ConfigManager.getEnglishMessages();

        //if(this.comments == null)
        if(this.listMessages == null) {
            if(this.section == null)
                msgs.set(this.toString(), this.message);
            else
                msgs.set(section + "." + this.toString(), this.message);
        } else {
            if(this.section == null)
                msgs.set(this.toString(), this.listMessages);
            else
                msgs.set(section + "." + this.toString(), this.listMessages);
        }
        /*else
            msgs.set(this.toString(), this.message, this.comments);*/

        ConfigManager.getEnglishMessages().saveConfig();
    }

    private boolean isString() {
        if(this.defaultMessage != null)
            return true;
        return false;
    }

    public Messages setPlayer(Player p) {
        if(this.isString())
            this.playerMessage = this.playerMessage.replaceAll("%player%", p.getName());
        else replaceList("%player%", p.getName());
        return this;
    }
    public Messages setPlayer(HCFPlayer p) {
        if(this.isString())
            this.playerMessage = this.playerMessage.replaceAll("%player%", p.name);
        else replaceList("%player%", p.name);
        return this;
    }

    /**
     * Replacing '%amount%' variable
     */
    public Messages setAmount(String amount) {
        if(this.isString())
            this.playerMessage = this.playerMessage.replaceAll("%amount%", amount);
        else replaceList("%amount%", amount);
        return this;
    }

    private List<String> replaceList(String key, String value) {
        List<String> returnList = new ArrayList<>();
        for (String message : this.playerListMessages) {
            returnList.add(
                    ChatColor.translateAlternateColorCodes('&', message.replace(key, value))
            );
        }

        return returnList;
    }

    public Messages replace(String... strings) {
        String key = "";
        for (int i = 0; i < strings.length; i++) {
            if(strings[i].startsWith("%") && strings[i].endsWith("%")) {
                key = strings[i];
                continue;
            }
            if(this.isString())
                this.playerMessage = ChatColor.translateAlternateColorCodes('&', this.playerMessage.replace(key, strings[i]));
            else replaceList(key, strings[i]);
            key = "";
        }

        return this;
    }

    public Messages setMembers(HashMap<String, List<String>> categories, HashMap<String, String> players) {
        List<String> listike = new ArrayList<>();

        // Sorted hashMap
        Map<String, List<String>> sortedMap = sortbykey(categories);

        for (String line : this.playerListMessages) {
            if (line.equalsIgnoreCase("%members_list_categories_and_members%")) {
                for (Map.Entry<String, List<String>> category : sortedMap.entrySet()) {

                    if (category.getKey().equalsIgnoreCase("Leader")) continue;

                    listike.add(Messages.category_design.queue()
                            .replace("%category%",
                                    category.getKey().substring(0, 1).toUpperCase() + category.getKey().substring(1)));

                    List<String> playersList = new ArrayList<>();
                    for (Map.Entry<String, String> player : players.entrySet()) {
                        if (category.getValue().contains(player.getKey())) { // player a rankban!!!
//                            if(Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(Bukkit.getPlayer(player.getKey()), "factionid")))
//                                    .player_ranks.get(Bukkit.getPlayer(player.getKey())).isLeader)
                            if (Bukkit.getPlayer(player.getKey()) != null) { // player online!!
                                playersList.add(Messages.member_list_design_online.queue()
                                        .replace("%member%", player.getKey())
                                        .replace("%member_kill%", player.getValue()));
                            } else {
                                playersList.add(Messages.member_list_design_offline.queue()
                                        .replace("%member%", player.getKey())
                                        .replace("%member_kill%", player.getValue()));
                            }
                        }
                    }
                    listike.add(playersList.toString().substring(1, playersList.toString().length() - 1));
                }
            }
            if (!line.contains("%members_list_categories_and_members%")) {
                listike.add(line);
            }
        }
        this.playerListMessages = listike;
        return this;
    }

    public Messages setupShow(
            String factionName, String factionStatus, String leaderName, String factionBalance, String factionKills, String factionDeaths,
            String factionPos, String factionDtr, String factionDtrRegen, String dtrMax, String onlinesMembers,
            String totalMembers, String isRaidable) {
        List<String> lines = new ArrayList<>();
        for (String line : playerListMessages) {
            lines.add(line
                    .replace("%faction_name%", factionName)
                    .replace("%faction_status%", factionStatus)
                    .replace("%leader_name%", leaderName)
                    .replace("%faction_balance%", factionBalance)
                    .replace("%faction_kills%", factionKills)
                    .replace("%faction_deaths%", factionDeaths)
                    .replace("%faction_pos%", (factionPos.equals("0, 0") ? "None" : factionPos))
                    .replace("%faction_max_dtr%", dtrMax)
                    .replace("%faction_dtr_regen", factionDtrRegen)
                    .replace("%faction_dtr%", factionDtr)
                    .replace("%members_online%", onlinesMembers)
                    .replace("%members_count%", totalMembers)
                    .replace("%faction_raidable%", isRaidable)
            );
        }
        playerListMessages = lines;
        return this;
    }

    private static Map<String, List<String>> sortbykey(HashMap map) {
        // TreeMap to store values of HashMap
        Map<String, List<String>> sorted = new TreeMap<>(map);

        // Copy all data from hashMap into TreeMap
        sorted.putAll(map);

        // Display the TreeMap which is naturally sorted
        return sorted;
    }
}
