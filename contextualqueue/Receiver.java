package eventpatterns.contextualqueue;


public class Receiver {

	public static void main(String[] args) throws Exception {

		java.util.Scanner input = new java.util.Scanner(System.in);
	    System.out.print("Multi-Queue?(y/n): ");
	    boolean multiQueue = input.next().equalsIgnoreCase("y");
		input.close();
		
		if (multiQueue) {
			new Thread(()->new ReceiverThread().processMessages("CLIENT1.Q")).start(); 
			new Thread(()->new ReceiverThread().processMessages("CLIENT2.Q")).start(); 
			new Thread(()->new ReceiverThread().processMessages("CLIENT3.Q")).start(); 
		} else {
			new Thread(()->new ReceiverThread().processMessages("SINGLE.Q")).start(); 
		}
	}
}
