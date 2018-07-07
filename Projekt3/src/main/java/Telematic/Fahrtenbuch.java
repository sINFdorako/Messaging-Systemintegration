package Telematic;

import Telematic.GPS.GPS;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class Fahrtenbuch extends MessageListenerAdapter implements Runnable {

    private Session session;

    private Map<String, List<Nachricht>> log = new TreeMap<>();

    public Fahrtenbuch(){
        try {
            session = Service.newSession();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            //make sure handleMessage (of this object) is called every time a message form topic 'verteiler' is received
            Topic topic = session.createTopic("verteiler");
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(this);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    public void handleMessage(Nachricht nachricht) {

        //add list for telematic telematicunit if not present yet^
        assert nachricht != null;
        if (! log.containsKey(nachricht.getTid())) {
            log.put(nachricht.getTmstp().toString(), new ArrayList<>());
        }

        //add message to list
        log.get(nachricht.getTid()).add(nachricht);

        //save new message to disk
        writeToFile(nachricht);
    }

    private void writeToFile(Nachricht nachricht) {
        String DIRECTORY = "LogBook";

        LocalDateTime timestamp = nachricht.getTmstp();
        String unitId = nachricht.getTid();

        //create root dir if not present yet
        String dir = DIRECTORY + File.separator + unitId;
        new File(dir).mkdirs();

        //build json out of message
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("unitId", unitId);
        jsonBuilder.add("timestamp", timestamp.toString());
        jsonBuilder.add("distance", nachricht.getGps().getDistance());
        jsonBuilder.add("latitude", nachricht.getGps().getBreitengrad());
        jsonBuilder.add("longitude", nachricht.getGps().getLängengrad());

        byte[] content = jsonBuilder.build().toString().getBytes();

        //create file
        String fileName = dir + File.separator + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss")) + ".log";
        try {
            Files.write(Paths.get(fileName), content, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
