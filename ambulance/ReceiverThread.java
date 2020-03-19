package eventpatterns.ambulance;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiverThread {

	public void processMessages(boolean priority, boolean carpool) {

		try {
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616?jms.prefetchPolicy.all=0");
			Connection connection = cf.createConnection();
			connection.start();			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer priorityReceiver = null;
			MessageConsumer standardReceiver = null;
			if (priority) {
				priorityReceiver = session.createConsumer(session.createQueue("PRIORITY.Q"));
				standardReceiver = session.createConsumer(session.createQueue("STANDARD.Q"));
			} else {
				standardReceiver = session.createConsumer(session.createQueue("STANDARD.Q"));				
			}

			long time = 0;
			String thread = "";
			while (true) {
				//if not carpool, always check priority queue first, then process a standard message
				TextMessage msg = null;
				if (priority) {
					thread = "[PRIORITY THREAD]";
					if (carpool) {
						msg = (TextMessage)priorityReceiver.receive();
					} else {
						msg = (TextMessage)priorityReceiver.receiveNoWait();
						if (msg == null) {
							msg = (TextMessage)standardReceiver.receiveNoWait();
						}
					}
				} else {
					thread = "[NORMAL THREAD]";
					msg = (TextMessage)standardReceiver.receive();
				}
				if (msg != null) {
					if (msg.getText().equals("END")) break;
					System.out.println("Processing message: " + msg.getText() + "  (T=" + time + ") - " + thread);
					Thread.sleep(2000);
					time += 2000;
				}
			}
			
			connection.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}