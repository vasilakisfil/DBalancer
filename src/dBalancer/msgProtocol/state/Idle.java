package dBalancer.msgProtocol.state;

import org.dom4j.Document;

import dBalancer.Helpers;
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

    InfoMessage infoMsg = new InfoMessage();
    return helper.linate(infoMsg.build());
  }

  
}
