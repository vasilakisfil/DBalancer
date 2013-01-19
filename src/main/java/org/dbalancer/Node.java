package main.java.org.dbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import main.java.org.dbalancer.msgProtocol.state.StateWrapper;
import main.java.org.dbalancer.overlay.OverlayManager;



public class Node implements Runnable {

  @SuppressWarnings("unused")
  private final Coordinator coo;
  @SuppressWarnings("unused")
  private final Helpers helper;
  private final StateWrapper stateWrapper;
  private final OverlayManager om;
  private final String nodeID;
  private static final Logger logger = Logger.getLogger(Node
                                                        .class
                                                        .getName());
  
  private BufferedReader in;
  private PrintWriter out;
  
  //when this constructor is called by a new node that contacts the other
  // node through the server port, client should be true, otherwise false.
  public Node(final String nodeID) {
    this.stateWrapper = StateWrapper.getInstance();
    this.helper = new Helpers();
	this.om = OverlayManager.getInstance();
    this.coo = Coordinator.getInstance();
    this.nodeID = nodeID;
    
    
    try {
      /* obtain an input stream to the node ... */
      in = new BufferedReader(new InputStreamReader(
          this.om.getNode(nodeID).getSd().getInputStream() ));
      /* ... and an output stream to the same node */
      out = this.om.getNode(nodeID).getOut();
    }
    catch (IOException e) {
      logger.error("Could not get input/output streams");
      logger.error(e);
    }
  }
  
  public void run() {
    String response;
    String msg=null;
    
    logger.info("Started serving node " + nodeID);
    
    try {
      while (( msg = in.readLine()) != null) {
        logger.info("Received new message from " + nodeID + "\n" + msg);
        if ( (response = stateWrapper.process(msg)) != null ) {
		  out.println(response);
        }
        msg = null;
      }
    }
    catch (IOException e) {
      logger.error("Error reading from client");
      logger.error(e);
    }

  }
}
