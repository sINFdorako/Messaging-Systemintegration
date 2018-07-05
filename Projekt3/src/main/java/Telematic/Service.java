package Telematic;

public class Service {
    public static void main(String[] args) {
        TelematikEinheit t1 = new TelematikEinheit();
        new Thread(t1).start();
    }
}
