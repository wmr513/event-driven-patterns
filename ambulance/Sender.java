package eventpatterns.ambulance;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

	public static void main(String[] args) throws Exception {

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = cf.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue standardQueue = session.createQueue("STANDARD.Q");
		Queue priorityQueue = session.createQueue("PRIORITY.Q");
		MessageProducer standardSender = session.createProducer(standardQueue);
		MessageProducer prioritySender = session.createProducer(priorityQueue);

		java.util.Scanner input = new java.util.Scanner(System.in);
	    System.out.print("Priority Queue?(y/n): ");
	    boolean priority = input.next().equalsIgnoreCase("y");
		input.close();

		long sleep = 500;
		long time = 0;
		
		//send standard messages
		for (int i=1;i<4;i++) {
			standardSender.send(session.createTextMessage("Standard Message S" + i));
			System.out.println("Sending Standard Message S" + i + "  (T=" + time + ")");
			Thread.sleep(sleep);
			time+=sleep;
		}
		
		//send priority messages
		for (int i=1;i<2;i++) {
			if (priority) {
				prioritySender.send(session.createTextMessage("Priority Message P" + i));				
			} else {
				standardSender.send(session.createTextMessage("Priority Message P" + i));				
			}			
			System.out.println("Sending Priority Message P" + i + "  (T=" + time + ")");
			Thread.sleep(sleep);
			time+=sleep;
		}

		//send standard messages
		for (int i=4;i<7;i++) {
			standardSender.send(session.createTextMessage("Standard Message S" + i));
			System.out.println("Sending Standard Message S" + i + "  (T=" + time + ")");
			Thread.sleep(sleep);
			time+=sleep;
		}
		
		//send priority messages
		for (int i=2;i<3;i++) {
			if (priority) {
				prioritySender.send(session.createTextMessage("Priority Message P" + i));				
			} else {
				standardSender.send(session.createTextMessage("Priority Message P" + i));				
			}			
			System.out.println("Sending Priority Message P" + i + "  (T=" + time + ")");
			Thread.sleep(sleep);
			time+=sleep;
		}

//		standardSender.send(session.createTextMessage("S_END"));				
//		if (priority) {
//			prioritySender.send(session.createTextMessage("P_END"));				
//		}
		
		connection.close();
	}
	
}
