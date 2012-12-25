package dBalancer;

import java.net.*;
import java.util.Date;
import java.io.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.msgProtocol.message.requestmsg.*;

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
    Socket server = null;
    try {
      server = new Socket(IP, conPort);
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }

    PrintWriter out = null;
    BufferedReader in = null;
    try {
      out = new PrintWriter(server.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(
                                server.getInputStream() ));
    } catch (IOException e1) {
      System.err.println("Could not create in out buffers");
      e1.printStackTrace();
      throw new DBlncrException();
    }
    
    RequestInfoMessage request = new RequestInfoMessage();
    out.println(request.build());
    
    try {
      String msg = in.readLine();
      System.out.println(msg);
    } catch (IOException e) {
      System.out.println("Error from Server. Terminating");
      e.printStackTrace();
      System.exit(1);
    }
    
  }
  
  public void showLoad() {
    String load = coo.getLoad();
    System.out.println(load);
  }
  

  
  
  
  
  private class Server implements Runnable {
    private ServerSocket server;
    private Socket nodeFd;
    private int port;
    
    Server(final int port) {
      this.server = null;
      this.nodeFd = null;
      this.port = port; 
    }
    
    public void run() {
      try {
        server = new ServerSocket(port); /* start listening on the port */
      } catch (IOException e) {
        System.err.println("Could not listen on port: " + port);
        System.err.println(e);
        System.exit(1);
      }
  
      while(true) {
        try {
          nodeFd = server.accept();
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
        Thread t = new Thread(new Node(nodeFd, coo));
        t.start();
      }
    }
  }
  
}
