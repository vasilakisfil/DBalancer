package main.java.org.dbalancer;

import org.apache.log4j.Logger;

import main.java.org.dbalancer.overlay.OverlayManager;


public class Coordinator {
  private static Coordinator instance;
  @SuppressWarnings("unused")
  private static OverlayManager om;
  private static final Logger logger = Logger.getLogger(Coordinator
                                                        .class
                                                        .getName());
  Coordinator() {
  }

  static public Coordinator getInstance() {
    if (instance == null) {
      instance = new Coordinator();
      om = OverlayManager.getInstance();
    }
    return instance;
  }
  
  public void start() {
      try {
        while(true) {
          logger.info("Sleeping");
          Thread.sleep(4000);
        }
      } catch (InterruptedException e) {
        logger.fatal("Main thread Interrupted");
        logger.fatal(e);
      } finally {
        System.exit(1);
      }
  }
  
  public String getLoad() {
    String load = "Empty Load";
    return load;
  }

}
