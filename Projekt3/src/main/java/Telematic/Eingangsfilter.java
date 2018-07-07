package Telematic;

import org.apache.activemq.ActiveMQConnection;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.*;
import java.io.Serializable;

public class Eingangsfilter extends MessageListenerAdapter implements Runnable{



    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    // default broker URL is : tcp://localhost:61616"

    // Name of the queue we will receive messages from
    private static String subject = "Daten_Queue";


        private Session session;
        private MessageProducer verteilerProducer;
        private MessageProducer alarmeProducer;

        public Eingangsfilter(){
            try {
                session = Service.newSession();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {

                //create producers to access verteiler topic and alarme queue
                Topic vertilerTopic = session.createTopic("verteiler");
                Queue alarmeQueue = session.createQueue("alarme");

                verteilerProducer = session.createProducer(vertilerTopic);
                alarmeProducer = session.createProducer(alarmeQueue);

                //make sure handleMessage (of this object) is called every time a message is received
                Queue fahrdatenQueue = session.createQueue(subject);
                MessageConsumer fahrdatenConsumer = session.createConsumer(fahrdatenQueue);
                fahrdatenConsumer.setMessageListener(this);

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }



        public void handleMessage(Message message) {

            //if alarm message send to alarm queue
            if (message instanceof Alarm) {
                try {
                    alarmeProducer.send(session.createObjectMessage(((Serializable) message)));
                } catch (JMSException e) {
                    e.printStackTrace();
                }

                System.out.println("ALARM for Telematik "+ ((Alarm) message).getTid() + ": " + ((Alarm) message).getAlarm());

                return;
            }

            //send message to 'verteiler'-topic
            try {
                verteilerProducer.send(session.createObjectMessage(((Serializable) message)));
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }






