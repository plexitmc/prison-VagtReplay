package me.jumper251.replay.vagt;

import dk.plexhost.core.time.Time;
import dk.plexhost.core.utils.FileUtils;
import dk.plexit.vagt.VagtSystem;
import dk.plexit.vagt.managers.VagtManager;
import dk.plexit.vagt.vagt.Vagt;
import dk.plexit.vagt.vagt.VagtRank;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.vagt.command.VagtReplayCommand;
import me.jumper251.replay.vagt.listeners.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class VagtReplayManager {

    private static final HashMap<UUID, VagtRecording> currentlyRecording = new HashMap<>();

    private static final HashMap<UUID, VagtReplay> newReplays = new HashMap<>();
    private static final HashMap<UUID, VagtReplay> oldReplays = new HashMap<>();

    private static File replayFolder;


    public static void register(ReplaySystem replaySystem) {
        replayFolder = new File(replaySystem.getDataFolder(), "vagt-replays");
        loadReplays();
        enableListeners(replaySystem, VagtSystem.getInstance().getVagtManager());

        replaySystem.getCommand("vagtreplay").setExecutor(new VagtReplayCommand());
    }

    private static void enableListeners(JavaPlugin plugin, VagtManager manager){
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new EntityDamageListener(manager), plugin);
        pm.registerEvents(new EntityRegainHealthListener(manager), plugin);
        pm.registerEvents(new PlayerQuitListener(manager), plugin);
        pm.registerEvents(new VagtKillListener(manager), plugin);
        pm.registerEvents(new PlayerGameModeChangeListener(manager), plugin);
    }


    public static void loadReplays(){
        newReplays.clear();
        oldReplays.clear();

        File[] files = replayFolder.listFiles();
        if(files == null || files.length == 0) return;

        for(File file : files){
            if(FileUtils.isYamlFile(file.getName())){
                VagtReplay replay = new VagtReplay(file);
                if (replay.isOld()) oldReplays.put(replay.getReplayID(), replay);
                else newReplays.put(replay.getReplayID(), replay);
            }
        }
    }

    public static void startReplay(Player player){
        if(currentlyRecording.containsKey(player.getUniqueId())) return;

        UUID id;

        do id = UUID.randomUUID();
        while (currentlyRecording.containsKey(id) || newReplays.containsKey(id) || oldReplays.containsKey(id));

        currentlyRecording.put(player.getUniqueId(), new VagtRecording(id));
        ReplayAPI.getInstance().recordReplay(id.toString(), player, player.getWorld().getPlayers());
    }

    public static void trashReplay(Player player){

        if(!currentlyRecording.containsKey(player.getUniqueId())) return;

        VagtRecording vagtRecording = currentlyRecording.get(player.getUniqueId());
        currentlyRecording.remove(player.getUniqueId());
        ReplayAPI.getInstance().stopReplay(vagtRecording.getId().toString(), false);
    }

    public static void saveReplay(Vagt vagt, Player player){
        if(!currentlyRecording.containsKey(player.getUniqueId())) return;

        if(VagtRank.getHighestVagtRank(player) == null) {
            trashReplay(player);
            return;
        }

        VagtRecording vagtRecording = currentlyRecording.get(player.getUniqueId());
        currentlyRecording.remove(player.getUniqueId());
        ReplayAPI.getInstance().stopReplay(vagtRecording.getId().toString(), true);

        VagtReplay replay = new VagtReplay(
                vagt.getRankInt(),
                System.currentTimeMillis(),
                player.getName(),
                player.getUniqueId(),
                player.getLocation(),
                vagtRecording.getId(),
                Time.currentUnixInSeconds() - vagtRecording.getStartTime(),
                false
        );
        replay.save();

        newReplays.put(vagtRecording.getId(), replay);
    }

    public static void showReplay(Player player, UUID id){
        ReplayAPI.getInstance().playReplay(id.toString(), player);
        if(newReplays.containsKey(id)) {
            VagtReplay replay = newReplays.get(id);
            newReplays.remove(id);

            oldReplays.put(id, replay);

            replay.setOld(true);
            replay.save();
        }
    }

    public static File getReplayFolder() {
        return replayFolder;
    }

    public static Collection<VagtReplay> getOldReplays() {
        return oldReplays.values();
    }

    public static Collection<VagtReplay> getNewReplays() {
        return newReplays.values();
    }
}
