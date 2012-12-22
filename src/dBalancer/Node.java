package dBalancer;

import java.io.*;
import java.net.*;

import dBalancer.msg_protocol.MessageProtocol;



public class Node implements Runnable {

  private Coordinator coo;
  private Socket nodeFd;
  private MessageProtocol msgPrc;
  
  Node(final Socket nodeFd, final Coordinator coo) {
	this.nodeFd = nodeFd;
	this.coo = coo;
	msgPrc = new MessageProtocol();
  }
  
  public void run() {
    BufferedReader in = null;
    PrintWriter out = null;
    try {
      /* obtain an input stream to the node ... */
      in = new BufferedReader(new InputStreamReader(
                                nodeFd.getInputStream() ));
	  /* ... and an output stream to the same node */
      out = new PrintWriter(nodeFd.getOutputStream(), true);
    }
    catch (IOException e) {
	  System.err.println(e);
	  return;
    }

    String msg;
    String response = null;
    try {
      while ((msg = in.readLine()) != null) {
		System.out.println("Client says: " + msg);
		out.println("OK");
		response = msgPrc.process(msg);
      }
    }
    catch (IOException e) {
      System.out.println("Error reading from client");
      System.err.println(e);
    }
  }
  
  


}
