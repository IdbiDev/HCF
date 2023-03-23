package me.idbi.hcf.Scoreboard;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Miner;
import me.idbi.hcf.CustomFiles.BoardFile;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.FastBoard.FastBoard;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Board {

    private final Player player;
    private long titleChanger;
    private FastBoard fastBoard;
    private int titleIndex;
    private String title;

    public Board(Player player) {
        this.player = player;
        this.titleChanger = System.currentTimeMillis();
        this.titleIndex = 0;
        this.fastBoard = new FastBoard(player);
        this.title = BoardManager.get().getTitle();
    }

    private void tickTitle() {
        if (BoardManager.get().isTitleChanger()) {
            if (this.titleChanger < System.currentTimeMillis()) {
                this.titleChanger = System.currentTimeMillis() + BoardManager.get().getTitleChangerTicks();
                this.fastBoard.updateTitle(this.getTitle());
            }
        }
        else {
            if (!this.fastBoard.getTitle().equals(this.title)) {
                this.fastBoard.updateTitle(this.title);
            }
        }
    }

    public String getTitle() {
        List<String> lines = BoardManager.get().getTitleChangeLines();
        if(this.titleIndex == lines.size()) {
            this.titleIndex = 0;
            return lines.get(0);
        }

        String s = lines.get(this.titleIndex);
        this.titleIndex++;
        return s;
    }


    public void update() {
        if(player == null) {
            this.fastBoard.delete();
            return;
        }
        List<String> lines = getLines();
        if (lines == null || lines.isEmpty()) {
            if (!this.fastBoard.isDeleted()) {
                this.fastBoard.delete();
            }
            return;
        }
        if (this.fastBoard.isDeleted()) {
            this.fastBoard = new FastBoard(this.player);
        }
        this.tickTitle();
        this.fastBoard.updateLines(getLines());
    }

    public List<String> getLines() {
        if(player == null) {
            this.fastBoard.delete();
            return new ArrayList<>();
        }
        List<String> boardList = new ArrayList<>();
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);

        if(hcfPlayer.isDeathBanned()) {
            boardList.add(BoardManager.get().getLine());
            boardList.add(BoardManager.get().getDeathbanInfoTime() + new SimpleDateFormat("HH:mm").format(new Date(hcfPlayer.getDeathTime())));
            boardList.add(BoardManager.get().getDeathbanInfoLives() + hcfPlayer.getLives());
            boardList.add(BoardManager.get().getLine());
            return boardList;
        }
        boardList.add(BoardManager.get().getLine());

        if(hcfPlayer.isInDuty()) {
            List<String> list = new ArrayList<>(BoardManager.get().getStaffModeLines());
            list.replaceAll(s -> s
                    .replace("%vanish%", AdminTools.InvisibleManager.getInvisedAdmins().contains(this.player) + "")
                    .replace("%players%", Bukkit.getOnlinePlayers().size() + "")
                    .replace("%staff%", Playertools.getStaffs().size() + "")
                    .replace("%chat%", Playertools.upperFirst(hcfPlayer.getChatType().name()))
                    .replace("%tps%", "21"));
            boardList.addAll(list);
        }

        if(BoardManager.get().getLocation() != null) {
            boardList.add(BoardManager.get().getLocation() + hcfPlayer.getLocationFormatted());
        }

        // ToDo: Koth
        if(BoardManager.get().getActiveClass() != null) {
            boardList.add(BoardManager.get().getActiveClass() + Playertools.upperFirst(hcfPlayer.getPlayerClass().name()));
            if(hcfPlayer.getPlayerClass() == Classes.BARD) {
                boardList.add(BoardManager.get().getBardEnergy() + Formatter.formatBardEnergy(hcfPlayer.getBardEnergy()));
                if(Timers.BARD_COOLDOWN.has(hcfPlayer))
                    boardList.add(BoardManager.get().getBardCooldown() + Formatter.getRemaining(Timers.BARD_COOLDOWN.get(hcfPlayer), true));
            }
        }

        // Player Timers
        for (Timers value : Timers.values()) {
            if(value.has(hcfPlayer)) {
                try {
                    String getLine = ChatColor.translateAlternateColorCodes('&',
                            BoardFile.get().getString("player_timers." + value.name().toLowerCase()));

                    if (getLine == null) continue;
                    boardList.add(getLine + Formatter.getRemaining(value.get(hcfPlayer), true));
                } catch (NullPointerException e) {
                }
            }
        }

        // ToDo: Footer
        boardList.add(BoardManager.get().getLine());
        return boardList;
    }
}
