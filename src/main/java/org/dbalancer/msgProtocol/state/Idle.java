package main.java.org.dbalancer.msgProtocol.state;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import main.java.org.dbalancer.msgProtocol.message.Message;

public class Idle implements State {
  private String response;
  private static final Logger logger = Logger.getLogger(Idle
                                                        .class
                                                        .getName());
  
  public Idle() {
    logger.info("Idle state");
  }
  
  public String process(final StateWrapper wrapper,
                        final Document msgDocument,
                        final Message msgType) {
    response = msgType.handleMsg();
    return response;
  }

  
}
