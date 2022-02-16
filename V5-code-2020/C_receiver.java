package ASSIGNMENT;

import java.io.IOException;
import java.net.*;

/*
 * Continuously listens for requests from nodes. Requests consist of IP and port sent through socket on port 7000
 * On connection receiver will spawn a thread of the class C_connection_r
 * Such thread will receives IP and port and stores them in buffer
 */
public class C_receiver extends Thread {

	private C_buffer buffer; 				// Instance of buffer class
	private int port; 						// Port to listen to token requests from node

	private ServerSocket s_socket; 			// ServerSocket to receives a connection from node
	private Socket socketFromNode;			//Socket listening to port 7000

	private C_Connection_r connect; 		// C_Connection_r class, instantiated after connection is successful

	public C_receiver(C_buffer b, int p) { 	// Constructor is passed buffer object and receiving port, assigns to local variables
										
		buffer = b;
		port = p;
	}

	public void run() {

		// >>> create the socket the coordinator will listen to
		try {
			s_socket = new ServerSocket(port); 		// Creates the socket listening to port 7000
		} catch (IOException e1) { 					// If unable to create the socket
			System.out.println("Failed to get serversocket");
			System.out.println(e1);
			System.exit(1);
		}

		while (true) {

			try {

				// >>> get a new connection
				socketFromNode = s_socket.accept(); //Assigns socket socketFromNode to accepted requests on ServerSocket s_socket

				System.out.println("C:receiver    Coordinator has received a request ...");

				// >>> create a separate thread to service the request, a C_Connection_r thread.
				connect = new C_Connection_r(socketFromNode, buffer);		//C_Connection_r instantiated after successful connection, passes socket receiving requests from node and buffer
				connect.start();		//Starts the class (running concurrently)

			} catch (java.io.IOException e) {								//If unable to listen to the nodes socket
				System.out.println("Exception when creating a connection " + e);
			}

		}
	}// end run

}

