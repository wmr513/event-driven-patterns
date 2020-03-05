package eventpatterns.watch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

public class ConfigServer {

	public static void main(String[] args) throws Exception {

		List<String> configValue = new ArrayList<String>();
		configValue.add("800");
		Map<String,String> registry = new HashMap<String,String>();
		
		BrokerService broker = new BrokerService();
		broker.addConnector("tcp://127.0.0.1:61000");	
		broker.setBrokerName("embeddedConfig");
		broker.setPersistent(false);
		broker.start();
		
		//define the broker and the queue that this service listens on and listen for requests
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://embeddedConfig");
		Connection connection = cf.createConnection();
		connection.start();
		new Thread(()->new RegistryThread().register(connection, registry, configValue)).start(); 		

		System.out.println("Current config value: " + configValue.get(0));

		//wait for 2 entries for demo purposes...
		while (registry.size() < 2) {
			Thread.sleep(1000);
		}
		//update new configuration value
		java.util.Scanner input = new java.util.Scanner(System.in);
		while (true) {
			System.out.println();
		    System.out.print("Enter new config value: ");
		    String newValue = input.nextLine();
		    configValue.set(0, newValue);
		    if (newValue.equals("END")) {
		    	break;
		    }

		    //notify all services
		    List<String> unreachableServices = new ArrayList<String>();
		    for (Map.Entry<String,String> entry : registry.entrySet()) {
		    	String ip = entry.getKey();
		    	String queueName = entry.getValue();
		    	System.out.println("Notifying " + queueName + ", IP=" + ip);
		    	if (serviceReachable(ip)) {
			    	ActiveMQConnectionFactory cfTemp = new ActiveMQConnectionFactory(ip);
					Connection connectionTemp = cfTemp.createConnection();
					connectionTemp.start();
					Session sessionTemp = connectionTemp.createSession(false, Session.AUTO_ACKNOWLEDGE);
					MessageProducer sender = sessionTemp.createProducer(sessionTemp.createQueue(queueName));
					TextMessage msg = sessionTemp.createTextMessage(configValue.get(0));
					sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
					sender.send(msg);
					connectionTemp.close();
		    	} else {
		    		System.out.println("Service not found: " + queueName);
		    		System.out.println("Removing service from registry");
		    		unreachableServices.add(ip);
		    	}
		    }
		    
		    //now cleanup registry...
		    for (String service : unreachableServices) {
		    	registry.remove(service);
		    }
		}
		input.close();
		System.exit(0);
	}
	
	private static boolean serviceReachable(String ip) {
    	ActiveMQConnectionFactory cfTemp = new ActiveMQConnectionFactory(ip);
		try {
			Connection connectionTemp = cfTemp.createConnection();
			connectionTemp.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
