package me.idbi.hcf.MessagesEnums;

//import com.sun.xml.internal.stream.StaxErrorReporter;

import me.idbi.hcf.CustomFiles.ConfigManager;
import me.idbi.hcf.CustomFiles.MessagesFile;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public enum ListMessages {

    FACTION_SHOW(Arrays.asList(
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

    CLAIM_INFO(Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
             "&eShift + Left click",
            "&f➥ Discard the claim."
    )),

    SPAWN_CLAIM(Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),

    KOTH_CLAIM(Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),
    CLAIM_INFO_ADMIN(Arrays.asList(
            "%prefix% &6Spawn claiming information:",
            "&7&l» &ePress &6&o[RIGHT] &eclick on the ground, to place one of the positions!", // pos1
            "&7&l» &ePress &6&o[LEFT] &eclick on the ground, to place the other position!", // pos2
            "&eTo accept the claim, use &b&n/a spawnclaim claim", // right click
            "&eTo discard the claim, use &c&n/a spawnclaim end" // left click
    )),

    COMMAND_LIST(Arrays.asList(
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
    KOTH_COMMAND_LIST(Arrays.asList(
            "§9/koth create <Name> §7- Create the koth",
            "§9/koth setcapturezone <Name> §7- Claim the koth place (CAPTURE ZONE)",
            "§9/koth setnatrualzone <Name> §7- Claim the koth place (NEUTRAL ZONE)",
            "§9/koth setreward §7- Set the koth reward with GUI"

    )),
    ADMIN_COMMAND_LIST(Arrays.asList(
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

    private List<String> list;
    private final List<String> defaultList;
    private List<String> message;

    ListMessages(List<String> list) {
        this.list = list;
        this.defaultList = list;
        this.message = new ArrayList<>();
    }

    private static Map<String, List<String>> sortbykey(HashMap map) {
        // TreeMap to store values of HashMap
        Map<String, List<String>> sorted = new TreeMap<>(map);

        // Copy all data from hashMap into TreeMap
        sorted.putAll(map);

        // Display the TreeMap which is naturally sorted
        return sorted;
    }

    public List<String> getMessageList() {
        List<String> returnString = new ArrayList<>();

        for (String str : list) {
            returnString.add(ChatColor.translateAlternateColorCodes('&', str.replace("%prefix%", Messages.PREFIX.queue())));
        }

        return returnString;
    }

    //                                rank name     playerek a rankban        player name     player kills
    public ListMessages setMembers(HashMap<String, List<String>> categories, HashMap<String, String> players) {
        List<String> listike = new ArrayList<>();

        // Sorted hashMap
        Map<String, List<String>> sortedMap = sortbykey(categories);

        for (String line : message) {
            if (line.equalsIgnoreCase("%members_list_categories_and_members%")) {
                for (Map.Entry<String, List<String>> category : sortedMap.entrySet()) {

                    if (category.getKey().equalsIgnoreCase("Leader")) continue;

                    listike.add(Messages.CATEGORY_DESIGN.queue()
                            .replace("%category%",
                                    category.getKey().substring(0, 1).toUpperCase() + category.getKey().substring(1)));

                    List<String> playersList = new ArrayList<>();
                    for (Map.Entry<String, String> player : players.entrySet()) {
                        if (category.getValue().contains(player.getKey())) { // player a rankban!!!
//                            if(Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(Bukkit.getPlayer(player.getKey()), "factionid")))
//                                    .player_ranks.get(Bukkit.getPlayer(player.getKey())).isLeader)
                            if (Bukkit.getPlayer(player.getKey()) != null) { // player online!!
                                playersList.add(Messages.MEMBER_LIST_DESIGN_ONLINE.queue()
                                        .replace("%member%", player.getKey())
                                        .replace("%member_kill%", player.getValue()));
                            } else {
                                playersList.add(Messages.MEMBER_LIST_DESIGN_OFFLINE.queue()
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
        this.message = listike;
        return this;
    }

    public ListMessages setupShow(
            String factionName, String factionStatus, String leaderName, String factionBalance, String factionKills, String factionDeaths,
            String factionPos, String factionDtr, String factionDtrRegen, String dtrMax, String onlinesMembers, String totalMembers, String isRaidable) {
        List<String> lines = new ArrayList<>();
        for (String line : list) {
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
        message = lines;
        return this;
    }

    public List<String> queue() {
        List<String> returnString = new ArrayList<>();

        for (String str : list) {
            returnString.add(ChatColor.translateAlternateColorCodes('&', str).replace("%prefix%", Messages.PREFIX.queue()));
        }

        return returnString;
    }

    public List<String> queueShow() {
        List<String> returnString = new ArrayList<>();

        for (String str : message) {
            returnString.add(ChatColor.translateAlternateColorCodes('&', str).replace("%prefix%", Messages.PREFIX.queue()));
        }

        return returnString;
    }

    public List<String> getDefaultMessageList() {
        List<String> returnString = new ArrayList<>();

        for (String str : defaultList) {
            returnString.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        return returnString;
    }

    public void save() {
        FileConfiguration msgs = MessagesFile.getMessages();

        msgs.set(this.toString(), list);

        MessagesFile.saveMessages();
    }

//    private static HashMap sort(HashMap map) {
//        List linkedlist = new LinkedList(map.entrySet());
//        Collections.sort(linkedlist, new Comparator() {
//            public int compare(Object o1, Object o2) {
//                return ((Comparable) ((Map.Entry) (o1)).getValue())
//                        .compareTo(((Map.Entry) (o2)).getValue());
//            }
//        });
//        HashMap sortedHashMap = new LinkedHashMap();
//        for (Iterator it = linkedlist.iterator(); it.hasNext();) {
//            Map.Entry entry = (Map.Entry) it.next();
//            sortedHashMap.put(entry.getKey(), entry.getValue());
//        }
//        return sortedHashMap;
//    }

    public void load() {
        FileConfiguration msgs = ConfigManager.getManager().getListMessage();

        if (msgs == null) return;

        if (msgs.getString(this.toString()) == null) {
            msgs.set(this.toString(), list);
            save();
            return;
        }

        list = msgs.getStringList(this.toString());
    }
}
