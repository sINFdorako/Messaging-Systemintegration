package Telematic.GPS;

import java.io.Serializable;

public class GPS implements Serializable{

    public static final double initialLatitude = 52.5186200;
    public static final double initialLongitude = 13.3761870;
    private double distance;


        private double breitengrad;
        private double längengrad;


    public GPS(double breitengrad, double längengrad, double distance) {
        this.distance = distance;
        this.breitengrad = breitengrad;
        this.längengrad = längengrad;
    }

    public double getDistance() {
        return distance;
    }

    public double getBreitengrad() {
        return breitengrad;
    }

    public double getLängengrad() {
        return längengrad;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setBreitengrad(double breitengrad) {
        this.breitengrad = breitengrad;
    }

    public void setLängengrad(double längengrad) {
        this.längengrad = längengrad;
    }

    @Override
    public String toString() {
        return "GPS{" +
                "distance=" + distance +
                ", breitengrad=" + breitengrad +
                ", längengrad=" + längengrad +
                '}';
    }





    }

