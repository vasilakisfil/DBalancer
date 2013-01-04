package dBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import dBalancer.msgProtocol.message.AddMeMessage;
import dBalancer.msgProtocol.state.StateWrapper;
import dBalancer.overlay.NodeInfo;
import dBalancer.overlay.OverlayManager;



public class Node implements Runnable {

  @SuppressWarnings("unused")
  private final Coordinator coo;
  private final Helpers helper;
  private final Socket nodeSd;
  private final StateWrapper msgPrc;
  private final OverlayManager om;
  
  private BufferedReader in;
  private PrintWriter out;
  
  //when this constructor is called by a new node that contacts the other
  // node through the server port, client should be true, otherwise false.
  public Node(final Socket nodeSd, final Boolean client) {
    helper = new Helpers();
	this.nodeSd = nodeSd;
	this.om = OverlayManager.getInstance();
    coo = Coordinator.getInstance();

    try {
      /* obtain an input stream to the node ... */
      in = new BufferedReader(new InputStreamReader(
                                nodeSd.getInputStream() ));
      /* ... and an output stream to the same node */
      out = new PrintWriter(nodeSd.getOutputStream(), true);
    }
    catch (IOException e) {
      System.err.println("Could not get input/output streams");
      System.err.println(e);
    }
    
    //create a NodeInfo object just to hold the address of this node
    NodeInfo nf = new NodeInfo(this.nodeSd.getInetAddress(),
        this.nodeSd.getPort(),
        this.nodeSd, this.out);
    msgPrc = new StateWrapper(nf, client);
  }
  
  //this constructor is called when a new node learns about other nodes
  public Node(final InetAddress IP, final Integer Port, final String nodeID) {
    helper = new Helpers();
    coo = Coordinator.getInstance();
    this.om = OverlayManager.getInstance();
    Socket Sd = null;
    
    
    
    try {
      Sd = new Socket(IP, Port);
    } catch (IOException e) {
      System.err.println("Could not connect to server");
      System.err.println(e);
    } finally {
      this.nodeSd = Sd;
    }
    
    //--> should merge it with the above constructor
    out = null;
    try {
      out = new PrintWriter(this.nodeSd.getOutputStream(), true);
      /* obtain an input stream to the node ... */
      in = new BufferedReader(new InputStreamReader(nodeSd.getInputStream()));
    } catch (IOException e1) {
      System.err.println("Could not create in out buffers");
      e1.printStackTrace();
    }
    /*
    NodeInfo nf = new NodeInfo(this.nodeSd.getInetAddress(),
        this.nodeSd.getPort(),
        this.nodeSd, this.out);
    */
    NodeInfo nf = this.om.getNode(nodeID);
    nf.setNodeSd(nodeSd);
    nf.setOut(out);
    this.om.removeNode(nodeID);
    this.om.addNode(nf);
    
    AddMeMessage msg = new AddMeMessage(null, nf);
    out.println(msg.request());
    msgPrc = new StateWrapper(nf, true);
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
