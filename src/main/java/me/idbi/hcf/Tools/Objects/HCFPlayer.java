package me.idbi.hcf.Tools.Objects;

import com.keenant.tabbed.tablist.TableTabList;
import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Board;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.*;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.*;

public class HCFPlayer {

    private static final Connection con = Main.getConnection();

    // le lehet kérni: "getUUID()"
    private UUID uuid;
    @Getter private String name;

    @Getter private Faction faction;
    @Getter private int money;
    @Getter private ArrayList<ChatTypes> toggledChatTypes;
    @Getter private String language;
    @Getter private PlayerStatistic playerStatistic;
    @Getter private TreeMap<Integer, Rollback> rollbacks;
    @Getter private TableTabList tabList;
    @Getter @Setter private HCF_Claiming.ClaimTypes claimType;
    @Getter @Setter private Classes playerClass;
    @Getter @Setter private boolean freezed;
    @Getter @Setter private double bardEnergy;
    @Getter @Setter private HCF_Claiming.Faction_Claim currentArea;
    @Getter @Setter private int assassinState;
    @Getter @Setter private boolean inDuty;
    @Getter @Setter private Location stuckLocation;
    @Getter @Setter private ChatTypes chatType;
    @Getter @Setter private int kothId;
    @Getter @Setter private FactionRankManager.Rank rank;
    @Getter @Setter private boolean isDeathBanned;
    @Getter @Setter private int lives;
    @Getter @Setter private long deathTime;
    @Getter @Setter private boolean online;
    @Getter private HashMap<Timers, Long> timers;
    @Getter private Board scoreboard;
    @Getter @Setter private boolean isClassWarmup;
    @Getter @Setter private boolean viewMap;
    @Getter private List<HCF_Claiming.Point> factionViewMapLocations;

    public HCFPlayer(UUID uuid,
                     int deaths,
                     int kills,
                     Faction faction,
                     int money,
                     PlayerStatistic playerStatistic,
                     String rankName,
                     String language,
                     long deathTime

    ) {
        try {
            this.uuid = uuid;
            this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
            this.money = money;
            Main.getInstance().playerBank.put(uuid, (double) money);
            this.faction = faction;
            if (this.faction != null) {
                this.rank = this.faction.getRank(rankName);
            }
            this.bardEnergy = 0D;
            this.chatType = ChatTypes.PUBLIC;
            this.inDuty = false;
            this.currentArea = null;
            this.stuckLocation = null;
            this.claimType = HCF_Claiming.ClaimTypes.NONE;
            this.freezed = false;
            this.kothId = 0;
            this.toggledChatTypes = new ArrayList<>();
            this.language = language;
            this.factionViewMapLocations = new ArrayList<>();
            this.playerClass = Classes.NONE;
            this.playerStatistic = playerStatistic;
            this.playerStatistic.kills = kills;
            this.playerStatistic.deaths = deaths;
            this.online = false;
            if(deathTime != 0) {
                this.isDeathBanned = true;
                this.deathTime = deathTime;
            }else{
                this.isDeathBanned = false;
                this.deathTime = 0;
            }
            this.rollbacks = new TreeMap<>();
            this.timers = new HashMap<>();
            this.isClassWarmup = false;
            /*
            SQL_Connection.dbExecute(con, "INSERT INTO members SET name='?',uuid='?',money='?'",
                    this.name, this.uuid.toString(), Config.default_balance.asInt() + "");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HCFPlayer getPlayer(Player p) {
        if (Main.playerCache.containsKey(p.getUniqueId())) {
            return Main.playerCache.get(p.getUniqueId());
        } else {
            //todo: SQL?
            Playertools.loadOnlinePlayer(p);
            /*return new HCFPlayer(
                    p.getUniqueId(),c
                    0,
                    0,
                    null,
                    Config.default_balance.asInt(),
                    new PlayerStatistic(new JSONObject(PlayerStatistic.defaultStats)),
                    null,
                    Config.default_language.asStr()
            );*/
        }

        return getPlayer(p);
    }

    public static HCFPlayer getPlayer(String name) {
        for (HCFPlayer p : Main.playerCache.values()){
            if(p.name.equalsIgnoreCase(name)){
                return p;
            }
        }
        return null;
    }

    public static HCFPlayer getPlayer(UUID uuid) {
        if (Main.playerCache.containsKey(uuid)) {
            return Main.playerCache.get(uuid);
        } else {
            return new HCFPlayer(
                    uuid,
                    0,
                    0,
                    null,
                    Config.DefaultBalance.asInt(),
                    new PlayerStatistic(new JSONObject(PlayerStatistic.defaultStats)),
                    null,
                    Config.DefaultLanguage.asStr(),0
            );
        }

    }

