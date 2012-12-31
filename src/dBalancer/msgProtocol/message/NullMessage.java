package dBalancer.msgProtocol.message;

import org.dom4j.Document;

import dBalancer.Helpers;

public class NullMessage extends AbstractMessage implements Message {
  @SuppressWarnings("unused")
  private final Helpers helper;
  
  public NullMessage(Document msgDocument) {
    super(msgDocument);
    helper = new Helpers();
  }

  @Override
  public String request() {
    return null;
  }

  @Override
  public String replyRequest() {
    return null;
  }

  @Override
  public void handleReplyRequest() {
    
  }

  @Override
  public String handleMsg() {
    return null;
  }

}
