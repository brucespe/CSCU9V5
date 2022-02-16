package ASSIGNMENT;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/*
 * TRAFFIC LIGHTS - 2 of them, token is required to turn green, only one green at a time its mutually exclusive
 * Turns green for a while then returns token, can't go green without token, green light is a critical section
 * 
 * Will perform loop:
 * 		Request token from coordinator, by sending nodes IP and port to coordinator (coordinator listens on port 7000)
 * 		Wait till token is granted by coordinator
 * 		Execute critical section, print green light on, sleep, then green light off, critical section as in case of DME based on token ring
 * 		Return token to coordinator, done by means of synchronisation message as done in case of DME based on a token ring
 * 
 */
public class Node {

	private Random ra; 							// Random generator for delays
	private Socket s; 							// Socket to connect with receiver class to request token

	private PrintWriter pout = null; 			// Send node details using PrintWriter and socket OutputStream

	private ServerSocket n_ss; 					// Node ServerSocket to accept the token
	private Socket n_token; 					// Socket connecting to mutex class to return token

	String c_host = "127.0.0.1"; 				// HOST IP ADDRESS
	int c_request_port = 7000; 					// THE PORT REQUESTING THE TOKEN
	int c_return_port = 7001; 					// THE PORT RETURNING THE TOKEN

	String n_host = "127.0.0.1"; 				// THE HOST IP OF THE NODE
	String n_host_name; 						// THE HOST NAME OF THE NODE
	int n_port; 								// THE PORT OF THE NODE, PASSED FROM ARGUMENTS

	public Node(String nam, int por, int sec) {

		ra = new Random();
		n_host_name = nam;
		n_port = por;

		System.out.println("Node " + n_host_name + ":" + n_port + " of DME is active ....");

		// NODE, i.e. the program for the traffic light sends n_host and n_port
		// through a socket s to the coordinator
		// c_host:c_req_port
		// and immediately opens a server socket through which it will receive
		// a TOKEN (actually just a synchronization).

		while (true) {

			// >>> sleep a random number of seconds linked to the frequency for requesting
			// the token to be able to switch the green light on. You can use the
			// initialisation
			// parameter in milliseconds (see param sec of the constructor).
			
			try {
				Thread.sleep(ra.nextInt(sec)); // sleeps for a random amount of time
			} catch (InterruptedException e1) {
				System.out.println("Exception attempting random delay - " + e1);
			}

			try {


				// **** Send to the coordinator a token request.
				// send your ip address and port number

				s = new Socket(c_host, c_request_port);
				System.out.println("Sent the coordinator a token request");
				OutputStream os = s.getOutputStream();
				pout = new PrintWriter(os, true);

				//writing to PrintWriter
				pout.write(n_host);
				pout.println();									//new line to allow reading from InputStream
				String output = Integer.toString(n_port);		//requires awkward casting to work
				pout.write(output);
				//pout.write(n_port);
				pout.close();


				// >>>
				// **** Wait for the token 
				// this is just a synchronization
				// Print suitable messages;
				n_ss = new ServerSocket(n_port);
				n_ss.accept();						//node receives token
				System.out.println("Token has been received - Critical section has began");

				// >>>
				// Switch the green light on (print suitable message - see instructions)
				// Sleep a bit, a second say.
				// Switch the light off.
				// This is the critical session
				
				Date start = new Date();
				DateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
				String timeStart = sdf.format(start);						//printing out the time to ensure only one node is in critical section

				System.out.println("GREEN LIGHT: ON - " + timeStart);
				try {
					Thread.sleep(ra.nextInt(sec)); 
				} catch (InterruptedException e2) {
					System.out.println("Exception attempting random delay - " + e2);
				}
				
				Date end = new Date();
				String timeEnd = sdf.format(end);
				System.out.println("GREEN LIGHT: OFF- " + timeEnd);

				// >>> USING THE MUTEX SOCKET RETURN THE TOKEN
				// **** Return the token
				// this is just establishing a synch connection to the coordinator's ip and return port.
				// Print suitable messages - also considering communication failures
				n_token = new Socket(n_host_name, c_return_port);		//node returns token
				System.out.println("Node has returned the token - Critical section has ended");
				n_ss.close();
				n_token.close();

			} catch (java.io.IOException e3) {
				System.out.println("Node has failed to enter the critical section - " + e3);
				System.exit(1);
			}
		}
	}

	public static void main(String args[]) {

		String n_host_name = "";
		int n_port;

		// port and millisec (average waiting time) are specific of each node
		if ((args.length < 1) || (args.length > 2)) {
			System.out.print("Usage: Node [port number] [millisecs]");
			System.exit(1);
		}

		// get the IP address and the port number of the node
		try {
			InetAddress n_inet_address = InetAddress.getLocalHost();
			n_host_name = n_inet_address.getHostName();
			System.out.println("node hostname is " + n_host_name + ":" + n_inet_address);
		} catch (java.net.UnknownHostException e) {
			System.out.println(e);
			System.exit(1);
		}

		n_port = Integer.parseInt(args[0]);
		System.out.println("node port is " + n_port);

		Node n = new Node(n_host_name, n_port, Integer.parseInt(args[1]));
	}

}
