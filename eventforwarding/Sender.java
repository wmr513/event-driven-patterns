package eventpatterns.eventforwarding;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

	public static void main(String[] args) throws Exception {

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = cf.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("FORWARD.Q");
		MessageProducer sender = session.createProducer(queue);

		java.util.Scanner input = new java.util.Scanner(System.in);
	    System.out.print("Mesasage: ");
	    String msgBody = input.nextLine();
	    System.out.print("Persisted(y/n): ");
	    boolean persist = input.next().equalsIgnoreCase("y");
		input.close();

		TextMessage msg = session.createTextMessage(msgBody);
		sender.setDeliveryMode(persist ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT);
		sender.send(msg);
		System.out.println("Message sent to queue");
		connection.close();
	}
}
