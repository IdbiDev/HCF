package me.idbi.hcf.Tools.Objects;

import com.keenant.tabbed.tablist.TableTabList;
import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.*;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.*;

import static me.idbi.hcf.Tools.HCF_Timer.addPvPTimerCoolDownSpawn;

public class HCFPlayer {

    private static final Connection con = Main.getConnection();

    private UUID uuid;
    @Getter private String name;
    private HCF_Claiming.ClaimTypes claimType;
    private Faction faction;
    private int money;
    private Classes playerClass;
    private boolean freezeStatus;
    private double bardEnergy;
    private HCF_Claiming.Faction_Claim currentArea;
    private int assassinState;
    private boolean inDuty;
    @Getter @Setter private Location stuckLocation;
    private ChatTypes chatType;
    private ArrayList<ChatTypes> toggledChatTypes;
    private int kothId;
    private String language;
    private FactionRankManager.Rank rank;
    private PlayerStatistic playerStatistic;
    private boolean isDeathBanned;
    private int lives;
    private long deathTime;
    private boolean online;
    private TreeMap<Integer, Rollback> rollbacks;
    private TableTabList tabList;

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
            this.freezeStatus = false;
            this.kothId = 0;
            this.toggledChatTypes = new ArrayList<>();
            this.language = language;
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
                    p.getUniqueId(),
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

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInDuty() {
        return this.inDuty;
    }

    public void setDuty(boolean duty) {
        this.inDuty = duty;
    }

    public boolean hasStaffChat() {
        return this.chatType == ChatTypes.STAFF;
    }

    public void setChatType(ChatTypes chatType) {
        this.chatType = chatType;
    }

    public void setFreezeStatus(boolean state) {
        this.freezeStatus = state;
    }

    public void setClass(Classes newClass) {
        this.playerClass = newClass;
    }

    public void setRank(FactionRankManager.Rank rank) {
        this.rank = rank;
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

    public int getMoney() {
        return this.money;
    }

    public void setClaimType(HCF_Claiming.ClaimTypes type) {
        this.claimType = type;
    }

    public Classes getPlayerClass() {
        return this.playerClass;
    }

    public HCF_Claiming.ClaimTypes getClaimType() {
        return this.claimType;
    }

    public void setLocation(HCF_Claiming.Faction_Claim newLoc) {
        this.currentArea = newLoc;
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

    public boolean isFreezed() {
        return this.freezeStatus;
    }

    public void setCurrentArea(HCF_Claiming.Faction_Claim currentArea) {
        this.currentArea = currentArea;
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

    public void setKothId(int id) {
        this.kothId = id;
    }

    public void setAssassinState(int state) {
        this.assassinState = state;
    }

    public Faction getFaction() {
        return this.faction;
    }

    public String getFactionName() {
        return this.faction == null ? "None" : this.faction.name;
    }

    public void takeMoney(int amount) {
        if (this.money - amount < 0) {
            this.money = 0;
            return;
        }

        EconomyResponse r = Main.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(this.uuid), amount);
        this.money = Math.toIntExact(Math.round(r.balance));
    }

    public void setBardEnergy(double energy) {
        this.bardEnergy = energy;
    }

    public void setOnline(boolean online) {
        this.online = online;
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
        this.faction.members.add(this);
    }

    public void removeFaction() {
        setChatType(ChatTypes.PUBLIC);
        this.faction.members.remove(this);
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

    public TreeMap<Integer, Rollback> getRollbacks() {
        return rollbacks;
    }
    public Rollback createRollback(Player p, EntityDamageEvent.DamageCause damageCause, Rollback.RollbackLogType logType) {
        Date date = new Date();
        int id = this.rollbacks.size();
        Rollback rollback = new Rollback(id, p, damageCause, logType, date);
        this.rollbacks.put(id, rollback);
        return rollback;
    }

    public Rollback getRollback(int id) {
        return this.rollbacks.get(id);
    }

    public boolean inFaction() {
        return faction != null;
    }
    public void setDeathTime(long time) {
        this.deathTime = time;
    }
    public long getDeathTime() {
        return this.deathTime;
    }
    public void setDeathBanned(boolean state) {
        this.isDeathBanned = state;
    }
    public boolean isDeathBanned() {
        return this.isDeathBanned;
    }
    public void setLives(int amount) {
        this.lives = amount;
    }
    public void addLives(int amount) {
        this.lives += amount;
    }
    public int getLives() {
        return this.lives;
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
                addPvPTimerCoolDownSpawn(p);
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
            boolean friendly = currentArea.faction == faction;
            boolean isAlly = false;
            if (!friendly) {
                if (faction != null)
                    isAlly = currentArea.faction.isAlly(faction);
            }
            if (friendly) {
                //return Messages.zone_friendly.language(offline.getPlayer()).setZone(currentArea.faction.name).queue();
                return Config.TeammateColor.asStr() + currentArea.faction.name;
            } else {
                if (isAlly) {
                    return Config.AllyColor.asStr() + currentArea.faction.name;
                }
                return Config.EnemyColor.asStr() + currentArea.faction.name;
            }
        }
        return null;
    }

    public void sendChat(String message, ChatTypes chatTypes) {
        if(chatTypes == ChatTypes.PUBLIC || message.startsWith("!")) {
            if(message.equalsIgnoreCase("!")) {
                message = message.substring(1);
            }
            if(this.inFaction()) {
                for (Player onlines : Bukkit.getOnlinePlayers()) {
                    onlines.sendMessage(Messages.chat_prefix_faction.language(onlines)
                            .setFaction(this.faction.name)
                            .setMessage(message)
                            .setPlayer(this).queue());
                }
            } else {
                for (Player onlines : Bukkit.getOnlinePlayers()) {
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
            if (!this.faction.Allies.isEmpty()) {
                for (AllyFaction value : this.faction.Allies.values()) {
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

    public void save() {
        // ToDo: Push to SQL
        if (this.name == null) return;

        SQL_Connection.dbExecute(con,
                "UPDATE members SET faction='?', rank='?', kills='?', deaths='?', money='?', name='?', language = '?',lives='?' WHERE UUID='?'",
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

    public void saveStats() {
        this.playerStatistic.save(this.uuid);
    }

    public FactionRankManager.Rank getRank() {
        return this.rank;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public double getBardEnergy() {
        return bardEnergy;
    }

    public HCF_Claiming.Faction_Claim getCurrentArea() {
        return currentArea;
    }

    public int getAssassinState() {
        return assassinState;
    }

    public ChatTypes getChatType() {
        return chatType;
    }

    /**
     *
     * @return list of disabled chat types
     */
    public ArrayList<ChatTypes> getToggledChatTypes() {
        return toggledChatTypes;
    }

    public int getKothId() {
        return kothId;
    }

    public String getLanguage() {
        return language;
    }

    public PlayerStatistic getPlayerStatistic() {
        return playerStatistic;
    }

    public void setPlayerStatistic(PlayerStatistic playerStatistic) {
        this.playerStatistic = playerStatistic;
    }

    public boolean isOnline() {
        return online;
    }

}
