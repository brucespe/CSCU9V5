package ASSIGNMENT;

import java.net.*;
import java.io.*;

/*
 * This class reacts to a node request.
 * on connection, the receiver will spawn a thread of this, thread receives ip and port and stores them in buffer using a request data structure defined in code
 */
public class C_Connection_r extends Thread {

	// class variables
	C_buffer buffer;

	Socket s; 				// socket to receive requests from the node
	InputStream in;			// InputStream to receive data from node
	BufferedReader bin; 	// BufferedReader to read this InputStream

	public C_Connection_r(Socket s, C_buffer b) {
		this.s = s;
		this.buffer = b;

	}

	public void run() {

		// array index for node name and its port
		final int NODE = 0;
		final int PORT = 1;

		String[] request = new String[2];

		System.out.println("C:connection IN    dealing with request from socket " + s);			//Confirms socket creation
		try {

			// >>> read the request, i.e. node ip and port from the socket s
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream())); // gets the InputStream to receive data from Node OutputStream

			// >>> save it in a request object and save the object in the buffer (see C_buffer's methods).
			request[NODE] = in.readLine(); 	// The node IP address
			request[PORT] = in.readLine(); 	// The node port number
			buffer.saveRequest(request); 	// Save this array to the C_buffer

			s.close(); 						// close socket after receiving the data
			// Print confirmation and the data received
			System.out.println("C:connection OUT    received and recorded request from " + request[NODE] + ":"
					+ request[PORT] + "  (socket closed)"); 			

		} catch (java.io.IOException e) { 	// Prints exception if problem with the BufferedReader
			System.out.println("Error: "+ e + " - Cannot get inputstream");
			System.exit(1);
		}

		buffer.show(); 						// Print the contents of the buffer

	} // end of run() method

} // end of class
