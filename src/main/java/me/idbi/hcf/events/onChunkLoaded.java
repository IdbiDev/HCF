package me.idbi.hcf.events;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import static me.idbi.hcf.tools.brewing.brewingStands;
import static me.idbi.hcf.tools.brewing.furnaces;

public class onChunkLoaded implements Listener {
    @EventHandler
    public  void onChunkLoaded(ChunkLoadEvent e){
        ///Todo: Optimize
        BlockState[] blocks = e.getChunk().getTileEntities();
        for(BlockState b : blocks){
            if (b.getType() == Material.BREWING_STAND) {
                BrewingStand stand = (BrewingStand) b;
                brewingStands.add(stand);
            }
            if (b.getType() == Material.FURNACE || b.getType() == Material.BURNING_FURNACE) {
                Furnace stand = (Furnace) b;
                furnaces.add(stand);
            }
        }
    }
    @EventHandler
    public void onChunkUnloaded(ChunkUnloadEvent e){
        BlockState[] blocks = e.getChunk().getTileEntities();
        for(BlockState b : blocks){
            if (b.getType() == Material.BREWING_STAND) {
                BrewingStand stand = (BrewingStand) b;
                brewingStands.remove(stand);
            }
            if (b.getType() == Material.FURNACE) {
                Furnace stand = (Furnace) b;
                furnaces.remove(stand);
            }
        }
    }
}
