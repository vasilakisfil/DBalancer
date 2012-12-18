package dBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Coordinator implements Runnable {

  private Socket client;

  Coordinator() {
    
  }
  
  Coordinator(Socket client) {
      this.client = client;
  }

  
  public void start() {

      try {
        while(true) {
          System.out.println("Sleeping");
          Thread.sleep(4000);
        }
      } catch (InterruptedException e) {
        System.out.println("Main thread Interrupted");
        e.printStackTrace();
      } finally {
        System.exit(1);
      }
  }
  
  public void run() {
    BufferedReader in = null;
    PrintWriter out = null;
    try {
      /* obtain an input stream to this client ... */
      in = new BufferedReader(new InputStreamReader(
                                client.getInputStream() ));
        /* ... and an output stream to the same client */
      out = new PrintWriter(client.getOutputStream(), true);
    }
    catch (IOException e) {
        System.err.println(e);
        return;
    }

    String msg;
    try {
      /* loop reading messages from the client, 
      * output to stdin and send back an "OK" back */
      while ((msg = in.readLine()) != null) {
          System.out.println("Client says: " + msg);
          out.println("OK");
      }
    }
    catch (IOException e) {
      System.err.println(e);
    }
  }
  
  public String getLoad() {
    String load = "Empty Load";
    return load;
  }

}
