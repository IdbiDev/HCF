package me.idbi.hcf.MessagesEnums;

//import com.sun.xml.internal.stream.StaxErrorReporter;

import me.idbi.hcf.CustomFiles.ConfigManager;
import me.idbi.hcf.CustomFiles.MessagesFile;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public enum ListMessages {

    FACTION_SHOW(Arrays.asList(
            "&8&m        &c %faction_name% &f[%faction_status%&f] &8&m        ",
            "&7&l✣ &eLeader: &a%leader_name%",
            "&6Tagok:",
            "%members_list_categories_and_members%",
            " ",
            "&7┌ &eSzámla: &a$%faction_balance%",
            "&7├ &eÖlések: &a%faction_kills%",
            "&7├ &eHalálok: &a%faction_deaths%",
            "&7├ &ePozíció: &a%faction_pos%",
            "&7└ &eDTR: &a%faction_dtr%"
    )),

    CLAIM_INFO(Arrays.asList(
            "%prefix% &6Claiming information:",
            "&7&l» &ePress &6&o[RIGHT] &eclick on the ground, to place one of the positions!", // pos1
            "&7&l» &ePress &6&o[LEFT] &eclick on the ground, to place the other position!", // pos2
            "&eTo accept the claim, press &6&oSHIFT + RIGHT &eclick!", // right click
            "&eTo discard the claim, press &6&oSHIFT + LEFT &eclick!" // left click
    )),

    COMMAND_LIST(Arrays.asList(
            "§9/f create §7- Create your faction",
            "§9/f show [Faction] §7- Show faction",
            "§9/f claim §7- ",
            "§9/f invite <Player> §7- Invite player to your faction",
            "§9/f join §7- Join to a faction",
            "§9/f leave §7- Leave from your faction",
            "§9/f deposit <Amount> §7- Deposit money to your faction bank",
            "§9/f withdraw <Amount> §7- Withdraw money from faction bank",
            "§9/f sethome §7- Sets a home to your faction",
            "§9/f home §7- Teleport to faction's home",
            "§9/f reload [file] §7- Reload files"
    ));

    private List<String> list, defaultList;
    private List<String> message;

    ListMessages(List<String> list) {
        this.list = list;
        this.defaultList = list;
        this.message = new ArrayList<>();
    }

    public List<String> getMessageList() {
        List<String> returnString = new ArrayList<>();

        for (String str : list) {
            returnString.add(ChatColor.translateAlternateColorCodes('&', str));
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

                    if(category.getKey().equalsIgnoreCase("Leader")) continue;

                    listike.add(Messages.CATEGORY_DESIGN.queue()
                            .replace("%category%",
                                    category.getKey().substring(0, 1).toUpperCase() + category.getKey().substring(1)));

                    for (Map.Entry<String, String> player : players.entrySet()) {
                        if (category.getValue().contains(player.getKey())) { // player a rankban!!!
//                            if(Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(Bukkit.getPlayer(player.getKey()), "factionid")))
//                                    .player_ranks.get(Bukkit.getPlayer(player.getKey())).isLeader)
                            if (Bukkit.getPlayer(player.getKey()) != null) { // player online!!
                                listike.add(Messages.MEMBER_LIST_DESIGN_ONLINE.queue()
                                        .replace("%member%", player.getKey())
                                        .replace("%member_kill%", player.getValue()));
                            } else {
                                listike.add(Messages.MEMBER_LIST_DESIGN_OFFLINE.queue()
                                        .replace("%member%", player.getKey())
                                        .replace("%member_kill%", player.getValue()));
                            }
                        }
                    }
                }
            }
            if (!line.contains("%members_list_categories_and_members%")) {
                listike.add(line);
            }
        }
        this.message = listike;
        return this;
    }

    public ListMessages setupShow (
            String factionName, String factionStatus, String leaderName, String factionBalance, String factionKills, String factionDeaths, String factionPos, String factionDtr) {
        List<String> lines = new ArrayList<>();
        for(String line : list) {
            lines.add(line
                    .replace("%faction_name%", factionName)
                    .replace("%faction_status%", factionStatus)
                    .replace("%leader_name%", leaderName)
                    .replace("%faction_balance%", factionBalance)
                    .replace("%faction_kills%", factionKills)
                    .replace("%faction_deaths%", factionDeaths)
                    .replace("%faction_pos%", factionPos)
                    .replace("%faction_dtr%", factionDtr));
        }
        message = lines;
        return this;
    }

    public List<String> queue() {
        List<String> returnString = new ArrayList<>();

        for(String str : message) {
            returnString.add(ChatColor.translateAlternateColorCodes('&', str).replace("%prefix%", Messages.PREFIX.queue()));
        }

        return returnString;
    }

    public List<String> getDefaultMessageList() {
        List<String> returnString = new ArrayList<>();

        for(String str : defaultList) {
            returnString.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        return returnString;
    }

    public void save() {
        FileConfiguration msgs = MessagesFile.getMessages();

        msgs.set(this.toString(), list);

        MessagesFile.saveMessages();
    }

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

    private static Map<String, List<String>> sortbykey(HashMap map) {
        // TreeMap to store values of HashMap
        Map<String, List<String>> sorted = new TreeMap<>(map);

        // Copy all data from hashMap into TreeMap
        sorted.putAll(map);

        // Display the TreeMap which is naturally sorted
        return sorted;
    }
}
