package me.idbi.hcf.CustomFiles.Messages;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import me.idbi.hcf.CustomFiles.MessagesTool;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.ChatTypes;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public enum Messages {
    //Sections: Server,"Claims",Factions,
    prefix("&8[&2HCF&a+&8] &7>"),
    prefix_cmd("&r[&2HCF&a+&r] &r"),
    reload("Server","%prefix% &aSuccessfully reloaded configuration files!"),
    invalid_type("Server","Invalid Type! Check the command description for more details"),
    cant_start_eotw("Server","%prefix% &ceotw start failed! (No spawn location defined! Please use &n/a spawnclaim&f&c)"),
    chat_prefix_faction("Factions","&8[&6%faction_name%&8] &e%player% &f> &7%message%"),
    chat_prefix_without_faction("Factions","&e%player% &f> &7%message%"),
    // Errors
    no_permission("Server","%prefix% &cYou don't have permission!"),
    no_permission_in_faction("Factions","%prefix% &cYou need a higher rank to use this command!"),
    you_cant_do("Factions","%prefix% &eYou can't do this at &c&o%faction_name%'s &eplace!"),
    not_enough_slot("Server","%prefix% &cYou don't have enough space!"),
    not_found_player("Server","%prefix% &cCan't find this player!"),
    not_found_faction("Server","%prefix% &cCan't find this faction!"),
    command_cooldown("Server","%prefix% &cYou must wait &e%time%s &cbetween uses!"),
    not_a_number("Server","%prefix% &cPlease use a valid number"),
    not_a_valid_name("Server","%prefix% &cPlease use a valid name"),
    server_slot_amount("Server","%prefix% &aThe server has currently &2&o%number% &aslots!"),
    server_slot_set("Server","%prefix% &aYou have successfully set the slots to: &2&o%number%&a!"),

    cant_demote("Factions","%prefix% &cYou can't demote &c&o%player%"),
    chat_unavailable("Server","%prefix% &cThis chat-channel is not available!"),

    dont_have_item("Server","%prefix% &cYou don't have enough items in your inventory!"),
    custom_item_give_executor("Server", "&aYou gave &2&o%item%&a to &2&o%player%&a."),
    custom_item_give_player("Server", "&aYou got &2&o%item%&a."),

    // Errors - faction
    exists_faction_name("Factions","%prefix% &cThis faction name already exists!"),
    you_already_in_faction("Factions","%prefix% &cYou are already in faction!"),
    not_in_faction("Factions","%prefix% &cYou are not in a faction!"),
    player_in_faction("Factions","%prefix% &cThis player is already in a faction!"),
    player_cant_self_promote("Factions","%prefix% &c&nNice try&r&c, but you cant promote yourself :("),
    player_cant_self_demote("Factions","%prefix% &c&nNice try&r&c, but you cant demote yourself :("),

    uninvite_target("Factions","%prefix% &2&o%player%&c canceled your invitation!"),
    uninvite_executor("Factions","%prefix% &cYou canceled to invite &2&o%player%&c!"),
    already_invited("Factions","%prefix% &cThis player is already invited!"),
    not_invited("Factions","%prefix% &cYou are not invited to this faction!"),

    no_faction_exists("Factions","%prefix% &cThis faction does not exists!"),

    already_invited_ally("Ally","%prefix% &cThis faction is already invited to your ally."),
    not_invited_ally("Ally","%prefix% &cThis faction didn't invited you to their ally."),
    //
    faction_invited_ally("Ally","%prefix% &2&o%faction_name% &awas invited to join this faction ally."),
    faction_not_ally("Ally","%prefix% &2&o%faction_name% &c is not your ally!"),
    joined_ally("Ally","%prefix% &2&o%faction_name% &anow is your ally!"),
    faction_decline_ally_target("Ally","%prefix% &2&o%faction_name% &chad declined the alliance!"),
    faction_decline_ally_success("Ally","%prefix% &aYou have successfully declined &2&o%faction_name%&a alliance!"),
    faction_uninvite_ally_target("Ally","%prefix% &2&o%faction_name% &chas withdrawn the ally invitation!"),
    faction_uninvite_ally_success("Ally","%prefix% &aYou successfully uninvited &2&o%faction_name%&a from your allies!"),
    faction_ally_resolve_target("Ally","%prefix% &2&o%faction_name%&a had dissolved the alliance!"),
    faction_ally_resolve_success("Ally","%prefix% &aYou have successfully dissolved the alliance with &2&o%faction_name%&a!"),

    faction_team_focused("Factions","%prefix% &2&o%faction_name% &ahas been focused by &2&o%player%&a!"),
    faction_team_unfocused("Factions","%prefix% &eYour team focus has been cleared!"),
    faction_own_team_focus("Factions","%prefix% &cYou can't focus your faction!"),

    faction_rally("Factions","%prefix% &2&o%player% &ahas set a rally point at &2&o%location% &a(&2&o%world%&a)"),
    faction_unrally("Factions","%prefix% &eYour factions rally point has been removed!"),

    // Commands
    faction_creation("Factions","%prefix% &2&o%faction_name% &awas created by &2&o%player%&a!"),
    faction_create_success("Factions","%prefix% &aYou have successfully created the &2&o%faction_name%&a faction!"),
    faction_disband("Factions","%prefix% &2&o%faction_name% &awas disbanded by &2&o%player%&a!"),
    faction_delete_success("Factions","%prefix% &aYou have successfully deleted the &2&o%faction_name%&a faction!"),
    faction_claim_price("Claims","%prefix% &aPrice: &2&o$%price% &afor &2&o%blocks%"),

    //Claim actions
    faction_claim_decline("Claims","%prefix% &cYou successfully cancelled the claim!"),
    faction_claim_accept("Claims","%prefix% &aYou successfully claimed!"),
    faction_claim_invalid_zone("Claims","%prefix% &cInvalid claiming zone!"),
    faction_claim_overlap("Claims","%prefix% &cYour claim can't overlap an existing claim!"),
    faction_claim_too_small("Claims","%prefix% &cYour claim is too small! (Min size: %number%x%number%)"),
    //The difference between sizes :o
    faction_claim_too_large("Claims","%prefix% &cYour claim is too large! (Max size: %number%x%number%)"),
    faction_reached_max_claims("Claims","%prefix% &cYour faction reached the max amount of claims."),
    faction_claim_overlap_plus_one("Claims","%prefix% &cYour claim can't overlap an existing claim! Leave a block between the two claims"),
    faction_claim_not_connected("Claims","%prefix% &cYour claim is not connected to the main claim."),
    faction_not_have_claim("Claims","%prefix% &cYour faction does not have any claims."),
    success_unclaim("Claims","%prefix% &aYou successfully unclaimed!"),



    // Commands - invite
    invited_by("Factions","%prefix% &6&o%executor% &einvited you to join &6&o%faction_name%&e!"),
    invited_player("Factions","%prefix% &eYou invited &6&o%player% &eto the faction!"),
    faction_invite_broadcast("Factions","%prefix% &6&o%executor% &einvited &6&o%player% &eto the faction!"),

    // Faction join and quit
    new_member_join_faction("Factions","%prefix% &6&o%player% &ejoined the faction!"),
    member_leave_faction("Factions","%prefix% &6&o%player% &eleft the faction!"),
    player_joined_faction_message("Factions","%prefix% &eYou joined the faction!"),
    player_leaving_faction_message("Factions","%prefix% &aYou left the faction!"),

    // Faction bank
    faction_bank_deposit("Factions","%prefix% &aSuccessfully deposited &2&o$%amount% &ain your faction's bank!"),
    faction_bank_withdraw("Factions","%prefix% &aSuccessfully withdrew &2&o$%amount% &afrom your faction's bank!"),
    faction_bank_not_enough("Factions","%prefix% &cYour faction doesn't have enough money!"),
    faction_bank_number_error("Factions","%prefix% &cPlease enter a valid number!"),
    not_enough_money("Factions","%prefix% &cYou don't have enough money!"),
    faction_transfer_leader("Factions","%prefix% &aYou have successfully transferred the leadership to &6&o%player%&a!"),
    faction_transfer_leader_target("Factions","%prefix% &6&o%player%&a transferred the leadership to you!"),

    // Server broadcasts
    // FACTION_CREATED("&6&o%faction_name% &esuccessfully created by &6&o%player%&e!"), // Sexy lesz :D azám :D
    // DEATH_MESSAGE("&a&o%victim% &ekilled by &a&o%killer%&e."),
    //PVP_QUIT_MESSAGE("&a&o%player% leaved "),

    // Zones interact You've exited
    leave_zone("Claims","&eYou left %zone%"),
    entered_zone("Claims","&eYou entered %zone%"),
    //    zone_friendly("&a%zone%"),
//    zone_enemy("&c%zone%"),
    wilderness("Claims","&2Wilderness"),
    cant_find_page("Server","&cThis page does not exists!"),

    // Faction messages
    kick_message("Factions","&%prefix% &a%executor% &ekicked &a%player%&e from the faction!"),
    kick_message_target("Factions","&%prefix% &cYou have been kicked from the faction!"),
    /*withdraw_message("%prefix% &eYou withdrew &6&o$%amount% &efrom faction bank!"),
    deposit_message("%prefix% &eYou deposited &6&o$%amount% &eto faction bank!"),
    executor_invite_message("&eYou invited &6&o%player% &eto your faction!"),
    invited_invite_message("&eYou invited to &6&o%faction_name% &efaction by &6&o%executor%&e!"),*/
    leader_leaving_faction("Factions","%prefix% &cYou can't leave this faction, because you are the leader! Use &e&l/f disband %faction% &r&cinstead!"),
    not_leader("Factions","%prefix% &cYou are not the faction's leader!"),
    cant_kick_yourself("Factions","%prefix% &cYou can't kick yourself!"),
    cant_kick_leader("Factions","%prefix% &cYou can't the faction leader!"),
    join_online_player_in_faction("Factions","&8[&a+&8] &7&l» &6&o%player%"),
    leave_online_player_in_faction("Factions","&8[&c-&8] &7&l» &6&o%player%"),
    faction_player_position("Factions","%prefix% &7-> &6&o%player%'s &eposition: &6&o%location_x%&e, &6&o%location_y%&e, &6&o%location_z%"),
    faction_dont_have_claim("Claims","%prefix% &4Your faction does not have a claim yet!"),

    looking_for_faction("Factions", "&2&o%player% &alooking for faction!"),

    // reclaim
    reclaim_claimed("Reclaim","%prefix% &aYou successfully reclaimed!"),
    reclaim_already_claimed("Reclaim","%prefix% &aYou already reclaimed!"),
    reclaim_reset_all("Reclaim","%prefix% &aYou successfully reclaim for all player!"),
    reclaim_reset_player("Reclaim","%prefix% &aYou successfully reclaim for &2&o%player%!"),

    // Faction home
    sethome_message("Factions","%prefix% &aSuccessfully set home of your faction to your location!"),
    sethome_update_faction("Factions","%prefix% &7&l%player% &f&echanged the faction's home location! &7[&6&o%location_x%&e, &6&o%location_y%&e, &6&o%location_z%&7]"),
    teleport_to_home("Factions","%prefix% &eTeleportation in &6&o%time% seconds&e. &cDo not move!"),
    already_home_teleporting("Factions","%prefix% &cTeleportation canceled!"),
    successfully_home_teleport("Factions","%prefix% &eSuccessfully teleported to your faction's home!"),
    teleport_cancel("Factions","%prefix% &cYou moved, teleportation cancelled!"),
    no_home("Factions","%prefix% &cYour faction doesn't have a home set!"),
    not_deathbanned("Server","%prefix% &cThis player is not deathbanned!"),

    // Faction chat
    staff_chat("Chat","&8[&eStaffChat&8] &b%player%&7: &e%message%"),
    faction_chat("Chat","&7[&e%faction_name%&7] [&e%rank%&7] &6&o%player%&7: &e%message%"),
    ally_chat("Chat","&7[&dAllience&7] &7[&d%faction_name%&7] &5&o%player%&7: &d%message%"),
    leader_chat("Chat","&7[&aLeader&7] &2&o%player%&7: &a%message%"),
    toggle_chat_enable("Chat","%prefix% &aYou enabled the &2&o%chat% &achat."),
    toggle_chat_disable("Chat","%prefix% &cYou disabled the &2&o%chat% &cchat."),
    cant_send_message_to_channel("Chat","%prefix% &cYou can't send message, because you muted this channel!"),
/*    faction_chat_toggle_on("%prefix% &aYou are now in &2&ofaction chat&a!"),
    faction_chat_toggle_off("%prefix% &cYou are now in &c&oALL chat&c!"),*/
    chat_channel_changed("Chat","%prefix% &aYou are now in &2&o%chat% &achat."),
    public_chat_channel("Chat","Public"),
    staff_chat_channel("Chat","Staff"),
    faction_chat_channel("Chat","Faction"),
    ally_chat_channel("Chat","Ally"),
    leader_chat_channel("Chat","Leader"),

    promote_message("Factions","%prefix% &6&o%executor% &epromoted you to &6&o%rank%&e!"),
    demote_message("Factions","%prefix% &6&o%executor% &edemoted you to &6&o%rank%&e!"),
//
    executor_promote_message("Factions","%prefix% &eYou promoted &6&o%player% &eto &6&o%rank%&e!"),
    executor_demote_message("Factions","%prefix% &eYou demoted &6&o%player% &eto &6&o%rank%&e!"),
    executor_promote_broadcast_message("Factions","%prefix% &6&o%executor% &epromoted &6&o%player% to &6&o%rank%&e!"),
    executor_demote_broadcast_message("Factions","%prefix% &6&o%executor% &edemoted &6&o%player% to &6&o%rank%&e!"),


//
//    faction_create_rank("Factions","%prefix% &aRank successfully created!"),
//    faction_rank_exists("Factions","%prefix% &cThis rank already exists!"),
//
//    // Console error
//    not_valid_blacklisted_blocks("Server","§cInvalid material type in config. Excepted material: %material%"),
//    sign_shop_material("Server","§cInvalid material in the sign shop."),

    // Designs
    status_design_online("Design","&aOnline"),
    status_design_offline("Design","&cOffline"),
    category_design("Design","&7&l» &6%category%:"),
    member_list_design_online("Design","&a%member% &f[&7%member_kill%&f]"),
    member_list_design_offline("Design","&7%member% &f[&7%member_kill%&f]"),

    faction_list_title("Design","&7&m--- &6Faction List by %type% &7&m---"),
    faction_list("Design","&7%number%. &e%faction_name% &6-- &7[&a%online%&7] [%dtr%&7]"),

    faction_top_title("Design","&7&m--- &6Faction Top by %type% &7&m---"),
    faction_top("Design","&7%number%. &e%faction_name% &6-- &e%value%"),
    faction_top_list_gui_prefix("Design","&7#%number% "),

    faction_chat_page("Page", "&2Page %page%"),
    faction_chat_page_previous("Page", "&a<<< "),
    faction_chat_page_next("Page", " &a>>>"),

    // Sign Shop message
    sign_shop_bought("Shop","%prefix% &aYou bought &2&o%amount%x %item% &afor &2&o$%price%&a."),
    sign_shop_sold("Shop","%prefix% &aYou sold &2&o%amount%x %item% &afor &2&o$%price%&a."),

    // Admin Duty
    admin_duty_on("Admin","%prefix% &aDuty mode: &2&oon"),
    admin_duty_off("Admin","%prefix% &cDuty mode: &4&ooff"),
    freeze_player_on("Admin","%prefix% &aYou got frozen by &2&o%executor%"),
    freeze_player_off("Admin","%prefix% &cYou got unfrozen by &4&o%executor%"),
    freeze_executor_on("Admin","%prefix% &aYou froze &2&o%player%"),
    freeze_executor_off("Admin","%prefix% &cYou unfroze &4&o%player%"),
    admin_freeze_dtr_enable("Admin","%prefix% &aYou froze &2&o%faction_name%&a's DTR to: &o%dtr%"),
    admin_freeze_dtr_disable("Admin","%prefix% &aYou unfroze &2&o%faction_name%&a's DTR"),

    spawn_claim_success("Claims","%prefix% &bYou successfully claimed the spawn!"),

    faction_admin_withdraw("Admin","%prefix% &c&l%executor% &cdeposited &c&l%amount% &cinto the faction's bank!"),
    faction_admin_deposit("Admin","%prefix% &c&l%executor% &cwithdrew &c&l%amount% &cfrom the faction's bank!"),
    admin_set_playerfaction("Admin","%prefix%&1 You successfully put &6&o%player% in the %faction_name% faction"),
    set_faction_name("Admin","%prefix% &eFaction's name changed by &6&o%executor% &eto &6&o%faction_name%&e!"),
    admin_set_dtr("Admin","%prefix% &eYou have successfully set the DTR of &6&o%faction_name%&e faction to &6&o%new_dtr%"),
    admin_set_faction_name("Admin","%prefix% &1You have successfully changed the name of the faction to &e%faction_name%"),
    give_money("Admin","%prefix% &eYou got &6&o%amount% &efrom &6&o%executor%&e!"),
    take_money("Admin","%prefix% &6&o%executor%&e took &6&o%amount% &efrom you!"),

    set_faction_leader_by_admin("Admin","%prefix% &6&o%executor%&e set &6&o%player% &eto faction leader!"),
    delete_faction_by_admin("Admin","%prefix% &6&o%executor%&e deleted the &6&o%faction_name% &efaction!"),

    cant_damage_admin("PvP","%prefix% &cYou can't attack while you are in duty mode"),
    cant_damage_protected_area("PvP","%prefix% &cYou can't attack while you are in a protected area!"),
    combat_message("PvP","%prefix% &7You are now in combat &a[%sec% sec]"),
    teammate_damage("PvP","%prefix% &cYou can't damage your teammate!"),

    deathban_kick("Server","&cYou are deathbanned for %format_time%"),
    pvp_timer_enable("PvP","%prefix% &aYou no longer have any pvp protection."),
    pvp_timer_already_enable("PvP","%prefix% &cYou do not have any timers active."),

    logout_timer_started("Logout","%prefix% &4&lLogout timer&r&4 started! Don't move!"),
    logout_timer_interrupted("Logout","%prefix% &4Logout interrupted!"),
    logout_timer_already_started("Logout","%prefix% &4You are already started to logout!"),
    logout_kick_message("Logout","&aYou have been logged out safely!"),

    kill_message_broadcast("PvP","&4&l%killer%&f[%killer_kills%] slained &4&l%victim%&f[%victim_kills%] using [&b&o%killer_weapon%&f]"),

    kill_message_broadcast_without_killer("PvP","&4&l%victim%&f[%victim_kills%] died"),

    max_members_reached("Factions","%prefix% &4&lYou can't invite more people to the faction because it's full!"),

    error_while_executing("Server","%prefix% &4&lAn error occurred while running the command! Please check the LOG file, and report it!"),
    kill_message_faction("Factions","&4&l%killer%&f killed &4&l%victim%&f. &7[&eNew DTR: &a&l%dtr%&7]"),

    enchant_confirm_button("&aConfirm"),
    enchant_cancel_button("&cCancel"),
    enchant_not_enough_xp("%prefix% &cYou don't have enough exp!"),
    confirm_button_lore("&6Costs: &e&n%xp_level% level"),

    bard_dont_have_enough_energy("Classes","%prefix% &cYou don't have enough energy to activate this. &4&l[Required %amount%]"),

    bard_used_powerup("Classes","%prefix% &6You used &b%effect% &6for &b&o%amount%&r&6. [&aBuffed teammates: &b&o%count%&r&6]"),

    cant_damage_while_pvptimer("PvP","%prefix% &cYou can't attack while you have a PvP timer on you!"),
    cant_damage_while_pvptimer_victim("PvP","%prefix% &cThis player has a PvP timer!"),
    cant_damage_while_sotw_timer_active("PvP","%prefix% &cThis player has an active SOTW timer!"),
    stuck_started("Stuck","%prefix% &aStuck timer started! You are getting teleported to a safe zone in &o&e%amount%&r&a seconds! Don't move!"),
    stuck_finished("Stuck","%prefix%  &bYou successfully teleported to a safe zone!"),
    stuck_interrupted("Stuck","%prefix% &cYou interrupted the stuck timer!"),

    claim_pos_start("Claims","%prefix% &bClaim start position: &a&n%location%"),
    claim_pos_end("Claims","%prefix% &bClaim end position: &a&n%location%"),

    // KOTH SOTW EOTW
    enable_eotw("Eotw","%prefix% &aEOTW started!"),
    disable_eotw("Eotw","%prefix% &cEOTW disabled!"),
    eotw_already_started("Eotw","%prefix% &cEOTW already started!"),
    enable_sotw("Eotw","%prefix% &aSOTW started!"),
    disable_sotw("Eotw","%prefix% &cSOTW disabled!"),
    enable_deathmatch("Eotw","%prefix% &aDeathmatch started!"),
    disable_deathmatch("Eotw","%prefix% &cDeathmatch disabled!"),

    // koth
    updated_koth_rewards("Koth","%prefix% &aSuccessfully saved rewards!"),
    claim_koth_rewards("Koth","%prefix% &aYou claimed KOTH rewards!"),
    koth_invalid_name("Koth","%prefix% &cThis KOTH doesn't exits!"),
    koth_success_create("Koth","%prefix% &aSuccessfully created the &6&o%faction_name%&a koth!"),
    koth_capturing_started("Koth","%prefix% &aSomeone started to occupy the &6&o%faction_name%&a KOTH!"),
    koth_capturing_ended("Koth","%prefix% &aThe occupation of KOTH &6&o%faction_name%&a was interrupted!"),
    koth_capture_timer("Koth","%prefix% &aSomeone is capturing &6&o%faction_name%&a (%format_time%)"),
    koth_start_title("Koth","&a&lKOTH &r&aStarted!"),
    koth_start_subtitle("Koth","&6&o%faction_name%"),
    koth_failed_not_valid_claim("Koth","%prefix% &cThis faction does not have a valid KOTH claim!"),

    warzone_no_permission("Factions","%prefix% &eYou can't do this in the Warzone!"),

    // GUI
    gui_rank_created("Gui","%prefix% &bYou have successfully created rank: &b&o%rank%!"),
    gui_rank_change("Gui","%prefix% &bYou have successfully changed the rank of &b&o%player%&b to: &b&o%rank%!"),
    gui_rank_already_have("Gui","%prefix% &cThis user already have this rank."),
    gui_rename_text("Gui","Enter rank name"),
    gui_rename_faction_text("Gui","Enter faction name"),
    gui_invite_player("Gui","Enter a player name"),
    gui_create_rank_text("Gui","Enter rank name"),
    gui_invalid_type_text("Gui","Invalid name!"),
    gui_bad_word("Gui","Don't use forbidden words."),
    gui_priority_saved("Gui","%prefix% &aYou successfully saved the changes!"),
    rename_faction("Gui","%prefix% §aSuccessfully renamed faction "),
    gui_request_ally("Gui","Enter ally name"),

    // Subclaim "Factions",
    subclaim_created("Subclaims","%prefix% &aSubclaim successfully created!"),
    subclaim_cant_create("Subclaims","%prefix% &cYou can't create a subclaim here!"),
    subclaim_no_access("Subclaims","%prefix% &cAccess denied!"),
    subclaim_lower_rank("Subclaims","%prefix% &cYou must type a lower rank or your rank!"),

    rollback_successfully_rollbacked("Rollback", "%prefix% &aYou successfully setted %player%'s inventory to this!"),
    rollback_dont_have_rollback("Rollback", "%prefix% &cThis player doesn't have saved inventory!"),
    rollback_not_found("Rollback", "%prefix% &cThis ID does not have rollback!"),

    // Hover things
    hover_join("Chat","&7Click here to join!"),
    hover_accept("Chat","&7Click here to accept!"),

    // Staff message
/*    staff_chat_on("%prefix% &aStaff chat enabled!"),
    staff_chat_off("%prefix% &cStaff chat disabled!"),*/

    vanish_enabled("Admin","%prefix% &aVanish enabled!"),
    vanish_disable("Admin","%prefix% &cVanish disabled!"),
    not_in_duty("Admin","%prefix% &cYou are not in staff duty!"),

    // Custom timers
    customt_created("CustomTimers","%prefix% §aCustom timer successfully created!"),
    customt_deleted("CustomTimers","%prefix% §aCustom timer successfully deleted!"),
    customt_edited("CustomTimers","%prefix% §aCustom timer successfully edited!"),
    customt_no_active_timer("CustomTimers","%prefix% §cNo available active timers!"),
    customt_not_found("CustomTimers","%prefix% §cThis timer not found!"),
    already_created("CustomTimers","%prefix% §aThis name already in use!"),


    sotw_start_title("Sotw","SOTW STARTED"),
    sotw_start_subtitle("Sotw","Kawaii"),
    eotw_start_title("Eotw","EOTW STARTED"),
    eotw_start_subtitle("Eotw","UwU"),
    cant_teleport_to_safezone("PvP","%prefix% &cYou can't teleport to a protected zone while you are in PvP tag"),
    missing_argument("Server","%prefix% &cThis command requires &none or more&f&c argument!"),

    coordinates_command(Arrays.asList(
            " ",
            "&7&m-----&2&o Coordinates &7&m-----",
            "&aGlowstone: &f500, 250",
            "&aEnd Portal: &f1000, 1000",
            " "
    )),

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
            "&7│ &ePoints: &a%faction_points%",
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

                        "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),*/

    claim_info_admin(Arrays.asList(
            "%prefix% &6Spawn claiming information:",
            "&7&l» &ePress &6&o[RIGHT] &eclick on the ground, to place one of the positions!", // pos1
            "&7&l» &ePress &6&o[LEFT] &eclick on the ground, to place the other position!", // pos2
            "&eTo accept the claim, press &bShift + Right Click", // right click
            "&eTo discard the claim, press &bShift + Left Click" // left click
    )),
    claim_info(Arrays.asList(
            "%prefix% &6Claiming information:",
            "&7&l» &ePress &6&o[RIGHT] &eclick on the ground, to place one of the positions!", // pos1
            "&7&l» &ePress &6&o[LEFT] &eclick on the ground, to place the other position!", // pos2
            "&eTo accept the claim, press &bShift + Right Click", // right click
            "&eTo discard the claim, press &bShift + Left Click" // left click
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

    private static Map<String, List<String>> sortbykey(HashMap map) {
        // TreeMap to store values of HashMap
        Map<String, List<String>> sorted = new TreeMap<>(map);

        // Copy all data from hashMap into TreeMap
        sorted.putAll(map);

        // Display the TreeMap which is naturally sorted
        return sorted;
    }

    public Messages language(Player p) {
        String language = MessagesTool.getPlayerLanguage(p);
        String prefix = MessagesTool.getLanguageMessages(language).getString("prefix");
        if (this.isString()) {
            String configMessage = MessagesTool.getLanguageMessages(language).getString(this.toString());
            if (configMessage != null) {
                this.playerMessage = ChatColor.translateAlternateColorCodes('&', configMessage.replace("%prefix%", prefix));
                return this;
            } else {}
                //MessagesTool.updateMessageFiles();
        } else {
            List<String> returnList = new ArrayList();
            List<String> configList = MessagesTool.getLanguageMessages(language).getStringList(this.toString());

            if (!configList.isEmpty()) {
                for (String message : configList) {
                    returnList.add(
                            ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", prefix))
                    );
                }
                this.playerListMessages = returnList;
            } else {
                //MessagesTool.updateMessageFiles();
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
    public Messages language(HCFPlayer hcfPlayer) {
        String language = MessagesTool.getPlayerLanguage(hcfPlayer);
        String prefix = MessagesTool.getLanguageMessages(language).getString("prefix");
        if (this.isString()) {
            String configMessage = MessagesTool.getLanguageMessages(language).getString(this.toString());
            if (configMessage != null) {
                this.playerMessage = ChatColor.translateAlternateColorCodes('&', configMessage.replace("%prefix%", prefix));
                return this;
            } else {}
            //MessagesTool.updateMessageFiles();
        } else {
            List<String> returnList = new ArrayList();
            List<String> configList = MessagesTool.getLanguageMessages(language).getStringList(this.toString());

            if (!configList.isEmpty()) {
                for (String message : configList) {
                    returnList.add(
                            ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", prefix))
                    );
                }
                this.playerListMessages = returnList;
            } else {
                //MessagesTool.updateMessageFiles();
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
        if (this.playerMessage == null) {
            return ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", this.thisPrefix));
        }

        String tempMessage = this.playerMessage;
        this.playerMessage = this.message;
        return ChatColor.translateAlternateColorCodes('&', tempMessage.replace("%prefix%", this.thisPrefix));
    }

    public List<String> queueList() {
        List<String> tempList = new ArrayList<>();
        for (String s : this.playerListMessages) {
            tempList.add(ChatColor.translateAlternateColorCodes('&', s.replace("%prefix%", this.thisPrefix)));
        }
        this.playerListMessages = this.listMessages;
        return tempList;
    }

    public Messages setFaction(Faction faction) {
        if (this.isString()) {
            this.playerMessage = this.playerMessage.replaceAll("%faction_name%", faction.getName());
        }

        return this;
    }

    public Messages setFaction(String faction) {
        if (this.isString()) {
            this.playerMessage = this.playerMessage.replaceAll("%faction_name%", faction);
        }
        return this;
    }

    public Messages setNumber(int number) {
        playerMessage = playerMessage.replace("%number%", number + "");
        return this;
    }

    public Messages setRank(String rank) {
        playerMessage = playerMessage.replace("%rank%", rank);
        return this;
    }

    public Messages setTime(String time) {
        playerMessage = playerMessage.replace("%time%", time);
        return this;
    }

    public Messages setType(String type) {
        playerMessage = playerMessage.replace("%type%", type);
        return this;
    }

    public Messages setValue(int value) {
        playerMessage = playerMessage.replace("%value%", value + "");
        return this;
    }

    public Messages setDTR(Faction faction) {
        playerMessage = playerMessage.replace("%dtr%", Formatter.formatDtr(faction));
        return this;
    }

    public Messages setCoords(int x, int y, int z) {
        playerMessage = playerMessage
                .replace("%location_x%", x + "")
                .replace("%location_y%", y + "")
                .replace("%location_z%", z + "");
        return this;
    }

    public Messages setDisplayName(Player p) {
        if (p.getDisplayName() == null) {
            playerMessage = playerMessage.replace("%player_displayname%", p.getName());
        } else {
            playerMessage = playerMessage.replace("%player_displayname%", p.getDisplayName());
        }
        return this;
    }

    public Messages setBardEffects(Player bard, String effect, String members) {
        playerMessage = playerMessage.replace("%bard%", bard.getName()).replace("%effect%", effect).replace("%count%", members);
        return this;
    }


    public Messages setWorld(World world) {
        playerMessage = playerMessage.replace("%world%", world.getName());
        return this;
    }

    public Messages setItem(ItemStack item) {
        playerMessage = playerMessage.replace("%item%", (item.getType().name().charAt(0) + item.getType().name().substring(1).toLowerCase()).replace("_", " "));
        return this;
    }

    public Messages setChat(Player p, ChatTypes chatTypes) {
        String chat;
        switch (chatTypes) {
            case ALLY:
                chat = Messages.ally_chat_channel.language(p).queue();
                break;
            case STAFF:
                chat = Messages.staff_chat_channel.language(p).queue();
                break;
            case LEADER:
                chat = Messages.leader_chat_channel.language(p).queue();
                break;
            case FACTION:
                chat = Messages.faction_chat_channel.language(p).queue();
                break;
            default:
                chat = Messages.public_chat_channel.language(p).queue();
        }
        playerMessage = playerMessage.replace("%chat%", chat);
        return this;
    }

    /**
     * Auto setup from hcfPlayer
     * @param p
     * @return
     */
    public Messages setChat(Player p) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        ChatTypes chatTypes = player.getChatType();
        String chat;
        switch (chatTypes) {
            case ALLY:
                chat = Messages.ally_chat_channel.language(p).queue();
                break;
            case STAFF:
                chat = Messages.staff_chat_channel.language(p).queue();
                break;
            case LEADER:
                chat = Messages.leader_chat_channel.language(p).queue();
                break;
            case FACTION:
                chat = Messages.faction_chat_channel.language(p).queue();
                break;
            default:
                chat = Messages.public_chat_channel.language(p).queue();
        }
        playerMessage = playerMessage.replace("%chat%", chat);
        return this;
    }

    public Messages setLoc(int x, int z) {
        playerMessage = playerMessage.replace("%location%", "X:" + x + " Y:" + z);
        return this;
    }

    /**
     * replacing %location% -> "x, y, z"
     * @param loc
     * @return
     */
    public Messages setLoc(Location loc) {
        playerMessage = playerMessage.replace("%location%", Playertools.formatLocation(loc));
        return this;
    }

    public Messages setPrice(int price) {
        playerMessage = playerMessage.replace("%price%", String.valueOf(price));
        return this;
    }

    public Messages setPage(int page) {
        playerMessage = playerMessage.replace("%page%", String.valueOf(page));
        return this;
    }

    public Messages setNumber(double number) {
        playerMessage = playerMessage.replace("%number%", String.valueOf(number));
        return this;
    }

    public Messages setOnlines(int count) {
        playerMessage = playerMessage.replace("%online%", count + "");
        return this;
    }

    public Messages setExecutor(Player executor) {
        playerMessage = playerMessage.replace("%executor%", executor.getName());
        return this;
    }
    public Messages setExecutor(HCFPlayer executor) {
        playerMessage = playerMessage.replace("%executor%", executor.getName());
        return this;
    }

    public Messages setDeath(Player victim, Player killer) {
        playerMessage = playerMessage.replace("%victim%", victim.getName()).replace("%killer%", killer.getName());
        return this;
    }

    public Messages setDeathWithoutKiller(Player victim) {
        HCFPlayer player = HCFPlayer.getPlayer(victim);
        playerMessage = playerMessage.replace("%victim%", victim.getName())
                .replace("%victim_kills%", player.getPlayerStatistic().kills + "");
        return this;
    }

    public Messages setVictimWithKills(Player victim, Player killer) {
        HCFPlayer hcfKiller = HCFPlayer.getPlayer(killer);
        HCFPlayer hcfVictim = HCFPlayer.getPlayer(victim);
        String weaponName = (killer.getItemInHand().hasItemMeta()) ?
                (killer.getItemInHand().getItemMeta().hasDisplayName()
                        ? killer.getItemInHand().getItemMeta().getDisplayName()
                        : killer.getItemInHand().getType().name().toLowerCase())
                : killer.getItemInHand().getType().name().toLowerCase();

        playerMessage = playerMessage.replace("%victim%", victim.getName())
                .replace("%killer%", killer.getName())
                .replace("%killer_kills%", hcfKiller.getKills() + "")
                .replace("%victim_kills%", hcfVictim.getKills() + "")
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
        playerMessage = playerMessage.replace("%format_time%", String.format("%02d:%02d", MM, SS));
        return this;
    }

    public Messages setZone(String zoneName) {
        playerMessage = playerMessage.replace("%zone%", zoneName);
        return this;
    }

    public void load() {
        SimpleConfig msgs = ConfigManager.getEnglishMessages();

        if (msgs == null) return;

        if (msgs.getString((this.section == null ? "" : this.section + ".") + this) == null) {
            Object obj = this.message;
            if (this.listMessages != null)
                obj = this.listMessages;

            if (this.section == null) {
                if (this.comments == null)
                    msgs.set(this.toString(), obj);
                else
                    msgs.set(this.toString(), obj, this.comments);
            } else {
                if (this.comments == null)
                    msgs.set(section + "." + this, obj);
                else
                    msgs.set(section + "." + this, obj, this.comments);
            }

            save();
            this.thisPrefix = msgs.getString("prefix");
            return;
        }

        this.message = msgs.getString((this.section == null ? "" : this.section + ".") + this);

        this.thisPrefix = msgs.getString("prefix");
    }

    public void loadOthers(SimpleConfig config) {
        SimpleConfig msgs = config;

        if (msgs == null) return;

        if (msgs.getString((this.section == null ? "" : this.section + ".") + this) == null) {
            Object obj = this.message;
            if (this.listMessages != null)
                obj = this.listMessages;

            if (this.section == null) {
                if (this.comments == null)
                    msgs.set(this.toString(), obj);
                else
                    msgs.set(this.toString(), obj, this.comments);
            } else {
                if (this.comments == null)
                    msgs.set(section + "." + this, obj);
                else
                    msgs.set(section + "." + this, obj, this.comments);
            }

            config.saveConfig();
            // save();
        }
    }

    public void save() {
        SimpleConfig msgs = ConfigManager.getEnglishMessages();

        //if(this.comments == null)
        if (this.listMessages == null) {
            if (this.section == null)
                msgs.set(this.toString(), this.message);
            else
                msgs.set(section + "." + this, this.message);
        } else {
            if (this.section == null)
                msgs.set(this.toString(), this.listMessages);
            else
                msgs.set(section + "." + this, this.listMessages);
        }
        /*else
            msgs.set(this.toString(), this.message, this.comments);*/

        ConfigManager.getEnglishMessages().saveConfig();
    }

    private boolean isString() {
        return this.defaultMessage != null;
    }

    public Messages setPlayer(Player p) {
        if (this.isString())
            this.playerMessage = this.playerMessage.replaceAll("%player%", p.getName());
        else replaceList("%player%", p.getName());
        return this;
    }

    public Messages setPlayer(HCFPlayer p) {
        if (this.isString())
            this.playerMessage = this.playerMessage.replaceAll("%player%", p.getName());
        else replaceList("%player%", p.getName());
        return this;
    }

    /**
     * Replacing '%amount%' variable
     */
    public Messages setAmount(String amount) {
        if (this.isString())
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
            if (strings[i].startsWith("%") && strings[i].endsWith("%")) {
                key = strings[i];
                continue;
            }
            if (this.isString())
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
            String totalMembers, String isRaidable, String points) {
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
                    .replace("%faction_points%", points)
                    .replace("%members_online%", onlinesMembers)
                    .replace("%members_count%", totalMembers)
                    .replace("%faction_raidable%", isRaidable)
            );
        }
        playerListMessages = lines;
        return this;
    }
}
