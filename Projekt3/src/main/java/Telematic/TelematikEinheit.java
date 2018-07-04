package Telematic;


import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TelematikEinheit{

    private Fahrdaten fahrdaten = new Fahrdaten(1);

    //Constructor
    public TelematikEinheit() {
        this.fahrdaten = new Fahrdaten(1);
    }

    public Fahrdaten getFahrdaten() {
        return fahrdaten;
    }

    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	// default broker URL is : tcp://localhost:61616"
	private static String subject = "Daten_Queue"; // Queue Name.You can create any/many queue names as per your requirement.

    public void send(Fahrdaten fahrdaten) throws JMSException{

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

        //Fahrdaten get Datum
        fahrdaten.getTmstp();

        // MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination);

        // Creating the object message with the Fahrdaten
        ObjectMessage message = session
                .createObjectMessage(fahrdaten);

        // Here we are sending our message!
        producer.send(message);

        System.out.println("GESENDET '" + message.getObject() + "'");
        connection.close();

    }


    public static void main(String[] args) throws JMSException {

        TelematikEinheit t1 = new TelematikEinheit();
        TelematikEinheit t2 = new TelematikEinheit();
        t1.send(t1.getFahrdaten());
        t2.send(t2.getFahrdaten());
    }


    }


