package dBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import dBalancer.msgProtocol.message.AddMeMessage;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.state.Idle;
import dBalancer.msgProtocol.state.Initialization;
import dBalancer.msgProtocol.state.StateWrapper;
import dBalancer.overlay.OverlayManager;



public class DBalancer {
  private Coordinator coo;
  private OverlayManager om;
  private static final Logger logger = Logger.getLogger(DBalancer
                                                        .class
                                                        .getName());
  public DBalancer() {
    this.om = OverlayManager.getInstance();
    this.coo = Coordinator.getInstance();
    // BasicConfigurator replaced with PropertyConfigurator.
    PropertyConfigurator.configure("../log4j.properties");
  }
  
  public void start(InetAddress IP, Integer serverPort, Boolean debug) {
    this.om.setMyInfo(IP, serverPort);
    logger.info("Set myInfo\n" + this.om.getMyInfo());
    this.initialize(IP, serverPort, debug);
    //initiate Coordinator
    coo.start();
  }
  
  public void start(InetAddress IP, Integer remotePort, Integer serverPort,
                    Boolean debug) throws DBlncrException {
    this.om.setMyInfo(IP, serverPort);
    //initialize (retrieve other servers)
    this.initializeClient(IP, remotePort);
    this.initialize(IP, serverPort, debug);
    //initiate Coordinator
    coo.start();
    
  }
  
  public void initialize(InetAddress IP, Integer serverPort, Boolean debug) {
    //initiate server
    Thread s = new Thread(new Server(serverPort));
    s.start();
    
    if (debug) {
      Thread d = new Thread(new DebugConsole(coo));
      d.start();
    }
  }
  
  private void initializeClient(InetAddress IP, Integer remotePort)
      throws DBlncrException {
    Socket nodeSd = null;
    BufferedReader in = null;
    PrintWriter out = null;
    Document msgDocument = null;
    
    
    try {
      nodeSd = new Socket(IP, remotePort);
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }

    try {
      /* obtain an input stream to the node ... */
      in = new BufferedReader(new InputStreamReader(
                                nodeSd.getInputStream() ));
      out = new PrintWriter(nodeSd.getOutputStream(), true);
    } catch (IOException e1) {
      logger.error("Could not create in out buffers");
      e1.printStackTrace();
      throw new DBlncrException();
    }
    //send add message
    AddMeMessage request = new AddMeMessage(null);
    out.println(request.request());
    
    //get response with ID
    try {
      msgDocument = DocumentHelper.parseText(in.readLine());
    } catch (DocumentException e) {
      logger.error("Message received in non XML format");
      e.printStackTrace();
    } catch (IOException e) {
      logger.error("Message could not be recieved");
      e.printStackTrace();
    }
    AddMeMessage response = new AddMeMessage(msgDocument);
    
    //create and add node
    response.processAdd(IP, remotePort, nodeSd, out);
 
    //initialize state wrapper
    StateWrapper st = StateWrapper.getInstance();
    st.setState(Initialization.getInstance());

    //request node list
    InfoMessage im = new InfoMessage(null);
    this.om.dispatchMsg(im.request(), response.getSenderNodeID());
    
    /* start a new thread to handle the new node */
    Thread t = new Thread(new Node(response.getSenderNodeID()));
    t.start();
    
    //start server etc
  }
  
  public void showLoad() {
    String load = coo.getLoad();
    System.out.println(load);
  }

  private class Server implements Runnable {
    private final ServerSocket server;
    private final int port;
    
    private Socket nodeSd;
    
    Server(final int port) {
      this.nodeSd = null;
      this.port = port; 
      
      server = this.initServer();
    }
    
    public void run() {
      logger.info("Server Started");
      while(true) {
        try {
          nodeSd = server.accept();
        }
        catch (IOException e) {
          logger.error("Accept failed.");
          logger.error(e);
        }
        /* start a new thread to handle the new node */
        //Thread t = new Thread(new Node(nodeSd, false));
        Thread t = new Thread(new HandleNewNode(nodeSd.getInetAddress(),
                                                nodeSd.getLocalPort(),
                                                nodeSd));
        t.start();
      }
    }
    
    private ServerSocket initServer() {
      try {
        return new ServerSocket(port); /* start listening on the port */
      } catch (IOException e) {
        logger.fatal("Could not listen on port: " + port);
        logger.fatal(e);
        System.exit(1);
        return null; //compiler necessity
      }
    }
    
    private class HandleNewNode implements Runnable {
      InetAddress IP;
      Integer remotePort;
      Socket nodeSd;
      PrintWriter out;
      BufferedReader in;
      Document msgDocument;
      
      HandleNewNode(InetAddress IP, Integer remotePort, Socket nodeSd) {
        this.IP = IP;
        this.remotePort = remotePort;
        this.nodeSd = nodeSd;
      }

      @Override
      public void run() {
        //get in, out
        logger.info("Received new node request");
        try {
          this.in = new BufferedReader(new InputStreamReader(
                                        this.nodeSd.getInputStream() ));
          this.out = new PrintWriter(this.nodeSd.getOutputStream(), true);
        } catch (IOException e1) {
          logger.error("Could not create in out buffers");
          logger.error(e1);
        }
        
        //receive add message with ID
        try {
          msgDocument = DocumentHelper.parseText(in.readLine());
        } catch (DocumentException e) {
          logger.error("Message received in non XML format");
          logger.error(e);
        } catch (IOException e) {
          logger.error("Message could not be recieved");
          logger.error(e);
        }
        //process received add message
        AddMeMessage respond = new AddMeMessage(msgDocument);
        //send response
        this.out.println(respond.processAddResponse(this.IP, this.remotePort,
                                              this.nodeSd, this.out));
        //initialize state wrapper
        StateWrapper st = StateWrapper.getInstance();
        st.setState(new Idle());
        
        //set new node to handle this node
        Node nf = new Node(respond.getSenderNodeID());
        nf.run();        
      }
      
    }
  }
}
