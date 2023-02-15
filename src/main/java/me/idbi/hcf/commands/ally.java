package me.idbi.hcf.commands;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.commands.allyCommands.ally_Accept;
import me.idbi.hcf.commands.allyCommands.ally_Invite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ally implements CommandExecutor, TabCompleter {
    private final Main m = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "request":
                        try {
                            ally_Invite.InvitePlayerToFaction(p,args[1]);
                        } catch (IndexOutOfBoundsException ignored) {
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        } catch (Exception e) {
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    case "accept":
                        try {
                            //TODO:ACCEPT
                            ally_Accept.AcceptAlly(p, args[1]);
                        } catch (IndexOutOfBoundsException ignored) {
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        } catch (Exception e) {
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    case "decline":
                        try {
                            //TODO:ACCEPT
                        } catch (IndexOutOfBoundsException ignored) {
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        } catch (Exception e) {
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                    case "resolve":
                        try {
                            //TODO:ACCEPT
                        } catch (IndexOutOfBoundsException ignored) {
                            p.sendMessage(Messages.missing_argument.language(p).queue());
                            //Kiíratás hogy balfaszul használta a commandot
                        } catch (Exception e) {
                            p.sendMessage(Messages.error_while_executing.language(p).queue());
                            e.printStackTrace();
                        }
                        break;
                }
            }else{
                //TODO: PRINT HELP
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("ally")) {
            if (args.length == 1) {
                return Arrays.asList(
                        "request",
                        "accept",
                        "decline",
                        "resolve"
                );
            }
        }
        return null;
    }
}
