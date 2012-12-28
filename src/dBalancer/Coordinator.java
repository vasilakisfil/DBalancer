package dBalancer;

import java.net.Socket;



public class Coordinator {
  
  private static Coordinator coo;
  //private Socket client;

  Coordinator() {
    Coordinator.coo = this;
    
  }
  
  Coordinator(final Socket client) {
      //this.client = client;
  }

  static public Coordinator getInstance() {
    return coo;
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
