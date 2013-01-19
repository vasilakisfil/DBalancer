package main.java.org.dbalancer.msgProtocol.message;

import org.dom4j.Document;

import main.java.org.dbalancer.Helpers;

public class NullMessage extends AbstractMessage implements Message {
  @SuppressWarnings("unused")
  private final Helpers helper;
  
  public NullMessage(final Document msgDocument) {
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
  
  public String getSenderNodeID() {
    return "ID";
  }

}
