package me.idbi.hcf.commands;

import me.idbi.hcf.adminsystem.adminMain;
import me.idbi.hcf.tools.playertools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class admin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                if(p.hasPermission("factions.admin.admin")){
                    switch (args[0]) {
                        case "duty":
                            adminMain.setAdminDuty(p,!Boolean.parseBoolean(playertools.getMetadata(p,"adminDuty")));
                            break;
                        case "deposit":
                            try{
                                adminMain.Deposit(p,args[1], Integer.parseInt(args[2]));
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "withdraw":
                            try{
                                adminMain.Withdraw(p,args[1], Integer.parseInt(args[2]));
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "freeze":
                            try{
                                adminMain.FreezePlayer(p,args[1]);
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "kick":
                            break;
                        case "setfaction":
                            try{
                                adminMain.setPlayerFaction(p,args[1],args[2]);
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "removefaction":
                            try{
                                adminMain.kickPlayerFromFaction(p,args[1],args[2]);
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "deletefaction":
                            try{
                                adminMain.DeleteFaction(p,args[1]);
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "setleader":
                            try{
                                adminMain.setFactionLeader(p,args[1],args[2]);
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "givemoney":
                            try{
                                adminMain.GiveMoney(p,args[1], Integer.parseInt(args[2]));
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "takemoney":
                            try{
                                adminMain.TakeMoney(p,args[1], Integer.parseInt(args[2]));
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                        case "setfactionname":
                            try{
                                adminMain.SetFactionname(p,args[1], String.valueOf(args[2]));
                            }catch (Exception ignored){
                                System.out.println("Yes");
                            }
                            break;
                    }
                }else{
                    //Todo: nem vagy admin
                }
            }
        }
        return false;
    }
}
