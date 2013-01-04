package dBalancer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import dBalancer.msgProtocol.message.AddMeMessage;
import dBalancer.overlay.NodeInfo;



public class DBalancer {
  private Coordinator coo;
  
  public void start(InetAddress IP, Integer serverPort, Boolean debug) {
    this.initialize(IP, serverPort, debug);
    //initiate Coordinator
    coo.start();
  }
  
  public void start(InetAddress IP, Integer remotePort, Integer serverPort,
                    Boolean debug) throws DBlncrException {
    this.initialize(IP, serverPort, debug);
    //initialize (retrieve other servers)
    this.initializeClient(IP, remotePort);
    //initiate Coordinator
    coo.start();
    
  }
  
  public void initialize(InetAddress IP, Integer serverPort, Boolean debug) {
    coo = new Coordinator(IP, serverPort);
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
    try {
      nodeSd = new Socket(IP, remotePort);
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }

    PrintWriter out = null;
    try {
      out = new PrintWriter(nodeSd.getOutputStream(), true);
    } catch (IOException e1) {
      System.err.println("Could not create in out buffers");
      e1.printStackTrace();
      throw new DBlncrException();
    }
    
    NodeInfo nf = new NodeInfo(IP, remotePort, remotePort, nodeSd, out, "");
    AddMeMessage msg = new AddMeMessage(null, nf);
    out.println(msg.request());
    
    /* start a new thread to handle the new node */
    Thread t = new Thread(new Node(nodeSd, true ));
    t.start();
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
      
      while(true) {
        try {
          nodeSd = server.accept();
        }
        catch (IOException e) {
          try {
            server.close();
          } catch (IOException e1) {
            System.err.println("Server close failed.");
            e1.printStackTrace();
          }
          System.err.println("Accept failed.");
          System.err.println(e);
          System.exit(1);
        }
        /* start a new thread to handle the new node */
        Thread t = new Thread(new Node(nodeSd, false));
        t.start();
      }
    }
    
    private ServerSocket initServer() {
      try {
        return new ServerSocket(port); /* start listening on the port */
      } catch (IOException e) {
        System.err.println("Could not listen on port: " + port);
        System.err.println(e);
        System.exit(1);
        return null; //compiler necessity
      }      
    }    
  }  
}
