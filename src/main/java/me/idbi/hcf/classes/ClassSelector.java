package me.idbi.hcf.classes;

import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.classes.subClasses.Archer;
import me.idbi.hcf.classes.subClasses.Assassin;
import me.idbi.hcf.classes.subClasses.Bard;
import me.idbi.hcf.classes.subClasses.Miner;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

public class ClassSelector {
    final static Assassin assassin_class = new Assassin();
    final static Archer archer_class = new Archer();
    final static Bard bard_class = new Bard();
    final static Miner miner_class = new Miner();

    public static void addClassToPlayer(Player p){

        //Assassin class
        if(assassin_class.CheckArmor(p)){
            if (!playertools.getMetadata(p, "class").equalsIgnoreCase("Assassin")) {
                assassin_class.setEffect(p);
            }
        }else if(playertools.getMetadata(p, "class").equalsIgnoreCase("Assassin")){
            assassin_class.removeEffects(p);
        }
        //Archer class
        if(archer_class.CheckArmor(p)){
            if (!playertools.getMetadata(p, "class").equalsIgnoreCase("Archer")) {
                archer_class.setEffect(p);
            }
        }else if(playertools.getMetadata(p, "class").equalsIgnoreCase("Archer")){
            archer_class.removeEffects(p);
        }
        //Bard class
        if(bard_class.CheckArmor(p)){
            if (!playertools.getMetadata(p, "class").equalsIgnoreCase("Bard")) {
                bard_class.setEffect(p);
            }
        }else if(playertools.getMetadata(p, "class").equalsIgnoreCase("Bard")){
            bard_class.removeEffects(p);
        }
        //Miner class
        if(miner_class.CheckArmor(p)){
            if (!playertools.getMetadata(p, "class").equalsIgnoreCase("Miner")) {
                miner_class.setEffect(p);
            }
        }else if(playertools.getMetadata(p, "class").equalsIgnoreCase("Miner")){
            miner_class.removeEffects(p);
        }

        Scoreboards.refresh(p);
    }
}
