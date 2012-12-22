package dBalancer;

import java.net.Socket;



public class Coordinator {

  private Socket client;

  Coordinator() {
    
  }
  
  Coordinator(final Socket client) {
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
  
  public String getLoad() {
    String load = "Empty Load";
    return load;
  }

}
