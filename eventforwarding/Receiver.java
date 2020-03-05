package eventpatterns.eventforwarding;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {

	public static void main(String[] args) throws Exception {

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = cf.createConnection();
		connection.start();
		
		java.util.Scanner input = new java.util.Scanner(System.in);
	    System.out.print("client-acknowledge(y/n): ");
	    boolean clientAck = input.next().equalsIgnoreCase("y");
	    System.out.print("generate error(y/n): ");
	    boolean genError = input.next().equalsIgnoreCase("y");
		input.close();
		
		Session session = connection.createSession(false, clientAck ? Session.CLIENT_ACKNOWLEDGE : Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("FORWARD.Q");
		MessageConsumer receiver = session.createConsumer(queue);

		TextMessage msg = (TextMessage)receiver.receive();
		System.out.println("Processing message: " + msg.getText());
		for (int i=0;i<9;i++) {
			Thread.sleep(1000);
			System.out.print(".");
		}
		System.out.println();
		
		if (genError) {
			System.out.println("Error processing message.");
		} else {
			System.out.println("Database commit.");
		}
		
		if (clientAck && !genError) {
			msg.acknowledge();
		}
		
		connection.close();			
	}
}
