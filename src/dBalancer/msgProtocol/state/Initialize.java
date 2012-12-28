package dBalancer.msgProtocol.state;

import org.dom4j.Document;

import dBalancer.Helpers;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;

public class Initialize implements State {
  private final Helpers helper;
  
  public Initialize() {
    helper = new Helpers();
  }

  public String process(final StateWrapper wrapper,
                        final Document msgDocument,
                        final Message msgType) {

    //msgType.handle();
    wrapper.setState(new Idle());
    return null;
  }
}
