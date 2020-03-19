package eventpatterns.ambulance;


public class Receiver {

	public static void main(String[] args) throws Exception {

		boolean STANDARD = false;
		boolean PRIORITY = true;
		
		java.util.Scanner input = new java.util.Scanner(System.in);
	    System.out.print("Priority Queue? (y/n): ");
	    boolean priority = input.next().equalsIgnoreCase("y");
	    System.out.print("Carpool Lane? (y/n): ");
	    boolean carpool = input.next().equalsIgnoreCase("y");
		input.close();
		
		if (priority) {
			new Thread(()->new ReceiverThread().processMessages(PRIORITY, carpool)).start(); 
		}
		new Thread(()->new ReceiverThread().processMessages(STANDARD, carpool)).start(); 
	}
}
