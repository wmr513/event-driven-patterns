package eventpatterns.contextualqueue;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

	public static void main(String[] args) throws Exception {

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = cf.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer singleSender = session.createProducer(session.createQueue("SINGLE.Q"));
		MessageProducer client1Sender = session.createProducer(session.createQueue("CLIENT1.Q"));
		MessageProducer client2Sender = session.createProducer(session.createQueue("CLIENT2.Q"));
		MessageProducer client3Sender = session.createProducer(session.createQueue("CLIENT3.Q"));

		java.util.Scanner input = new java.util.Scanner(System.in);
	    System.out.print("MultiQueue?(y/n): ");
	    boolean multiQueue = input.next().equalsIgnoreCase("y");
		input.close();

		long sleep = 300;
		long time = 0;
	
		//start sending messages
		if (!multiQueue) {
			sendMessage(singleSender, session, "Client 1: Message 1", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 1: Message 2", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 1: Message 3", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 1: Message 4", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 1: Message 5", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 2: Message 1", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 2: Message 2", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 1: Message 6", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 1: Message 7", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 2: Message 3", time, sleep); time+=sleep;
			sendMessage(singleSender, session, "Client 3: Message 1", time, sleep); time+=sleep;
		} else {
			sendMessage(client1Sender, session, "Client 1: Message 1", time, sleep); time+=sleep;
			sendMessage(client1Sender, session, "Client 1: Message 2", time, sleep); time+=sleep;
			sendMessage(client1Sender, session, "Client 1: Message 3", time, sleep); time+=sleep;
			sendMessage(client1Sender, session, "Client 1: Message 4", time, sleep); time+=sleep;
			sendMessage(client1Sender, session, "Client 1: Message 5", time, sleep); time+=sleep;
			sendMessage(client2Sender, session, "Client 2: Message 1", time, sleep); time+=sleep;
			sendMessage(client2Sender, session, "Client 2: Message 2", time, sleep); time+=sleep;
			sendMessage(client1Sender, session, "Client 1: Message 6", time, sleep); time+=sleep;
			sendMessage(client1Sender, session, "Client 1: Message 7", time, sleep); time+=sleep;
			sendMessage(client2Sender, session, "Client 2: Message 3", time, sleep); time+=sleep;
			sendMessage(client3Sender, session, "Client 3: Message 1", time, sleep); time+=sleep;
		}
		
		connection.close();
	}
	
	private static void sendMessage(MessageProducer sender, Session session, String msg, long time, long sleep) throws Exception {
		sender.send(session.createTextMessage(msg));
		System.out.println("Sending " + msg);
		Thread.sleep(sleep);
	}
	
}
