package me.jumper251.replay.vagt;

import dk.plexit.vagt.utils.LocationUtils;
import dk.plexit.vagt.vagt.VagtRank;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class VagtReplay {

    private final VagtRank vagtRank;
    private final long timeStamp;
    private final String lastName;
    private final UUID uniqueId;

    private final UUID replayID;

    private final Location location;
    private final long length;

    private boolean isOld;

    public VagtReplay(int vagtRank, long timeStamp, String lastName, UUID uniqueId, Location location, UUID replayID, long length, boolean isOld) {
        this.vagtRank = VagtRank.getRankFromInt(vagtRank);
        this.timeStamp = timeStamp;
        this.lastName = lastName;
        this.location = location;
        this.uniqueId = uniqueId;
        this.replayID = replayID;
        this.length = length;
        this.isOld = isOld;
    }

    public VagtReplay(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.vagtRank = VagtRank.getRankFromInt(config.getInt("rank"));
        this.timeStamp = config.getLong("timestamp");
        this.lastName = config.getString("lastname");
        this.location = LocationUtils.fromString(config.getString("location"));
        this.uniqueId = UUID.fromString(config.getString("uniqueid"));
        this.replayID = UUID.fromString(config.getString("replayid"));
        this.isOld = config.getBoolean("old");
        this.length = config.getLong("length");
    }

    public long getTimestamp() {
        return timeStamp;
    }

    public String getLastName() {
        return lastName;
    }

    public UUID getReplayID() {
        return replayID;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public VagtRank getVagtRank() {
        return vagtRank;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isOld() {
        return isOld;
    }

    public long getLength() {
        return length;
    }

    public void setOld(boolean isOld) {
        this.isOld = isOld;
        save();
    }

    public void save(){
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.set("rank", getVagtRank().getRank());
        yaml.set("timestamp", getTimestamp());
        yaml.set("lastname", getLastName());
        yaml.set("location", LocationUtils.toString(getLocation()));
        yaml.set("uniqueid", getUniqueId().toString());
        yaml.set("replayid", getReplayID().toString());
        yaml.set("old", isOld);
        yaml.set("length", getLength());

        File file = new File(VagtReplayManager.getReplayFolder(), getReplayID().toString() + ".yml");

        try {
            yaml.save(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
