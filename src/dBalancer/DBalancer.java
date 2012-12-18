package dBalancer;

import java.net.*;
import java.io.*;

public class DBalancer {
  Coordinator co;
  
  DBalancer() {
    co = new Coordinator();
    
  }
  
  public void start(int port) {
    //commence server
    Thread t = new Thread(new Server(port));
    t.start();
    
    //commence Coordinator
    co.start();
  }
  
  public void connectToServer(InetAddress IP, int port) throws IOException {
    Socket server = null;
    
    try {
        /* try to open a socket to the server at the given host:port */
        server = new Socket(IP, port);
    } catch (UnknownHostException e) {
        System.err.println(e);
        System.exit(1);
    }

    /* obtain an output stream to the server... */
    PrintWriter out = new PrintWriter(server.getOutputStream(), true);
    /* ... and an input stream */
    BufferedReader in = new BufferedReader(new InputStreamReader(
                server.getInputStream()));
    /* stdin stream */
    BufferedReader stdIn = new BufferedReader(
            new InputStreamReader(System.in));

    String msg;

    /* loop reading messages from stdin, send them to the server 
     * and read the server's response */
    while ((msg = stdIn.readLine()) != null) {
        out.println(msg);
        System.out.println(in.readLine());
    }
  }
  
  
  
  private class Server implements Runnable {
    ServerSocket server;
    Socket client;
    int port;
    
    Server(int port) {
      this.server = null;
      this.client = null;
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
          client = server.accept();
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
        Thread t = new Thread(new Coordinator(client));
        t.start();
      }
    }
  }
  
}
