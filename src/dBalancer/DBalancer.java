package dBalancer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.state.Initialize;



public class DBalancer {
  private Coordinator coo;
  
  public void start(InetAddress IP, int serverPort, boolean debug) {
    coo = new Coordinator(IP, serverPort);
    
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
  
  public void start(InetAddress IP, int remotePort, int serverPort,
                    boolean debug) throws DBlncrException {
    coo = new Coordinator(IP, serverPort);
    
    //initialize (retrieve other servers)
    this.initializeClient(IP, remotePort);
    
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
  
  private void initializeClient(InetAddress IP, int remotePort) throws DBlncrException {
    Socket serverSd = null;
    try {
      serverSd = new Socket(IP, remotePort);
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
    
    InfoMessage msg = new InfoMessage(null);
    out.println(msg.request());
    
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
