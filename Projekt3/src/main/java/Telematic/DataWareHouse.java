package Telematic;

import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class DataWareHouse extends MessageListenerAdapter implements Runnable {

    private int interval;
    private Session session;

    private Map<LocalDate, Map<String, double[]>> distanceByIdAndHour = new TreeMap<>();

    public DataWareHouse(){
        try {
            session = Service.newSession();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        this.interval = 30000;
    }


    public DataWareHouse(int interval){
        try {
            session = Service.newSession();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        this.interval = interval;
    }

    @Override
    public void run() {
        try {

            Topic verteilerTopic = session.createTopic("verteiler");
            MessageConsumer consumer = session.createConsumer(verteilerTopic);
            consumer.setMessageListener(this);

        } catch (JMSException e) {
            e.printStackTrace();
        }

        //this loop handles the repetitive output of the distanceByIdAndHour data structure
        while (true) {
            //wait for interval
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            distanceByIdAndHour.forEach((date, map) -> {
                //print date
                System.out.println("--------------------------------------------------");
                System.out.println(date.toString());
                System.out.println("--------------------------------------------------");

                //for every hour
                map.forEach((id, arr) -> {
                    StringBuilder s = new StringBuilder();
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i] > 0) {
                            String rounded = new DecimalFormat("#.###").format(arr[i]);

                            //<from>-<to>: <distance> |
                            //i.e.: 10-11: 403.654 |
                            s.append("| ").append(i).append("-").append(i+1).append(": ").append(rounded).append(" |");
                        }
                    }

                    System.out.println("UNIT " + id + s);
                });
            });
            System.out.println("--------------------------------------------------");
        }
    }

    /**
     * Called upon verteiler-topic message received. Adds data contained in message to distanceByIdHour data-structure.
     */
    public void handleMessage(Nachricht nachricht) {

        LocalDate date = nachricht.getTmstp().toLocalDate();
        String id = nachricht.getTid();

        //add map for given date if not present yet
        if(! distanceByIdAndHour.containsKey(date)) {
            distanceByIdAndHour.put(date, new TreeMap<>());
        }

        Map<String, double []> map = distanceByIdAndHour.get(date);

        //add list for telematicunit if not present yet
        if (! map.containsKey(id)) {
            map.put(id, new double[24]);
        }

        //add distance to specific index in array
        int hour = nachricht.getTmstp().getHour();
        map.get(id)[hour] += nachricht.getGps().getDistance();
    }
}
