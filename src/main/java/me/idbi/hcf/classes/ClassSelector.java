package me.idbi.hcf.classes;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;


public class ClassSelector {
    public static void addClassToPlayer(Player player){
        try{
            if(Archer.CheckArmor(player)){
                if(!playertools.getMetadata(player,"class").equals("archer")) {
                    if(Main.debug)
                        System.out.println("Equiping Archerclass to " + player.getDisplayName());
                    Archer.setEffect(player);
                }
            } else if(playertools.getMetadata(player,"class").equals("archer")) {
                if(Main.debug)
                    System.out.println("Removing Archerclass from "+player.getDisplayName());
                Archer.removeEffects(player);
                playertools.setMetadata(player,"class","none");
            }

            if(Bard.CheckArmor(player)) {
                if(!playertools.getMetadata(player,"class").equals("bard")){
                    if(Main.debug)
                        System.out.println("Equiping Bard class to "+player.getDisplayName());
                    Bard.setEffect(player);
                }
            } else if(playertools.getMetadata(player,"class").equals("bard")) {
                if(Main.debug)
                    System.out.println("Removing Bard class from "+player.getDisplayName());
                Bard.removeEffects(player);
                playertools.setMetadata(player,"class","none");
            }
        } catch (Exception ignored) {

        }
    }
}

