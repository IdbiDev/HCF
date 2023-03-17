package me.idbi.hcf.Classes;

import me.idbi.hcf.Classes.SubClasses.*;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class ClassSelector {
    final static Assassin assassin_class = new Assassin();
    final static Archer archer_class = new Archer();
    final static Bard bard_class = new Bard();
    final static Miner miner_class = new Miner();
    final static Rogue rogue_class = new Rogue();

    public static void addClassToPlayer(Player p) {

        HCFPlayer player = HCFPlayer.getPlayer(p);
        Classes clss = player.getPlayerClass();
        Faction faction = player.getFaction();

        //Assassin class
        /*if(assassin_class.CheckArmor(p)){
            if (!playertools.getMetadata(p, "class").equalsIgnoreCase("Assassin")) {
                assassin_class.setEffect(p);
            }
        }else if(playertools.getMetadata(p, "class").equalsIgnoreCase("Assassin")){
            assassin_class.removeEffects(p);
        }*/


        if (rogue_class.checkArmor(p)) {
            if (rogue_class.rogueEnabled && (rogue_class.maxRogueInFaction >= Playertools.getClassesInFaction(faction, Classes.ROGUE).size() + 1 || rogue_class.maxRogueInFaction == -1)) {
                if (clss != Classes.ROGUE) {
                    rogue_class.setEffect(p);
                }
            }
        } else if (clss == Classes.ROGUE) {
            rogue_class.removeEffects(p);
        }
        //Archer class

        if (archer_class.checkArmor(p)) {
            if (archer_class.archerClassEnabled && (archer_class.maxArcherInFaction >= Playertools.getClassesInFaction(faction, Classes.ARCHER).size() + 1 || archer_class.maxArcherInFaction == -1)) {
                if (clss != Classes.ARCHER) {
                    archer_class.setEffect(p);
                }
            }
        } else if (clss == Classes.ARCHER) {
            archer_class.removeEffects(p);
        }
        //Bard class

        if (bard_class.checkArmor(p)) {
            if (bard_class.bardEnabled && ((bard_class.maxBardInFaction >= Playertools.getClassesInFaction(faction, Classes.BARD).size() + 1) || bard_class.maxBardInFaction == -1)) {
                if (clss != Classes.BARD) {
                    bard_class.setEffect(p);
                }
            }
        } else if (clss == Classes.BARD) {
            bard_class.removeEffects(p);
        }


        //Miner class

        if (miner_class.checkArmor(p)) {
            if (miner_class.minerEnabled && ((miner_class.maxMinerInFaction >= Playertools.getClassesInFaction(faction, Classes.MINER).size() + 1) || miner_class.maxMinerInFaction == -1)) {
                if (clss != Classes.MINER) {
                    miner_class.setEffect(p);
                }
            }
        } else if (clss == Classes.MINER) {
            miner_class.removeEffects(p);
        }
    }
}
