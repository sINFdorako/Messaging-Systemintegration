package Telematic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSslConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class Service {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;


    public static void main(String[] args) {

        new Thread(new Fahrtenbuch()).start();
        new Thread(new DataWareHouse(10000)).start();
        new Thread(new Eingangsfilter()).start();
        new Thread(new TelematikEinheit(8000)).start();
        new Thread(new TelematikEinheit(3000)).start();
        new Thread(new TelematikEinheit(2000)).start();

    }

    public static Session newSession() throws JMSException {
        ActiveMQSslConnectionFactory cf = new ActiveMQSslConnectionFactory(url);
        cf.setTrustAllPackages(true);
        Connection c = cf.createConnection();

        c.start();

        return (c.createSession(false,Session.AUTO_ACKNOWLEDGE));
    }
}
