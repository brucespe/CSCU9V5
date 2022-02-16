package ASSIGNMENT;

import java.net.*;

/*
 * A thread that continuously fetches from the buffer in FIFO order
 * For each request, mutex grants the token to the requesting node by simple synchronisation to the nodes indicated port
 * Then waits for token to be returned by means of synchronisation on port 7001
 * All sockets and servers must be suitably closed 
 */
public class C_mutex extends Thread {

	C_buffer buffer;
	Socket s;
	int port;

	// IP address and port number of the node requesting the token.
	// They will be fetched from the buffer
	String n_host;
	int n_port;

	public C_mutex(C_buffer b, int p) {

		buffer = b;
		port = p;
	}

	public void go() {

		try {
			// >>> Listening from the server socket on port 7001
			// from where the TOKEN will be later on returned.
			// This place the coordination server creation outside the while loop.
			ServerSocket ss_back = new ServerSocket(7001);

			while (true) {

				// >>> Print some info on the current buffer content for debugging purposes.
				// >>> please look at the available methods in C_buffer	
				if (buffer.size()==0) {											
					System.out.println("The buffer is currently empty");					//To make sure buffer starts empty
					try{Thread.sleep(1000);} catch(Exception e) {System.out.print("Error in delaying thread - "+e);}		//To avoid spamming the console
				}

				// >>> if the buffer is not empty
				if (buffer.size() > 0) {
					System.out.println("C:mutex   Buffer size is " + buffer.size());

					// >>> Getting the first (FIFO) node that is waiting for a TOKEN form the buffer
					// Type conversions may be needed.
					n_host = (String) buffer.get();				//Gets IP from buffer and converts object to String, then assigns to n_host variable (node host)
					String input = (String) buffer.get();		//Gets port from buffer and converts object to String
					n_port = Integer.parseInt(input);			//Parse the port String and assigns to n_port variable (node port)
					//n_port = (Integer) buffer.get();			//for some reason cannot be converted to int, must be sent as string then be converted
					System.out.println("C:mutex   Node details received from buffer ");
					
					// >>> **** Granting the token to the node
					try {
						s = new Socket(n_host, n_port);			//Socket to create connection with node, this grants the token
						System.out.println("C:mutex   Token has been granted to node ");

					} catch (java.io.IOException e) {			//Runs if unable to grant token
						System.out.println(e);
						System.out.println("CRASH Mutex connecting to the node for granting the TOKEN" + e);
					}

					// >>> **** Getting the token back from the node
					try {
						// THIS IS BLOCKING !
						ss_back.accept();					//ServerSocket listening on the nodes token return port (7001) and accepts connection with node
						System.out.println("C:mutex   ServerSocket received the returned token from node ");

					} catch (java.io.IOException e) {		//runs if unable to make connection with node
						System.out.println(e);
						System.out.println("CRASH: Mutex waiting for the TOKEN back - " + e);
					}
					s.close();
					//ss_back.close();		//closing this gives errors

				} // endif

			} // endwhile

		} catch (Exception e) {			//If unable to carry out any of this class 
			System.out.print(e);	
		}
	}

	public void run() {

		go();
	}

}
