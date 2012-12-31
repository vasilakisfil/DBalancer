package dBalancer;

import java.net.InetAddress;
import java.net.Socket;

import dBalancer.overlay.OverlayManager;



public class Coordinator {
  
  private static Coordinator coo;
  private OverlayManager om;

  Coordinator(InetAddress IP, int serverport) {
    Coordinator.coo = this;
    om = new OverlayManager();
    
    
  }

  static public Coordinator getInstance() {
    return Coordinator.coo;
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
