package dBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DebugConsole implements Runnable {
  private Coordinator coo;
  BufferedReader stdIn = null;
  
  DebugConsole(Coordinator coo) {
    this.coo = coo;
    /* obtain an input stream to the console ... */
    stdIn = new BufferedReader( new InputStreamReader(System.in) );
  }
  
  public void run() {
    String msg = null;
    try {
      while ((msg = stdIn.readLine()) != null) {
        //out.println(msg);
        System.out.println("Executing operation: " + msg);
      }
    } catch (IOException e) {
      System.out.println("Console input failed");
      e.printStackTrace();
      System.exit(1);
    }
    
  }
  

}
