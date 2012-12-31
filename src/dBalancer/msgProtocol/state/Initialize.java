package dBalancer.msgProtocol.state;

import org.dom4j.Document;

import dBalancer.Helpers;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;

public class Initialize implements State {
  @SuppressWarnings("unused")
  private final Helpers helper;
  
  public Initialize() {
    helper = new Helpers();
  }

  public String process(final StateWrapper wrapper,
                        final Document msgDocument,
                        final Message msgType) {
    String response = null;

    if (msgType.getClass() == InfoMessage.class)
    {
      response = msgType.handleMsg();
    }
    
    wrapper.setState(new Idle());
    return response;
  }
}
