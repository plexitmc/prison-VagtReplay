package me.jumper251.replay.vagt.listeners;

import dk.plexit.vagt.events.VagtKillEvent;
import dk.plexit.vagt.managers.VagtManager;
import me.jumper251.replay.vagt.VagtReplayManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VagtKillListener implements Listener {

    private final VagtManager manager;

    public VagtKillListener(VagtManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onVagtKill(VagtKillEvent event){

        if(!manager.isVagt(event.getVictim())) return;

        VagtReplayManager.saveReplay(event.getVagt(), event.getVictim());
    }
}