    public static HCFPlayer createPlayer(Player p) {
        return new HCFPlayer(
                p.getUniqueId(),
                0,
                0,
                null,
                Config.DefaultBalance.asInt(),
                new PlayerStatistic(new JSONObject(PlayerStatistic.defaultStats)),
                null,
                Config.DefaultLanguage.asStr(),0
        );
    }

    public void setTabList(TableTabList tabList) {
        this.tabList = tabList;
    }

    public TableTabList getTabList() {
        return this.tabList;
    }

    public boolean hasStaffChat() {
        return this.chatType == ChatTypes.STAFF;
    }

    public void addKills() {
        this.playerStatistic.kills++;
    }

    public void addDeaths() {
        this.playerStatistic.deaths++;
    }

    public void addBardEnergy(double energy) {
        this.bardEnergy += energy;
    }

    public void join() {
        this.playerClass = Classes.NONE;
        this.isClassWarmup = false;
        this.claimType = HCF_Claiming.ClaimTypes.NONE;
        this.toggledChatTypes = new ArrayList<>();
        this.factionViewMapLocations = new ArrayList<>();
        this.viewMap = false;
        this.inDuty = false;
        this.bardEnergy = 0D;
        this.kothId = 0;
    }

    /**
     *
     * @param chatTypes
     * @return New status of chat type. Return true if chat type has been set to enable
     */
    public boolean toggleChat(ChatTypes chatTypes) {
        if(this.toggledChatTypes.contains(chatTypes)) {
            this.toggledChatTypes.remove(chatTypes);
            return true;
        } else {
            this.toggledChatTypes.add(chatTypes);
            return false;
        }
    }

    public boolean isDisabled(ChatTypes chatTypes) {
        return this.toggledChatTypes.contains(chatTypes);
    }

