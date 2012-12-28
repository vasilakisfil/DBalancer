package dBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import dBalancer.msgProtocol.message.RequestInfoMessage;
import dBalancer.msgProtocol.state.Initialize;



public class DBalancer {
  private Coordinator coo;
  
  public DBalancer() {
    coo = new Coordinator();
  }
  
  public void start(int port, boolean debug) {
    //initiate server
    Thread s = new Thread(new Server(port));
    s.start();
	
    if (debug) {
      Thread d = new Thread(new DebugConsole(coo));
      d.start();
    }
	
    //initiate Coordinator
    coo.start();
  }
  
  public void start(InetAddress IP, int conPort, int serverPort, boolean debug) throws DBlncrException {
    
    //initialize (retrieve other servers)
    this.initializeClient(IP, conPort);
    
    //initiate server
    Thread s = new Thread(new Server(serverPort));
    s.start();
    
    if (debug) {
      Thread d = new Thread(new DebugConsole(coo));
      d.start();
    }
    
    //initiate Coordinator
    coo.start();
    
  }
  
  private void initializeClient(InetAddress IP, int conPort) throws DBlncrException {
    Socket serverSd = null;
    try {
      serverSd = new Socket(IP, conPort);
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }

    PrintWriter out = null;
    try {
      out = new PrintWriter(serverSd.getOutputStream(), true);
    } catch (IOException e1) {
      System.err.println("Could not create in out buffers");
      e1.printStackTrace();
      throw new DBlncrException();
    }
    
    RequestInfoMessage request = new RequestInfoMessage();
    out.println(request.build());
    
    //start new node with specific init state
    //that handles the incoming response
    
    /* start a new thread to handle the new node */
    
    
    Thread t = new Thread(new Node(serverSd, new Initialize() ));
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
        Thread t = new Thread(new Node(nodeSd, null));
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
