package eventpatterns.watch;

import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class RegistryThread {

	public void register(Connection connection, Map<String,String> registry, List<String>configValue) {
		try {
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer receiver = session.createConsumer(session.createQueue("CONFIG.Q"));
			while (true) {
				MapMessage msg = (MapMessage)receiver.receive();
				String ip = msg.getString("IP");
				String queue = msg.getString("QUEUE");
				System.out.println("Added to registry: " + ip + "/" + queue);			
				registry.put(ip,queue);
				TextMessage reply = session.createTextMessage(configValue.get(0));
				MessageProducer sender = session.createProducer((Queue)msg.getJMSReplyTo());
				sender.send(reply);			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
