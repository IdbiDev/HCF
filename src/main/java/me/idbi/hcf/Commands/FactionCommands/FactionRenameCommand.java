package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class FactionRenameCommand extends SubCommand {
    public boolean isFactionNameTaken(String name) {
        return Main.nameToFaction.containsKey(name);
    }
    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public String getDescription() {
        return "Renames the faction";
    }

    @Override
    public String getSyntax() {
        return "/faction " + getName() + " <new name>";
    }

    @Override
    public int getCooldown() {
        return 5;
    }

    @Override
    public void perform(Player p, String[] args) {
        addCooldown(p);
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(player.inFaction()) {
            if(player.getRank().isLeader()) {
                String name = args[1];
                if (!Playertools.isValidName(args[1])) {
                    p.sendMessage(Messages.not_a_valid_name.language(p).queue());
                    return;
                }
                if (!isFactionNameTaken(name)) {
                    for (String blacklisted_word : Main.blacklistedRankNames) {
                        if (name.toLowerCase().contains(blacklisted_word.toLowerCase())) {
                            p.sendMessage(Messages.prefix_cmd.language(p).queue() + " " + Messages.gui_bad_word.language(p).queue());
                            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                            return;
                        }
                    }
                    Faction f = player.getFaction();
                    Main.nameToFaction.remove(f.getName());
                    f.setName(name);
                    Main.nameToFaction.put(f.getName(),f);
                    player.sendMessage(Messages.rename_faction);
                    GUISound.playSound(p, GUISound.HCFSounds.SUCCESS);
                }else {
                    player.sendMessage(Messages.exists_faction_name);
                }
            }else {
                player.sendMessage(Messages.no_permission_in_faction);
            }
        } else {
            player.sendMessage(Messages.not_in_faction);
        }
    }
}
