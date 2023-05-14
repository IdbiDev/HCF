package me.idbi.hcf.Commands.CustomClaimCommands;

import me.idbi.hcf.ClickableMessages.ClickableFactions;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionListGUI.FactionToplistInventory;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomClaimListCommand extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Lists all the custom created factions.";
    }

    @Override
    public String getSyntax() {
        return "/customclaim " + getName() + " [page]";
    }

    @Override
    public int getCooldown() {
        return 3;
    }
    @Override
    public String getPermission() {
        return "factions.commands.customclaim." + getName();
    }
    @Override
    public void perform(Player p, String[] args) {
        if(args.length == 1) {
            try {
               // run(p, factions, 1, type);
            } catch (IndexOutOfBoundsException e) {
                p.sendMessage(Messages.cant_find_page.language(p).queue());
            }
            return;
        }
    }
    /*private void run(Player p, List<Faction> factions, int page) {
        if (!(page > 0)) {
            p.sendMessage(Messages.cant_find_page.language(p).queue());
            return;
        }
        boolean hasNext = false;
        if(factions.size() > 10 && factions.size() > (page * 10 + 10))
            hasNext = true;

        p.openInventory(FactionToplistInventory.listInv(p, factions, type, 1));
        if(page == 1) {
            List<Faction> newFactions = factions.subList(0, Math.min(factions.size(), 10));
            printFactions(p, newFactions, type);
            ClickableFactions.sendPage(p, page, hasNext, false, "/f list " + type.toLowerCase());
            return;
        }
        try {
            if (page * 10 <= factions.size()) {
                List<Faction> pageFactions = factions.subList(page * 10, Math.min(page * 10 + 10, factions.size()));
                printFactions(p, pageFactions, type);
                ClickableFactions.sendPage(p, page, hasNext, true, "/f list " + type.toLowerCase());
            } else {
                p.sendMessage(Messages.cant_find_page.language(p).queue());
            }
        } catch (IndexOutOfBoundsException e) {
            p.sendMessage(Messages.cant_find_page.language(p).queue());
        }
    }*/
}
