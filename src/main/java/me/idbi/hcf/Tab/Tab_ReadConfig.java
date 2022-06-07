package me.idbi.hcf.Tab;

import me.idbi.hcf.Tab.Kraken.tab.PlayerTab;
import me.idbi.hcf.Tab.Kraken.tab.event.PlayerTabCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class Tab_ReadConfig implements Listener {

    public static HashMap<Integer, HashMap<Integer, String>> tab;

    @EventHandler
    public void onPlayerTabCreateEvent(PlayerTabCreateEvent e) {
        PlayerTab playerTab = e.getPlayerTab();
        playerTab.getByPosition(0, 0).text("Static text").send();
//        for(Map.Entry<Integer, HashMap<Integer, String>> hashMap : tab.entrySet()) {
//
//            int row = hashMap.getKey();
//            HashMap<Integer, String> columnMap = hashMap.getValue();
//
//            for(Map.Entry<Integer, String> column : columnMap.entrySet()) {
//                int columnIndex = column.getKey();
//                String text = column.getValue();
//                playerTab.getByPosition(row, columnIndex).text(text).send();
//            }
//        }
    }
}
