package me.idbi.hcf.Scoreboard;

import lombok.Getter;
import me.idbi.hcf.CustomFiles.BoardFile;
import me.idbi.hcf.Main;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class BoardManager {

   @Getter private boolean scoreboardEnabled;
   @Getter private boolean linesEnabled;
   @Getter private String line;
   @Getter private String title;
   @Getter private boolean titleChanger;
   @Getter private int titleChangerTicks;
   @Getter private List<String> titleChangeLines;
   @Getter private boolean footerEnabled;
   @Getter private int footerChangeTicks;
   @Getter private List<String> footer;
   @Getter private List<String> footerLines;

   @Getter private String deathbanInfoTime;
   @Getter private String deathbanInfoLives;

   @Getter private String sotw;
   @Getter private String sotwOff;
   @Getter private String location;
   @Getter private String pvpTimer;
   @Getter private String combatTag;
   @Getter private String enderPearl;
   @Getter private String apple;
   @Getter private String gapple;
   @Getter private String stuck;
   @Getter private String home;
   @Getter private String archerTag;
   @Getter private String warmup;
   @Getter private String logout;
   @Getter private String activeClass;
   @Getter private String koth;
   @Getter private String spawn;
   @Getter private String globalAbilities;
   @Getter private String bardEnergy;
   @Getter private String bardCooldown;
   @Getter private boolean teamFocusEnabled;
   @Getter private List<String> teamFocusLines;
   @Getter private boolean staffModeEnabled;
   @Getter private List<String> staffModeLines;
   @Getter private String customTimer;

   private Main plugin;
   private static BoardManager manager;

   public BoardManager(Main plugin) {
      this.plugin = plugin;
      this.manager = this;
   }

   public void setup() {
      scoreboardEnabled = getBoolean("scoreboard_info.enabled");
      title = get("scoreboard_info.title");
      line = get("scoreboard_info.lines");
      linesEnabled = getBoolean("scoreboard_info.line_enabled");

      titleChanger = getBoolean("title_config.changer_enabled");
      titleChangerTicks = getInt("title_config.changer_ticks");
      titleChangeLines = getStringList("title_config.changes");

      footerEnabled = getBoolean("footer.enabled");
      footer = getStringList("footer.lines");
      footerChangeTicks = getInt("footer_config.changer_ticks");
      footerLines = getStringList("footer_config.changes");

      deathbanInfoTime = get("deathban_info.time");
      deathbanInfoLives = get("deathban_info.lives");

      sotw = get("player_timers.sotw");
      sotwOff = get("player_timers.sotw_off");
      location = get("player_timers.location");
      pvpTimer = get("player_timers.pvp_timer");
      combatTag = get("player_timers.combat_tag");
      enderPearl = get("player_timers.ender_pearl");
      apple = get("player_timers.apple");
      gapple = get("player_timers.gapple");
      stuck = get("player_timers.stuck");
      home = get("player_timers.home");
      archerTag = get("player_timers.archer_tag");
      warmup = get("player_timers.warmup");
      logout = get("player_timers.logout");
      activeClass = get("player_timers.active_class");
      koth = get("player_timers.koth");
      spawn = get("player_timers.spawn");
      globalAbilities = get("player_timers.global_abilities");
      bardEnergy = get("bard_class.bard_energy");
      bardCooldown = get("bard_class.bard_cooldown");

      teamFocusEnabled = getBoolean("team_focus.enabled");
      teamFocusLines = getStringList("team_focus.lines");

      staffModeEnabled = getBoolean("staff_mode.enabled");
      staffModeLines = getStringList("staff_mode.mod_mode");

      customTimer = get("custom_timers.format");
   }

   public static BoardManager get() {
      return manager;
   }

   private String get(String path) {
      System.out.println(path);
      return ChatColor.translateAlternateColorCodes('&', BoardFile.get().getString(path));
   }

   private boolean getBoolean(String path) {
      return BoardFile.get().getBoolean(path);
   }

   private List<String> getStringList(String path) {
      List<String> list = new ArrayList<>();
      for (String s : BoardFile.get().getStringList(path)) {
         list.add(ChatColor.translateAlternateColorCodes('&', s));
      }
      return list;
   }

   private int getInt(String path) {
      return BoardFile.get().getInt(path);
   }
}
