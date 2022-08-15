package me.jumper251.replay.vagt.listeners;

import dk.plexit.vagt.managers.VagtManager;
import dk.plexit.vagt.vagt.VagtRank;
import me.jumper251.replay.vagt.VagtReplayManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private final VagtManager manager;

    public EntityDamageListener(VagtManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();

            if(player.getHealth() > 20) return;
            if(!manager.isVagt(player)) return;

            if(VagtRank.getHighestVagtRank(player) == null) return;

            VagtReplayManager.startReplay(player);
        }
    }
}
