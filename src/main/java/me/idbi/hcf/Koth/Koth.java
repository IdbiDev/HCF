package me.idbi.hcf.Koth;

import me.idbi.hcf.Bossbar.BossbarTools;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.GUI.KOTHItemManager;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Koth implements Listener {

    public static HCF_Claiming.Faction_Claim GLOBAL_AREA = null;
    public static Player GLOBAL_PLAYER = null;
    public static int GLOBAL_TIME;

    @EventHandler(priority = EventPriority.LOWEST)
    public static void KothMoveEvent(PlayerMoveEvent e) {
        //Ha van aktív koth
        if (!(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()))
            return;
        if (GLOBAL_AREA != null) {
            HCF_Claiming.Point start = new HCF_Claiming.Point(GLOBAL_AREA.startX, GLOBAL_AREA.startZ);
            HCF_Claiming.Point end = new HCF_Claiming.Point(GLOBAL_AREA.endX, GLOBAL_AREA.endZ);
            HCF_Claiming.Point point = new HCF_Claiming.Point(e.getTo().getBlockX(), e.getTo().getBlockZ());
            Player p = e.getPlayer();
            if (GLOBAL_PLAYER == null) {
                // ha kothba ment ÉS nem foglalja senki
                if (GLOBAL_AREA.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH) && HCF_Claiming.doOverlap(start, end, point, point)) {
                    HCFPlayer hcf = HCFPlayer.getPlayer(p);
                    if (hcf.inFaction()) {
                        GLOBAL_PLAYER = e.getPlayer();


                        Bukkit.broadcastMessage(Messages.koth_capturing_started.setFaction(GLOBAL_AREA.faction).queue());
                    }
                }
            } else {
                if (GLOBAL_AREA.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH) && !HCF_Claiming.doOverlap(start, end, point, point)) {
                    if (GLOBAL_PLAYER == e.getPlayer()) {
                        GLOBAL_PLAYER = null;
                        GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;
                        Bukkit.broadcastMessage(Messages.koth_capturing_ended.language(p).setFaction(GLOBAL_AREA.faction).queue());
                    }
                }
            }

        }
    }

    public static Faction createKoth(String name) {
        return Playertools.createCustomFaction(name, "");
//        KOTH.koth_area temp = new KOTH.koth_area(
//                faction,
//                new HCF_Claiming.Point(claim.startX,claim.startZ),
//                new HCF_Claiming.Point(claim.endX,claim.endZ)
//        );
    }

    public static void startKoth(String f) {
        try {
            Faction kothFaction = Playertools.getFactionByName(f);
            HCF_Claiming.Faction_Claim kothArea = null;
            if(kothFaction != null) {
                for(HCF_Claiming.Faction_Claim claim : kothFaction.getClaims()) {
                    if(claim.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH)) {
                        kothArea = claim;
                        break;
                    }
                }
                if(kothArea != null) {
                    GLOBAL_AREA = kothArea;
                    GLOBAL_PLAYER = null;
                    GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;
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
            GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;
        }
    }
    public static void startKoth(Player player,String f) {
        try {
            Faction kothFaction = Playertools.getFactionByName(f);
            HCF_Claiming.Faction_Claim kothArea = null;
            if(kothFaction != null) {
                for(HCF_Claiming.Faction_Claim claim : kothFaction.getClaims()) {
                    if(claim.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH)) {
                        kothArea = claim;
                        break;
                    }
                }
                if(kothArea != null) {
                    GLOBAL_AREA = kothArea;
                    GLOBAL_PLAYER = null;
                    GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;
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
                //Todo: Koth not found
                player.sendMessage(Messages.koth_invalid_name.language(player).queue());
            }

        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Messages.error_while_executing.language(player).queue());
        }
    }

    public static void StartRandomKoth() {
        if (Main.kothCache.isEmpty()) {
            return;
        }
        int c = new Random().nextInt(Main.kothCache.size());
        List<String> names = new ArrayList<>(Main.kothCache.keySet());
        startKoth(names.get((c == 0 && names.size() > 0) ? c : c - 1));
    }

    public static void stopKoth() {
        GLOBAL_AREA = null;
        //TODO:  Koth Elfoglalta xy
        //TODO: Set the items
        KOTHItemManager.addRewardsToPlayer(GLOBAL_PLAYER);
        GLOBAL_PLAYER = null;
        GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;

        for (Player player : Bukkit.getOnlinePlayers()) {
            BossbarTools.remove(player);
        }
        ///Main.autoKoth.StopAutoKoth();
    }

    public static int getKothFromName(String name) {
        Faction k = Main.nameToFaction.get(name);
        if (k != null) {
            return k.id;
        }
        return 0;
    }
}
