package ASSIGNMENT;


import java.util.*;

/* 
 * This is a buffer class with a vector used for receiving node information from the connection class, adding it to the vector, and accessed by the mutex class
 * The nodes hostname and port are saved, this is FIFO order
 */

public class C_buffer {
	
    private Vector<Object> data;
    
    
    public C_buffer (){
	data = new Vector<Object>(); 
    }
    

    //returns vector size
    public int size(){		
	
	return data.size();
    }
    

    //adds String array to vector (IP, port)
    public synchronized void saveRequest (String[] r){		
	
	data.add(r[0]);
	data.add(r[1]);    
    }

  
    //prints the contents of the vector
    public void show(){						
	
	for (int i=0; i<data.size();i++)
	    System.out.print(" "+data.get(i)+" ");
	System.out.println(" ");
    }
    
    
    //add to vector
    public void add(Object o){				
	
	data.add(o);
	
    }
    

    //remove and return from vector (first in first out)
    synchronized public Object  get(){				
	
	Object o = null; 
	
	if (data.size() > 0){
	    o = data.get(0);
	    data.remove(0);
	}
	return o;
    }
    
}
	
	
