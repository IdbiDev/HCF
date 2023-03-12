package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.*;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static me.idbi.hcf.Tools.HCF_Timer.addPvPTimerCoolDownSpawn;

public class HCFPlayer {

    private static final Connection con = Main.getConnection();

    public UUID uuid;
    public String name;
    public HCF_Claiming.ClaimTypes claimType;
    public Faction faction;
    public int money;
    public Classes playerClass;
    public boolean freezeStatus;
    public double bardEnergy;
    public HCF_Claiming.Faction_Claim currentArea;
    public int assassinState;
    public boolean inDuty;
    public Location stuckLocation;
    public boolean factionChat;
    public int kothId;
    public String language;
    public boolean staffChat;
    public FactionRankManager.Rank rank;
    public PlayerStatistic playerStatistic;
    public boolean isDeathBanned;
    public int lives;
    public long deathTime;
    public boolean online;

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
            this.factionChat = false;
            this.staffChat = false;
            this.inDuty = false;
            this.currentArea = null;
            this.stuckLocation = null;
            this.claimType = HCF_Claiming.ClaimTypes.NONE;
            this.freezeStatus = false;
            this.kothId = 0;
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

    public void setDuty(boolean duty) {
        this.inDuty = duty;
    }

    public void setFactionChat(boolean chat) {
        this.factionChat = chat;
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

    public void setClaimType(HCF_Claiming.ClaimTypes type) {
        this.claimType = type;
    }

    public void setLocation(HCF_Claiming.Faction_Claim newLoc) {
        this.currentArea = newLoc;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int amount) {
        if (this.money + amount > Integer.MAX_VALUE - 1) {
            this.money = Integer.MAX_VALUE;
            return;
        }
        this.money += amount;
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

    public String getFactionName() {
        return this.faction == null ? "None" : this.faction.name;
    }

    public void takeMoney(int amount) {
        if (this.money - amount < 0) {
            this.money = 0;
            return;
        }
        this.money -= amount;
    }

    public void setBardEnergy(double energy) {
        this.bardEnergy = energy;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        this.rank = faction.getDefaultRank();
    }

    public void setFactionWithoutRank(Faction faction) {
        this.faction = faction;
    }

    public void setStuckLocation(Location loc) {
        this.stuckLocation = loc;
    }

    public void setStaffChat(boolean chat) {
        this.staffChat = chat;
    }

    public void addFaction(Faction f) {
        this.faction = f;
        this.rank = f.getDefaultRank();
        this.faction.members.add(this);
    }

    public void removeFaction() {
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

    public void save() {
        // ToDo: Push to SQL
        if (this.name == null) return;

        SQL_Connection.dbExecute(con,
                "UPDATE members SET faction='?', rank='?', kills='?', deaths='?', money='?', name='?', language = '?',lives='?' WHERE UUID='?'",
                this.faction == null ? 0 + "" : this.faction.id + "",
                this.rank == null ? "None" : this.rank.name,
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
}
