package Telematic;

import Telematic.GPS.GPS;

import java.io.Serializable;

public class Alarm extends Nachricht implements Serializable{

    private String Alarm;

    public Alarm(String tid, GPS gps, String alarm) {
        super(tid, gps);
        Alarm = alarm;
    }

    public String getAlarm() {
        return Alarm;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "Alarm='" + Alarm + '\'' +
                '}';
    }
}
