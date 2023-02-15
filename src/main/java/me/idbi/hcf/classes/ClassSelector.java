package me.idbi.hcf.classes;

import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.classes.subClasses.*;
import me.idbi.hcf.tools.Objects.HCFPlayer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

public class ClassSelector {
    final static Assassin assassin_class = new Assassin();
    final static Archer archer_class = new Archer();
    final static Bard bard_class = new Bard();
    final static Miner miner_class = new Miner();
    final static Rogue rogue_class = new Rogue();

    public static void addClassToPlayer(Player p){

        HCFPlayer player = HCFPlayer.getPlayer(p);
        Classes clss = player.playerClass;
        //Assassin class
        /*if(assassin_class.CheckArmor(p)){
            if (!playertools.getMetadata(p, "class").equalsIgnoreCase("Assassin")) {
                assassin_class.setEffect(p);
            }
        }else if(playertools.getMetadata(p, "class").equalsIgnoreCase("Assassin")){
            assassin_class.removeEffects(p);
        }*/
        if(rogue_class.CheckArmor(p)){
            if (clss != Classes.ROGUE) {
                rogue_class.setEffect(p);
            }
        }else if(clss == Classes.ROGUE) {
            rogue_class.removeEffects(p);
        }
        //Archer class
        if(archer_class.CheckArmor(p)){
            if (clss != Classes.ARCHER) {
                archer_class.setEffect(p);
            }
        }else if(clss == Classes.ARCHER) {
            archer_class.removeEffects(p);
        }
        //Bard class
        if(bard_class.CheckArmor(p)){
            if (clss != Classes.BARD) {
                bard_class.setEffect(p);
            }
        }else if(clss == Classes.BARD) {
            bard_class.removeEffects(p);
        }
        //Miner class
        if(miner_class.CheckArmor(p)){
            if (clss != Classes.MINER) {
                miner_class.setEffect(p);
            }
        }else if(clss == Classes.MINER) {
            miner_class.removeEffects(p);
        }

        Scoreboards.refresh(p);
    }
}
