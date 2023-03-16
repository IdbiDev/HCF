package me.idbi.hcf.Commands.KothCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import static me.idbi.hcf.Koth.Koth.getKothFromName;
import static me.idbi.hcf.Tools.HCF_Claiming.KothPrepare;

public class KothCreateCaptureZoneCommand extends SubCommand {
    @Override
    public String getName() {
        return "createcapturezone";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) ;
    }

    @Override
    public String getDescription() {
        return "Gives a claiming wand, it can be used to create the capture zone of the selected koth!";
    }

    @Override
    public String getSyntax() {
    return "/koth " + getName() + " <Koth name>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.koth." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        return false;
    }

    @Override
    public void addCooldown(Player p) {

    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (getKothFromName(args[1]) != 0) {
            if (player.getClaimType() != HCF_Claiming.ClaimTypes.KOTH) {
                if (KothPrepare(p)) {
                    player.setClaimType(HCF_Claiming.ClaimTypes.KOTH);
                    player.setKothId(getKothFromName(args[1]));
                }
            }
        } else {
            p.sendMessage(Messages.koth_invalid_name.language(p).queue());
        }
    }
}
