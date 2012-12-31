package dBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import dBalancer.msgProtocol.state.StateWrapper;
import dBalancer.msgProtocol.state.State;



public class Node implements Runnable {

  @SuppressWarnings("unused")
  private final Coordinator coo;
  @SuppressWarnings("unused")
  private final Socket nodeFd;
  private final StateWrapper msgPrc;
  
  private BufferedReader in;
  private PrintWriter out;
  
  Node(final Socket nodeFd, final State state) {
	this.nodeFd = nodeFd;
    coo = Coordinator.getInstance();

    try {
      /* obtain an input stream to the node ... */
      in = new BufferedReader(new InputStreamReader(
                                nodeFd.getInputStream() ));
      /* ... and an output stream to the same node */
      out = new PrintWriter(nodeFd.getOutputStream(), true);
    }
    catch (IOException e) {
      System.err.println("Could not get input/output streams");
      System.err.println(e);
    }
    
    if ( state != null ) {
      msgPrc = new StateWrapper(state);
    } else {
      msgPrc = new StateWrapper();
    }
  }
  
  public void run() {

    String response;
    String msg=null;
    try {
      while (( msg = in.readLine()) != null) {
        System.out.println(msg); //move that to logger
        if ( (response = msgPrc.process(msg)) != null ) {
		  out.println(response);
        }
        msg = null;
      }
    }
    catch (IOException e) {
      System.err.println("Error reading from client");
      System.err.println(e);
    }

  }
  
  


}
