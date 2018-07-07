package Telematic;

import Telematic.GPS.GPS;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Nachricht implements Serializable{

    private String tid;
    private LocalDateTime tmstp;
    private GPS gps;


    public Nachricht(String tid, GPS gps) {
        this.tid = tid;
        this.tmstp = LocalDateTime.now();
        this.gps = gps;
    }

    public String getTid() {
        return tid;
    }

    public LocalDateTime getTmstp() {
        return tmstp;
    }

    public GPS getGps() {
        return gps;
    }

    @Override
    public String toString() {
        return "Nachricht{" +
                "tid='" + tid + '\'' +
                ", tmstp=" + tmstp +
                ", " + gps +
                '}';
    }
}
