package eventpatterns.contextualqueue;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiverThread {

	public void processMessages(String queueName) {

		try {
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
			Connection connection = cf.createConnection();
			connection.start();			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer receiver = session.createConsumer(session.createQueue(queueName));

			long time = 0;
			while (true) {
				TextMessage msg = (TextMessage)receiver.receive();
				if (msg.getText().equals("END")) break;
				System.out.println("Processing message: " + msg.getText() + "  (T=" + time + ")");
				Thread.sleep(1000);
				time += 1000;
			}
			
			connection.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}