    public void setMoney(int money) {
        Main.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(this.uuid), this.money);
        EconomyResponse r = Main.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(this.uuid), money);
        this.money = Math.toIntExact(Math.round(r.balance));
    }

    public void addMoney(int amount) {
        if (this.money + amount > Integer.MAX_VALUE - 1) {
            this.money = Integer.MAX_VALUE;
            return;
        }
        EconomyResponse r = Main.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(this.uuid), amount);
        System.out.println("Economy response addMoney: " + r.transactionSuccess() + " Error: " + r.errorMessage);
        this.money =  Math.toIntExact(Math.round(r.balance));
    }

    public void addFactionViewMapLocation(HCF_Claiming.Point loc) {
        if(!this.factionViewMapLocations.contains(loc))
            this.factionViewMapLocations.add(loc);
    }

    public void removeFactionViewMapLocation(HCF_Claiming.Point loc) {
        this.factionViewMapLocations.remove(loc);
    }

    public void removeFactionViewMap(Player p) {
        TowerTools.removePillar(p, this.factionViewMapLocations);
    }

    public String getFormattedChatType() {
        if(Bukkit.getPlayer(this.uuid) == null) return "";
        Player p = Bukkit.getPlayer(this.uuid);
        switch (this.chatType) {
            case ALLY -> {
                return Messages.ally_chat_channel.language(p).queue();
            }
            case LEADER -> {
                return Messages.leader_chat_channel.language(p).queue();
            }
            case STAFF -> {
                return Messages.staff_chat_channel.language(p).queue();
            }
            case FACTION -> {
                return Messages.faction_chat_channel.language(p).queue();
            }
            default -> {
                return Messages.public_chat_channel.language(p).queue();
            }
        }
    }

    public void setLanguage(String newLang) {
        this.language = newLang;
        Main.currentLanguages.put(this.uuid, newLang);
    }

    public Rollback getLastRollback() {
        return this.rollbacks.get(this.rollbacks.lastKey());
    }

    public String getFactionName() {
        return this.faction == null ? "None" : this.faction.getName();
    }

    public void takeMoney(int amount) {
        if (this.money - amount < 0) {
            this.money = 0;
            return;
        }

        EconomyResponse r = Main.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(this.uuid), amount);
        this.money = Math.toIntExact(Math.round(r.balance));
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        this.rank = faction.getDefaultRank();
    }

    public void setFactionWithoutRank(Faction faction) {
        this.faction = faction;
    }

    public void addFaction(Faction f) {
        this.faction = f;
        this.rank = f.getDefaultRank();
        this.faction.addMember(this);
    }

    public void removeFaction() {
        setChatType(ChatTypes.PUBLIC);
        this.faction.removeMember(this);
        this.faction = null;
        this.rank = null;
    }

    public int getKills() {
        return this.playerStatistic.kills;
    }

    public void setKills(int kills) {
        this.playerStatistic.kills = kills;
    }

    public int getDeaths() {
        return this.playerStatistic.deaths;
    }

    public void setDeaths(int deaths) {
        this.playerStatistic.deaths = deaths;
    }

    public Rollback createRollback(EntityDamageEvent.DamageCause damageCause, Rollback.RollbackLogType logType) {
        Player p = Bukkit.getPlayer(this.uuid);
        if(p == null) return null;
        boolean isEmpty = true;
        for(ItemStack it : p.getInventory().getContents()) {
            if(isEmpty)
                if(it != null && it.getType() != Material.AIR) {
                    isEmpty = false;
                }
        }
        for(ItemStack it : p.getInventory().getArmorContents()) {
            if(isEmpty)
                if(it != null && it.getType() != Material.AIR) {
                    isEmpty = false;
                }
        }
        if(isEmpty) return null;
        Date date = new Date();
        int id = this.rollbacks.size();
        String cause = "-";
        if(damageCause != null)
            cause = damageCause.name();
        else cause = "NaN";

        Rollback rollback = new Rollback(id, p, cause, logType, date);
        this.rollbacks.put(id, rollback);
        return rollback;
    }

    public Rollback getRollback(int id) {
        return this.rollbacks.get(id);
    }

    public boolean inFaction() {
        return faction != null;
    }
    public void addLives(int amount) {
        this.lives += amount;
    }

    public void banHCFPlayer() {
        this.isDeathBanned = true;
        this.deathTime = System.currentTimeMillis() + Main.deathbanTime;
        if (!Main.deathban) {
            String str = Config.SpawnLocation.asStr();
            Location spawn = new Location(
                    Bukkit.getWorld(Config.WorldName.asStr()),
                    Integer.parseInt(str.split(" ")[0]),
                    Integer.parseInt(str.split(" ")[1]),
                    Integer.parseInt(str.split(" ")[2]),
                    Integer.parseInt(str.split(" ")[3]),
                    Integer.parseInt(str.split(" ")[4])
            );
            Player p = Bukkit.getPlayer(this.uuid);
            if(p != null) {
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setFallDistance(0);
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);
                Timers.PVP_TIMER.add(p);
                this.isDeathBanned = false;
                this.deathTime = 0L;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.teleport(spawn);
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 2L);
            }

        } else {
            Player p = Bukkit.getPlayer(this.uuid);
            if(this.online || p != null) {
                p.kickPlayer(Messages.deathban_kick.language(p).setFormattedTime((int) ((Main.deathbanTime / 1000))).queue());
            }
           BanHandler.banPlayerInHCF(this);
        }
    }
    /**
     * Get location with color
     *
     * @return String formatted Zone name
     */
    public String getLocationFormatted() {
        OfflinePlayer offline = Bukkit.getOfflinePlayer(this.uuid);
        if (offline.isOnline()) {
            if (currentArea == null) {
                return Messages.wilderness.language(offline.getPlayer()).queue();
            }
            boolean friendly = currentArea.getFaction() == faction;
            boolean isAlly = false;
            if (!friendly) {
                if (faction != null)
                    isAlly = currentArea.getFaction().isAlly(faction);
            }
            if (friendly) {
                //return Messages.zone_friendly.language(offline.getPlayer()).setZone(currentArea.faction.name).queue();
                return Config.TeammateColor.asStr() + currentArea.getFaction().getName();
            } else {
                if (isAlly) {
                    return Config.AllyColor.asStr() + currentArea.getFaction().getName();
                }
                return Config.EnemyColor.asStr() + currentArea.getFaction().getName();
            }
        }
        return null;
    }

    public void sendChat(String message, ChatTypes chatTypes) {
        if(isDisabled(chatTypes)) {
            Player p = Bukkit.getPlayer(this.uuid);
            if(p == null) return;
            p.sendMessage(Messages.cant_send_message_to_channel.language(p).queue());
            return;
        }
        if(chatTypes == ChatTypes.PUBLIC || message.startsWith("!")) {
            if(message.equalsIgnoreCase("!")) {
                message = message.substring(1);
            }
            if(this.inFaction()) {
                for (Player onlines : Bukkit.getOnlinePlayers()) {
                    HCFPlayer hcfPlayer = HCFPlayer.getPlayer(onlines);
                    if(hcfPlayer.isDisabled(ChatTypes.PUBLIC)) continue;
                    onlines.sendMessage(Messages.chat_prefix_faction.language(onlines)
                            .setFaction(this.faction.getName())
                            .setMessage(message)
                            .setPlayer(this).queue());
                }
            } else {
                for (Player onlines : Bukkit.getOnlinePlayers()) {
                    HCFPlayer hcfPlayer = HCFPlayer.getPlayer(onlines);
                    if(hcfPlayer.isDisabled(ChatTypes.PUBLIC)) continue;
                    onlines.sendMessage(Messages.chat_prefix_without_faction.language(onlines)
                            .setMessage(message)
                            .setPlayer(this).queue());
                }
            }
        }
        else if(chatTypes == ChatTypes.STAFF) {
            for (Player onlines : Bukkit.getOnlinePlayers()) {
                HCFPlayer hcfOnline = HCFPlayer.getPlayer(onlines);
                if(hcfOnline.isDisabled(ChatTypes.STAFF)) continue;
                if(onlines.hasPermission("factions.admin")) {
                    onlines.sendMessage(Messages.staff_chat.language(onlines)
                            .setMessage(message)
                            .setPlayer(this).queue());
                }
            }
        }
        else if(chatTypes == ChatTypes.FACTION) {
            if(!this.inFaction()) {
                return;
            }
            for (Player member : this.faction.getOnlineMembers()) {
                HCFPlayer hcfMember = HCFPlayer.getPlayer(member);
                if(hcfMember.isDisabled(ChatTypes.FACTION)) continue;
                member.sendMessage(Messages.faction_chat.setFaction(this.faction).setPlayer(this).setMessage(message).setRank(this.faction.getName()).queue());
            }
        }
        else if(chatTypes == ChatTypes.LEADER) {
            if(!this.inFaction()) {
                return;
            }
            for (Player member : this.faction.getOnlineMembers()) {
                HCFPlayer hcfMember = HCFPlayer.getPlayer(member);
                if(hcfMember.isDisabled(ChatTypes.LEADER)) continue;
                if(hcfMember.getRank().hasPermission(FactionRankManager.Permissions.MANAGE_ALL) || hcfMember.getRank().isLeader()) {
                    member.sendMessage(Messages.leader_chat.setPlayer(this).setMessage(message).queue());
                }
            }
        }
        else if(chatTypes == ChatTypes.ALLY) {
            if (!this.inFaction()) {
                return;
            }
            if (!this.faction.getAllies().isEmpty()) {
                for (AllyFaction value : this.faction.getAllies().values()) {
                    for (Player member : value.getAllyFaction().getOnlineMembers()) {
                        HCFPlayer hcfMember = HCFPlayer.getPlayer(member);
                        if (hcfMember.isDisabled(ChatTypes.ALLY)) continue;
                        member.sendMessage(Messages.ally_chat.setMessage(message).setFaction(this.faction).setPlayer(this).queue());
                    }
                }
                for (Player member : this.faction.getOnlineMembers()) {
                    HCFPlayer hcfMember = HCFPlayer.getPlayer(member);
                    if (hcfMember.isDisabled(ChatTypes.ALLY)) continue;
                    member.sendMessage(Messages.ally_chat.setMessage(message).setFaction(this.faction).setPlayer(this).queue());
                }
            }
        }
    }

    public void createScoreboard(Player p) {
        this.scoreboard = new Board(p);
    }

    public void removeScoreboard() {
        this.scoreboard = null;
    }

    // kell mert a @Getter function "getUuid()" szarul néz ki.
    public UUID getUUID() {
        return this.uuid;
    }

    public void save() {
        // ToDo: Push to SQL
        if (this.name == null) return;

        SQL_Connection.dbExecute(con,
                "UPDATE members SET faction='?', rank='?', kills='?', deaths='?', money='?', name='?', language = '?', lives='?' WHERE UUID='?'",
                this.faction == null ? 0 + "" : this.faction.getId() + "",
                this.rank == null ? "None" : this.rank.getName(),
                this.playerStatistic.kills + "",
                this.playerStatistic.deaths + "",
                this.money + "",
                this.name,
                this.language,
                this.lives+"",
                this.uuid.toString());
        saveStats();
    }
    public void saveSync() {
        // ToDo: Push to SQL
        if (this.name == null) return;

        SQL_Connection.dbSyncExec(con,
                "UPDATE members SET faction='?', rank='?', kills='?', deaths='?', money='?', name='?', language = '?', lives='?' WHERE UUID='?'",
                this.faction == null ? 0 + "" : this.faction.getId() + "",
                this.rank == null ? "None" : this.rank.getName(),
                this.playerStatistic.kills + "",
                this.playerStatistic.deaths + "",
                this.money + "",
                this.name,
                this.language,
                this.lives+"",
                this.uuid.toString());
        saveStatsSync();
    }

    public void saveStats() {
        this.playerStatistic.save(this.uuid);
    }
    public void saveStatsSync() {
        this.playerStatistic.saveSync(this.uuid);
    }

    public void addTimer(Timers timer, long time) {
        this.timers.put(timer, time);
    }

    public void removeTimer(Timers timer) {
        this.timers.remove(timer);
    }
}
