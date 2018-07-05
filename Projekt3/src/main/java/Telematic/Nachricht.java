package Telematic;

import com.sun.xml.bind.v2.model.core.ID;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Nachricht implements Serializable{

    private double breitengrad;
    private double längengrad;
    private String tid;
    private double strecke;
    private LocalDateTime tmstp;

    public Nachricht(String tid, double strecke, double breitengrad, double längengrad) {
        //this.breitengrad = ThreadLocalRandom.current().nextInt(1, 119 + 1);
        //this.längengrad = ThreadLocalRandom.current().nextInt(1, 89 + 1);
        //this.tid = UUID.randomUUID().toString();
        this.breitengrad = breitengrad;
        this.längengrad = längengrad;
        this.tid = tid;
        this.strecke = strecke;
        this.strecke = ThreadLocalRandom.current().nextInt(1, 50 + 1);;
        this.tmstp = LocalDateTime.now();
    }

    public double getBreitengrad() {
        return breitengrad;
    }

    public double getLängengrad() {
        return längengrad;
    }

    public String getTid() {
        return tid;
    }

    public double getStrecke() {
        return strecke;
    }

    public Timestamp getTmstp() {
        return tmstp;
    }

    @Override
    public String toString() {
        return "Nachricht{" +
                "breitengrad=" + breitengrad +
                ", längengrad=" + längengrad +
                ", tid='" + tid + '\'' +
                ", strecke=" + strecke +
                ", tmstp=" + tmstp +
                '}';
    }
}
