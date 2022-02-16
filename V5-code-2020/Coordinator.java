package ASSIGNMENT;

import java.net.*;




/*
 * Centralised DME based on token ring with class implementing ring node
 * STARTING POINT - passes unique token to each one of traffic lights exclusively
 * Sockets used for communication and synchronisation
 * Port must be instantiated at launch time as parameter passed to node
 * 
 * Granting token implemented by simple synchronisation by coordinator on the nodes IP port
 * 
 * Coordinator consists of two concurrent tasks that share a buffer data structure (class C_buffer) 
 */
public class Coordinator {
	
    public static void main (String args[]){
	
	int port = 7000;					//Port to receive requests from node
	
	Coordinator c = new Coordinator ();
	
	try {    														//Prints some information about the current host address and name
	    InetAddress c_addr = InetAddress.getLocalHost();
	    String c_name = c_addr.getHostName();
	    System.out.println ("Coordinator address is "+c_addr);
	    System.out.println ("Coordinator host name is "+c_name+"\n\n");    
	}
	catch (Exception e) {											//If unable to get host information
	    System.err.println(e);
	    System.err.println("Error in coorrdinator, cannot get host information");
	}
	
	
	// allows defining port at launch time
	if (args.length == 1) port = Integer.parseInt(args[0]);
     

	C_buffer b = new C_buffer ();					//Creates buffer object
	C_receiver crec = new C_receiver(b, port);		//Creates instance of C_receiver class, passing the buffer and the port to receives node requests
	C_mutex  mtx =new C_mutex(b, port+1);			//Creates instance of C_mutex class, passing buffer and the port to receive the returned token from node


	// >>> run the C_receiver and C_mutex object sharing a C_buffer object	
	crec.start();
	mtx.start();		//runs crec and mtx instance concurrently 

	
    }
    
}
