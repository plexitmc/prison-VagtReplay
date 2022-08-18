package me.jumper251.replay.vagt;

import dk.plexhost.core.time.Time;

import java.util.UUID;

public class VagtRecording {


    private final UUID id;
    private final long startTime;

    public VagtRecording(UUID id){
        this.id = id;
        this.startTime = Time.currentUnixInSeconds();
    }

    public long getStartTime() {
        return startTime;
    }

    public UUID getId() {
        return id;
    }
}
