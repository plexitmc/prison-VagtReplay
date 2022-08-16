package me.jumper251.replay.vagt.listeners;

import dk.plexit.vagt.managers.VagtManager;
import dk.plexit.vagt.vagt.Vagt;
import dk.plexit.vagt.vagt.VagtRank;
import me.jumper251.replay.vagt.VagtReplay;
import me.jumper251.replay.vagt.VagtReplayManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final VagtManager manager;

    public PlayerQuitListener(VagtManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        if(!manager.isVagt(event.getPlayer())) return;

        Vagt vagt = manager.getVagt(event.getPlayer());
        if(vagt != null && VagtRank.getHighestVagtRank(event.getPlayer()) != null) {
            //VagtReplayManager.saveReplay(vagt, event.getPlayer());
            VagtReplayManager.trashReplay(event.getPlayer());
        }

    }
}
