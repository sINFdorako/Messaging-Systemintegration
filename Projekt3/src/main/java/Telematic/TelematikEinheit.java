package Telematic;


import javax.jms.*;

import Telematic.GPS.GPS;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.UUID;


public class TelematikEinheit implements Runnable
{

    private String  id;
    private int zeitinervall;

    public TelematikEinheit(int zeitinervall) {
        this.id = UUID.randomUUID().toString();
        this.zeitinervall = zeitinervall;
    }

    public String getId() {
        return id;
    }

    public static String getUrl() {
        return url;
    }

    public static String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "TelematikEinheit{" +
                "id='" + id + '\'' +
                '}';
    }

    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    // default broker URL is : tcp://localhost:61616"
    private static String subject = "Daten_Queue"; // Queue Name.You can create any/many queue names as per your requirement.

    public void send(Nachricht nachricht) throws JMSException {


        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        factory.setTrustAllPackages(true);


        // Getting JMS connection from the server and starting it
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //Creating a non transactional session to send/receive JMS message.
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        //Destination represents here our queue 'JCG_QUEUE' on the JMS server.
        //The queue will be created automatically on the server.
        Destination destination = session.createQueue(subject);

        // MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination);

        // Creating the object message with the Fahrdaten
        ObjectMessage message = session
                .createObjectMessage(nachricht);

        // Here we are sending our message!
        producer.send(message);

        System.out.println("GESENDET '" + message.getObject() + "'");
        connection.close();
    }

    public GPS drive(GPS data){

        double distance = ((double) Math.random() * 200);
        double d = data.getDistance() + distance;
        double b = d / 112 + data.getBreitengrad();
        double l = d / 71 + data.getLängengrad();

        data.setDistance(d);
        data.setBreitengrad(b);
        data.setLängengrad(l);

        return data;
    }

    @Override
    public void run(){

        String id = UUID.randomUUID().toString();
        GPS data = new GPS( GPS.initialLongitude, GPS.initialLatitude,0);
        TelematikEinheit telematikEinheit = new TelematikEinheit(1);

        while(true){

            try {
                Thread.sleep(zeitinervall);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Nachricht nachricht = new Nachricht(id, data);
            try {
                telematikEinheit.send(nachricht);
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }

    }

}


