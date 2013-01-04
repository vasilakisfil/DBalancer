package dBalancer.msgProtocol.state;

import org.dom4j.Document;

import dBalancer.Helpers;
import dBalancer.msgProtocol.message.AddMeMessage;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;

public class Idle implements State {
  private final Helpers helper;
  
  public Idle() {
    helper = new Helpers();
  }
  

  public String process(final StateWrapper wrapper,
                        final Document msgDocument,
                        final Message msgType) {

    
    System.out.println("I am in IDLE STATE");
    return msgType.handleMsg();
  }

  
}
