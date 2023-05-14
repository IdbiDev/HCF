package me.idbi.hcf.Koth;

import me.idbi.hcf.Bossbar.BossbarTools;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.GUI.KOTHItemManager;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Random;


public class Koth implements Listener {

    public static Claiming.Faction_Claim GLOBAL_AREA = null;
    public static Player GLOBAL_PLAYER = null;
    public static int GLOBAL_TIME = 0;
    static Random r = new Random();

    @EventHandler(priority = EventPriority.LOWEST)
    public static void KothMoveEvent(PlayerMoveEvent e) {
        //Ha van aktív koth
        if (!(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()))
            return;
        if (GLOBAL_AREA != null) {
            Claiming.Point start = new Claiming.Point(GLOBAL_AREA.getStartX(), GLOBAL_AREA.getStartZ());
            Claiming.Point end = new Claiming.Point(GLOBAL_AREA.getEndX(), GLOBAL_AREA.getEndZ());
            Claiming.Point point = new Claiming.Point(e.getTo().getBlockX(), e.getTo().getBlockZ());
            Player p = e.getPlayer();
            if (GLOBAL_PLAYER == null) {
                if (GLOBAL_AREA.getAttribute().equals(Claiming.ClaimAttributes.KOTH) && Claiming.doOverlap(start, end, point, point)) {
                    HCFPlayer hcf = HCFPlayer.getPlayer(p);
                    if (hcf.inFaction()) {
                        GLOBAL_PLAYER = e.getPlayer();


                        Bukkit.broadcastMessage(Messages.koth_capturing_started.setFaction(GLOBAL_AREA.getFaction()).queue());
                    }
                }
            } else {
                if (GLOBAL_AREA.getAttribute().equals(Claiming.ClaimAttributes.KOTH) && !Claiming.doOverlap(start, end, point, point)) {
                    if (GLOBAL_PLAYER == e.getPlayer()) {
                        GLOBAL_PLAYER = null;
                        GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;
                        Bukkit.broadcastMessage(Messages.koth_capturing_ended.language(p).setFaction(GLOBAL_AREA.getFaction()).queue());
                    }
                }
            }

        }
    }

    public static void startKoth(String f) {
        try {
            Faction kothFaction = Playertools.getFactionByName(f);
            Claiming.Faction_Claim kothArea = null;
            if(kothFaction != null) {
                for(Claiming.Faction_Claim claim : kothFaction.getClaims()) {
                    if(claim.getAttribute().equals(Claiming.ClaimAttributes.KOTH)) {
                        kothArea = claim;
                        break;
                    }
                }
                if(kothArea != null) {
                    GLOBAL_AREA = kothArea;
                    GLOBAL_PLAYER = null;
                    GLOBAL_TIME = Config.KOTHDuration.asInt();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(
                                Messages.koth_start_title.language(p).setFaction(kothFaction.getName()).queue(),
                                Messages.koth_start_subtitle.language(p).setFaction(kothFaction.getName()).queue()
                        );
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            GLOBAL_AREA = null;
            GLOBAL_PLAYER = null;
            GLOBAL_TIME = Config.KOTHDuration.asInt() ;
        }
    }
    public static void startKoth(Faction kothFaction) {
        try {
            Claiming.Faction_Claim kothArea = null;
            if(kothFaction != null) {
                for(Claiming.Faction_Claim claim : kothFaction.getClaims()) {
                    if(claim.getAttribute().equals(Claiming.ClaimAttributes.KOTH)) {
                        kothArea = claim;
                        break;
                    }
                }
                if(kothArea != null) {
                    GLOBAL_AREA = kothArea;
                    GLOBAL_PLAYER = null;
                    GLOBAL_TIME = Config.KOTHDuration.asInt();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(
                                Messages.koth_start_title.language(p).setFaction(kothFaction.getName()).queue(),
                                Messages.koth_start_subtitle.language(p).setFaction(kothFaction.getName()).queue()
                        );
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            GLOBAL_AREA = null;
            GLOBAL_PLAYER = null;
            GLOBAL_TIME = Config.KOTHDuration.asInt() ;
        }
    }
    public static void startKoth(Player player,Faction kothFaction) {
        try {
            Claiming.Faction_Claim kothArea = null;
            if(kothFaction != null) {
                for(Claiming.Faction_Claim claim : kothFaction.getClaims()) {
                    if(claim.getAttribute().equals(Claiming.ClaimAttributes.KOTH)) {
                        kothArea = claim;
                        break;
                    }
                }
                if(kothArea != null) {
                    GLOBAL_AREA = kothArea;
                    GLOBAL_PLAYER = null;
                    GLOBAL_TIME = Config.KOTHDuration.asInt();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(
                                Messages.koth_start_title.language(p).setFaction(kothFaction.getName()).queue(),
                                Messages.koth_start_subtitle.language(p).setFaction(kothFaction.getName()).queue()
                        );
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                    }

                } else {
                    player.sendMessage(Messages.koth_failed_not_valid_claim.language(player).queue());
                }
            } else {
                player.sendMessage(Messages.koth_invalid_name.language(player).queue());
            }

        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Messages.error_while_executing.language(player).queue());
        }
    }

    public static void StartRandomKoth() {
        ArrayList<Faction> factions = new ArrayList<>();
        for(Faction f : Main.factionCache.values()) {
            if(f.getLeader() == null){
                for (Claiming.Faction_Claim c : f.getClaims())
                    if(c.getAttribute().equals(Claiming.ClaimAttributes.KOTH))
                        factions.add(f);
            }
        }

        if(!factions.isEmpty()) {
            startKoth(factions.get(r.nextInt(factions.size()-1)));
        }
    }

    public static void stopKoth() {
        GLOBAL_AREA = null;
        GLOBAL_PLAYER = null;
        GLOBAL_TIME = Config.KOTHDuration.asInt();
        for (Player player : Bukkit.getOnlinePlayers()) {
            BossbarTools.remove(player);
        }
    }
    public static void reward(Player p) {
        KOTHItemManager.addRewardsToPlayer(p);
        for(Player _p : Bukkit.getOnlinePlayers()){ // ha GLOBAL AREA null akk az adbi leszopja magát!
            _p.sendMessage(Messages.koth_faction_winner.language(_p).setFaction(HCFPlayer.getPlayer(p).getFaction()).setKoth(Koth.GLOBAL_AREA.getFaction().getName()).queue());
        }
        p.sendMessage(Messages.claim_koth_rewards.language(p).queue());
        HCFPlayer.getPlayer(p).getFaction().addPoints(Config.PointPerKoth.asInt());
    }

}
