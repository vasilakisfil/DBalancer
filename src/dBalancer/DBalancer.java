package dBalancer;

import java.net.*;
import java.io.*;

public class DBalancer {
  private Coordinator coo;
  /*private DBalancer copyThis;*/
  
  DBalancer() {
    coo = new Coordinator();
  }
  
  public void start(int port) {
    /*copyThis = this;*/
    //commence server
    Thread s = new Thread(new Server(port));
    s.start();
	
	//Thread d = new Thread(new DebugConsole(copyThis);
    //d.start();
	
    //commence Coordinator
    coo.start();
  }
  public void start(InetAddress IP, int conPort, int serverPort) {
    
    this.startClient(IP, conPort);
 
  }
  
  private void startClient(InetAddress IP, int port) {
    Socket server = null;
    try {
        server = new Socket(IP, port);
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
      System.exit(1);
    }
    
    BufferedReader stdIn = new BufferedReader(
                             new InputStreamReader(System.in) );
    String msg;

    try {
      while ((msg = stdIn.readLine()) != null) {
          out.println(msg);
          System.out.println(in.readLine());
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      out.close();
      try {
        in.close();
      } catch (IOException e) {
        System.out.println("Could not close out buffer");
        e.printStackTrace();
      }
      System.exit(-2);
    } 
  }
  
  public void showLoad() {
    String load = coo.getLoad();
    System.out.println(load);
  }
  
  private class Server implements Runnable {
    ServerSocket server;
    Socket nodeFd;
    int port;
    
    Server(int port) {
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
        /* start a new thread to handle this client */
        Thread t = new Thread(new Node(nodeFd, coo));
        t.start();
      }
    }
  }
  
}
