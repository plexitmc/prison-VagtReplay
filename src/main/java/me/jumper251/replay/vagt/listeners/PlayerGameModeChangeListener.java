package me.jumper251.replay.vagt.listeners;

import dk.plexit.vagt.managers.VagtManager;
import dk.plexit.vagt.vagt.Vagt;
import me.jumper251.replay.vagt.VagtReplayManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerGameModeChangeListener implements Listener {

    private final VagtManager manager;

    public PlayerGameModeChangeListener(VagtManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event){
        if(event.getPlayer() != null && !event.getNewGameMode().equals(GameMode.SURVIVAL)){
            Player player = (Player) event.getPlayer();

            if(!manager.isVagt(player)) return;

            Vagt vagt = manager.getVagt(player);
            if(vagt != null) {
                VagtReplayManager.trashReplay(player);
            }
        }
    }
}
