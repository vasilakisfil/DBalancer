package dBalancer.msgProtocol.state;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import dBalancer.Helpers;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;
import dBalancer.overlay.OverlayManager;

public class Initialization implements State {
  @SuppressWarnings("unused")
  private final Helpers helper;
  private static Initialization instance;
  @SuppressWarnings("unused")
  private static OverlayManager om;
  private StateEnum internalState;
  private static final Logger logger = Logger.getLogger(Initialization
                                                        .class
                                                        .getName());  
  private enum StateEnum {
    RECEIVENODES
  }
  
  private Initialization() {
    helper = new Helpers();
    this.internalState = StateEnum.RECEIVENODES;
    logger.info("Initialization State");
  }
  
  public static Initialization getInstance() {
    if (instance == null) {
      instance = new Initialization();
      om = OverlayManager.getInstance();
    }
    return instance;
  }

  public String process(final StateWrapper wrapper,
                        final Document msgDocument,
                        final Message msgType) {
    String response = null;
    
    if(internalState.equals(StateEnum.RECEIVENODES)) {
      if (msgType.getClass().equals(InfoMessage.class)) {
        InfoMessage msg = (InfoMessage) msgType;
        //retrieve other nodes
        msg.handleMsg();
        msg.connectToNodes();
        //go to IDLE state
        wrapper.setState(new Idle());
      }
    }

    return response;
  }
}
