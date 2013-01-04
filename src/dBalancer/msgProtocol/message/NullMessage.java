package dBalancer.msgProtocol.message;

import org.dom4j.Document;

import dBalancer.Helpers;
import dBalancer.overlay.NodeInfo;

public class NullMessage extends AbstractMessage implements Message {
  @SuppressWarnings("unused")
  private final Helpers helper;
  
  public NullMessage(Document msgDocument, NodeInfo currentNode) {
    super(msgDocument, currentNode);
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
  
  public String getCurrentNodeID() {
    return "ID";
  }

}
