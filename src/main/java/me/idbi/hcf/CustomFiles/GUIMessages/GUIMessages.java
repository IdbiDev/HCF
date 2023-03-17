package me.idbi.hcf.CustomFiles.GUIMessages;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import me.idbi.hcf.CustomFiles.MessagesTool;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.Objects.Faction;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public enum GUIMessages {

    back_button("&cBack", List.of()),
    leave_button("&cLeave", Arrays.asList(" ", "&7Click here to close the menu!")),
    save_button("&aSave All Changes", Arrays.asList(" ", "&7Click here to save the changes!")),
    discard_button("&cDiscard All Changes", Arrays.asList(" ", "&7Click here to discard the changes!")),
    member_head("&e%player%", List.of()),
    faction_rename("&eRename Faction", Arrays.asList("&5", "&7Click here to rename your faction!")),
    faction_histories("&eFaction Histories", Arrays.asList("&5", "&7Click here to see the histories!")),
    faction_member_manager("&eManage Players", Arrays.asList("&5", "&7Click here to manage members!")),
    faction_rank_manager("&eManage Ranks", Arrays.asList("&5", "&7Click here to manage ranks!")),
    faction_invite_manager("&eInvite Manager", Arrays.asList("&5", "&7Click here to manage the invites!")),
    faction_invite_player("&eInvite Player", Arrays.asList("&5", "&7Click here to invite a player!")),
    faction_invited_players("&eInvited Players", Arrays.asList("&5", "&7Click here to show the invited players!")),
    faction_uninvite_player("&e%player%", Arrays.asList("&5", "&7Click here to &euninvite %player%&7!")),
    faction_player_rank_manager("&eManage Player's Rank", Arrays.asList("&5", "&7Click here to manage rank!")),
    faction_kick_player("&eKick Player", Arrays.asList("&5", "&7Click here to kick player!")),
    manage_rank("&e%rank%", Arrays.asList("&5", "&7Click here to manage rank!")),
    create_rank("&aCreate a New Rank", Arrays.asList("&5", "&7Click here to create a rank!")),
    rename_rank("&eRename Rank", Arrays.asList("&5", "&7Click here to rename rank!")),
    rank_permission_manager("&ePermission Manager", Arrays.asList("&5", "&7Click here to manage permissions!")),
    delete_rank("&cDelete Rank", Arrays.asList("&5", "&7Click here to delete rank!")),
    faction_ranks("&a%rank%", List.of()),
    priority_toggle_button("&ePriority Manage", Arrays.asList("&5", "&7Click here to change the order!")),
    rank_manager_toggle_button("&eRank Manager", Arrays.asList("&5", "&7Click here to to go back!")),
    rank_priority_selected("", Arrays.asList("&5", "&aSELECTED!", "&7Click on another rank to swap their order!")),

    rollback_information("&e%player% &6#%id%", Arrays.asList(
            "&5",
            "&eDate &8» &6%date%",
            "&eDamage Cause &8» &6%damage_cause%",
            "&eEXP Level &8» &6%exp_level%",
            "&eType &8» &6%type%",
            "&eRolled Back &8» &6%rolled%",
            "&5",
            "&aClick here to view!",
            "&aDROP item to rollback!"
    )),

    faction_stats("&2&o%faction_name%", Arrays.asList(
            "&7┌──",
            "&7│ &aDTR &7/ &aMax DTR: &f%faction_dtr% &7/ &f%faction_dtr_max%",
            "&7│ &aDTR Regen: &f%faction_dtr_regen%",
            "&7│ &aBalance: &f$%faction_balance%",
            "&7│ &aMember Count: &f%faction_member_online%&7/&f%faction_member_count%",
            "&7│ &aHome Location: &f%faction_home%",
            "&7├──",
            "&7│ &aKills: &f%faction_kills%",
            "&7│ &aDeaths: &f%faction_deaths%",
            "&7└──"
    ));

    public String name, defaultName, tempName;
    public List<String> lore, defaultLore, tempLore;

    GUIMessages(String name, List<String> lore) {
        this.name = name;
        this.defaultName = name;
        this.tempName = name;
        this.lore = lore;
        this.defaultLore = lore;
        this.tempLore = lore;
    }

    public GUIMessages language(Player p) {
        String language = MessagesTool.getPlayerLanguage(p);
        String configMessage = MessagesTool.getGUILanguageMessages(language).getString(this + ".name");
        if (configMessage != null) {
            this.tempName = ChatColor.translateAlternateColorCodes('&', configMessage);
            return this;
        } else
            MessagesTool.updateGuiMessageFiles();

        List<String> returnList = new ArrayList<>();
        List<String> configList = MessagesTool.getGUILanguageMessages(language).getStringList(this + ".lore");

        if (!configList.isEmpty()) {
            for (String message : configList) {
                returnList.add(
                        ChatColor.translateAlternateColorCodes('&', message)
                );
            }
        } else {
            MessagesTool.updateGuiMessageFiles();
            for (String message : this.lore) {
                returnList.add(
                        ChatColor.translateAlternateColorCodes('&', message)
                );
            }
        }
        this.tempLore = returnList;

//,        this.tempL = ChatColor.translateAlternateColorCodes('&', this.message);
        return this;
    }

    public String getName() {
        if (this.tempName == null) {
            return ChatColor.translateAlternateColorCodes('&', name);
        }

        final String tempMessage = this.tempName;
        this.tempName = this.name;
        this.tempLore = this.lore;
        return ChatColor.translateAlternateColorCodes('&', tempMessage);
    }

    public GUIMessages setFaction(Faction f) {
        this.tempName = this.tempName.replace("%faction_name%", f.getName());
        this.tempLore = replaceLore("%faction_name%", f.getName());
        return this;
    }

    public GUIMessages setRank(String rankName) {
        this.tempName = this.tempName.replace("%rank%", rankName);
        this.tempLore = replaceLore("%rank%", rankName);
        return this;
    }

    public GUIMessages setPlayerName(Player p) {
        this.tempName = this.tempName.replace("%player%", p.getName());
        this.tempLore = replaceLore("%player%", p.getName());
        return this;
    }

    public GUIMessages setId(int id) {
        this.tempName = this.tempName.replace("%id%", id + "");
        this.tempLore = replaceLore("%id%", id + "");
        return this;
    }

    public GUIMessages setPlayerName(String name) {
        this.tempName = this.tempName.replace("%player%", name);
        this.tempLore = replaceLore("%player%", name);
        return this;
    }

    public GUIMessages setDate(Date name) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        this.tempName = this.tempName.replace("%date%", formatter.format(date));
        this.tempLore = replaceLore("%date%", formatter.format(date));
        return this;
    }

    public GUIMessages setEXPLevel(String level) {
        this.tempName = this.tempName.replace("%exp_level%", level);
        this.tempLore = replaceLore("%exp_level%", level);
        return this;
    }

    public GUIMessages setRolled(boolean rolledBack) {
        this.tempName = this.tempName.replace("%rolled%", rolledBack + "");
        this.tempLore = replaceLore("%rolled%", rolledBack + "");
        return this;
    }

    public GUIMessages setLogType(Rollback.RollbackLogType logType) {
        this.tempName = this.tempName.replace("%type%", logType.name());
        this.tempLore = replaceLore("%type%", logType.name());
        return this;
    }

    public GUIMessages setDamageCause(EntityDamageEvent.DamageCause damageCause) {
        this.tempName = this.tempName.replace("%damage_cause%", damageCause.name());
        this.tempLore = replaceLore("%damage_cause%", damageCause.name());
        return this;
    }

    public List<String> getLore() {
        final List<String> tempList = new ArrayList<>();
        for (String s : this.tempLore.isEmpty() ? this.lore : this.tempLore) {
            tempList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        this.tempName = this.name;
        this.tempLore = this.lore;
        return tempList;
    }

    public List<String> replaceLore(String... strings) {
        List<String> outputList = new ArrayList<>();
        for (String lore : this.tempLore) {
            outputList.add(replace(lore, strings));
        }

        return outputList;
    }

    public String replace(String s, String... strings) {
        String key = "";
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].startsWith("%") && strings[i].endsWith("%")) {
                key = strings[i];
                continue;
            }
            s = s.replace(key, strings[i]);
            key = "";
        }

        return s;
    }

    public void save() {
        SimpleConfig msgs = ConfigManager.getGUIEnglishMessages();

        if (this.toString() == null) {
            msgs.set(this + ".name", this.name);
            msgs.set(this + ".lore", this.lore);
        }
        ConfigManager.getGUIEnglishMessages().saveConfig();
    }

    public void load() {
        SimpleConfig msgs = ConfigManager.getGUIEnglishMessages();

        if (msgs == null) return;

        if (msgs.getString(this.toString()) == null) {
            msgs.set(this + ".name", this.name);
            msgs.set(this + ".lore", this.lore);
            save();
            return;
        }

        this.name = msgs.getString(this + ".name");
        this.lore = msgs.getStringList(this + ".lore");
    }

    public void loadOthers(SimpleConfig config) {
        SimpleConfig msgs = config;

        if (msgs == null) return;

        if (msgs.getString(this.toString()) == null) {
            msgs.set(this + ".name", this.name);
            msgs.set(this + ".lore", this.lore);
            config.saveConfig();
            return;
        }

        this.name = msgs.getString(this + ".name");
        this.lore = msgs.getStringList(this + ".lore");

        config.saveConfig();
    }

    public GUIMessages setupShow(
            String factionName, String factionStatus, String leaderName, String factionBalance, String factionKills, String factionDeaths,
            String factionPos, String factionDtr, String factionDtrRegen, String dtrMax, String onlinesMembers,
            String totalMembers, String isRaidable) {
        List<String> lines = new ArrayList<>();
        for (String line : this.tempLore) {
            lines.add(line
                    .replace("%faction_name%", factionName)
                    .replace("%faction_status%", factionStatus)
                    .replace("%leader_name%", leaderName)
                    .replace("%faction_balance%", factionBalance)
                    .replace("%faction_kills%", factionKills)
                    .replace("%faction_deaths%", factionDeaths)
                    .replace("%faction_home%", (factionPos.equals("0, 0") ? "None" : factionPos))
                    .replace("%faction_dtr_max%", dtrMax)
                    .replace("%faction_dtr_regen%", factionDtrRegen)
                    .replace("%faction_dtr%", factionDtr)
                    .replace("%faction_member_online%", onlinesMembers)
                    .replace("%faction_member_count%", totalMembers)
                    .replace("%faction_raidable%", isRaidable)
            );
        }
        this.tempLore = lines;
        return this;
    }
}
