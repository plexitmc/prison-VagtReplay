package me.jumper251.replay.vagt.listeners;

import dk.plexit.vagt.managers.VagtManager;
import dk.plexit.vagt.vagt.Vagt;
import me.jumper251.replay.vagt.VagtReplayManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthListener implements Listener {

    private final VagtManager manager;

    public EntityRegainHealthListener(VagtManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();

            if(player.getHealth() + event.getAmount() < 20) return;
            if(!manager.isVagt(player)) return;

            Vagt vagt = manager.getVagt(player);
            if(vagt != null) {
                VagtReplayManager.trashReplay(player);
            }
        }
    }
}
