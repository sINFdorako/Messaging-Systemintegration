package Telematic;


import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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


   /* public static void run(String[] args) throws JMSException{
        Nachricht nachricht = new Nachricht();
        TelematikEinheit telematikEinheit = new TelematikEinheit();
        telematikEinheit.send(nachricht);
    }*/

    @Override
    public void run() {

        double strecke = 0;
        double breitengrad = 10.0;
        double laengengrad = 27.0;

        while(true){

            try {
                Thread.sleep(zeitinervall);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            drive(strecke, breitengrad, laengengrad);
            //TODO: Fahren simulieren

            Nachricht nachricht = new Nachricht(this.id,strecke, breitengrad, laengengrad);

            //TODO: Nachricht an Queue schicken
        }

    }

    public void drive(double strecke, double breitengrad, double laengengrad){

        strecke += ThreadLocalRandom.current().nextInt(1, 50 + 1);
        breitengrad += ThreadLocalRandom.current().nextInt(1, 50 + 1);
        laengengrad += ThreadLocalRandom.current().nextInt(1, 50 + 1);

    }

}


