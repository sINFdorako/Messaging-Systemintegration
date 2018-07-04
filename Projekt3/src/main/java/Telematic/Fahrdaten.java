package Telematic;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Fahrdaten implements Serializable {

    private String  id;
    private int zeitintervall_in_s;
    private double zufallszahl_gefahrene_strecke;

    public Fahrdaten(int zeitintervall_in_s) {
        this.id = UUID.randomUUID().toString();
        this.zeitintervall_in_s = zeitintervall_in_s;
        this.zufallszahl_gefahrene_strecke = ThreadLocalRandom.current().nextInt(1, 50+1);
    }

    public String getId() {
        return id;
    }

    public int getZeitintervall_in_s() {
        return zeitintervall_in_s;
    }

    public double getZufallszahl_gefahrene_strecke() {
        return zufallszahl_gefahrene_strecke;
    }

    public Timestamp getTmstp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static double generiereZufallszahl(){
        double z;
        z = ( Math.random() * 100);
        return z;
    }

    public static int generiereId(){
        int z;
        z = (((int) Math.random())  * 1000);
        return z;
    }

    @Override
    public String toString() {
        return "Fahrdaten{" +
                "id='" + id + '\'' +
                ", zeitintervall_fuer_datenweitergabe=" + zeitintervall_in_s + "s" +
                ", gefahrene_strecke=" + zufallszahl_gefahrene_strecke + "km" +
                '}';
    }
}
