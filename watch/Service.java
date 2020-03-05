package eventpatterns.watch;

import javax.jms.Connection;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueRequestor;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

public class Service {

	public static void main(String[] args) throws Exception {

		java.util.Scanner input = new java.util.Scanner(System.in);
	    System.out.print("Port: ");
	    long port = input.nextLong();
		input.close();

		String brokerName = "embedded" + port;
		String queueName = "SERVICE-" + port + ".Q";
		String configValue = "";
		
		BrokerService broker = new BrokerService();
		broker.addConnector("tcp://127.0.0.1:" + port);	
		broker.setBrokerName(brokerName);
		broker.setPersistent(false);
		broker.start();
		
		//define the broker and the queue that this service listens on
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://" + brokerName);
		Connection connection = cf.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(queueName);

		//register with the config server and get the latest config value
		ActiveMQConnectionFactory cfConfig = new ActiveMQConnectionFactory("tcp://127.0.0.1:61000");
		QueueConnection connectionConfig = cfConfig.createQueueConnection();
		connectionConfig.start();
		QueueSession sessionConfig = connectionConfig.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queueConfig = sessionConfig.createQueue("CONFIG.Q");
		QueueRequestor requestor = new QueueRequestor(sessionConfig, queueConfig);
		MapMessage message = session.createMapMessage();
		message.setString("IP", "tcp://127.0.0.1:" + port);
		message.setString("QUEUE", queueName);
		TextMessage msgresp = (TextMessage) requestor.request(message);		
		configValue = msgresp.getText();
		connectionConfig.close();
		System.out.println("Config value = " + configValue);

		//now listen for changes from the config server (this would normally be in a thread)
		MessageConsumer receiver = session.createConsumer(queue);
		for (int i=0;i<10;i++) {
			TextMessage msg = (TextMessage)receiver.receive();
			System.out.println("New Config Value Received: " + msg.getText());
		}
		connection.close();			
	}
}
