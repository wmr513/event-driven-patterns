package eventpatterns.ambulance;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiverThread {

	public void processMessages(boolean priority) {

		try {
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
			Connection connection = cf.createConnection();
			connection.start();			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer receiver = null;
			if (priority) {
				receiver = session.createConsumer(session.createQueue("PRIORITY.Q"));
			} else {
				receiver = session.createConsumer(session.createQueue("STANDARD.Q"));				
			}

			long time = 0;
			while (true) {
				TextMessage msg = (TextMessage)receiver.receive();
				if (msg.getText().equals("END")) break;
				System.out.println("Processing message: " + msg.getText() + "  (T=" + time + ")");
				Thread.sleep(2000);
				time += 2000;
			}
			
			connection.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}