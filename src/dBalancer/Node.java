package dBalancer;

import java.io.*;
import java.net.*;



public class Node implements Runnable {

  private Coordinator coo;
  private Socket nodeFd;
  
  Node(Socket nodeFd, Coordinator coo) {
	this.nodeFd = nodeFd;
	this.coo = coo;
  }
  
  public void run() {
    BufferedReader in = null;
    PrintWriter out = null;
    try {
      /* obtain an input stream to this client ... */
      in = new BufferedReader(new InputStreamReader(
                                nodeFd.getInputStream() ));
	  /* ... and an output stream to the same client */
      out = new PrintWriter(nodeFd.getOutputStream(), true);
    }
    catch (IOException e) {
	  System.err.println(e);
	  return;
    }

    String msg;
    try {
      /* loop reading messages from the client, 
	   * output to stdin and send back an "OK" back */
      while ((msg = in.readLine()) != null) {
		System.out.println("Client says: " + msg);
		out.println("OK");
      }
    }
    catch (IOException e) {
      System.err.println(e);
    }
  }
  
  


}
