package dBalancer.msgProtocol.state;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Coordinator;
import dBalancer.msgProtocol.message.AddMeMessage;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;
import dBalancer.msgProtocol.message.NullMessage;
import dBalancer.overlay.NodeInfo;

public class StateWrapper {
  private State currentState;
  private final NodeInfo currentNode;
  @SuppressWarnings("unused")
  private final Coordinator coo;
  private final StateWrapper st;

  public StateWrapper(final NodeInfo nf, final Boolean client) {
    st = this;
    if(client.equals(Boolean.TRUE)) {
      this.setState(new Initialization());
    }
    else {
      this.setState(new Idle());
    }
    this.coo = Coordinator.getInstance();
    this.currentNode = nf;
  }
  
  public String process(final String msg) {
    Document msgDocument = null;
    String response = null;
    try {
      msgDocument = DocumentHelper.parseText(msg);
      response = this.getState().process(this, msgDocument,
          getMsgClassType(msgDocument)); 
    } catch (DocumentException e) {
      System.err.println("Message received in non XML format");
      response = null;
      e.printStackTrace();
    }
    return response;
  }
  
  public State getState() {
    return this.currentState;
  }
  protected void setState(final State state) {
    this.currentState = state;
  }
  
  private Message getMsgClassType(Document msgDocument) {
    final String typeStr;
    final Message msgClassType;
    
    //find message type in text
    Element root = msgDocument.getRootElement();
    typeStr = root.element("header").getText();
    
    //find and create the actual message class
    if (typeStr.equals("INFOMESSAGE")) {
      msgClassType = new InfoMessage(msgDocument, currentNode);
    } else if (typeStr.equals("ADDMEMESSAGE")) {
      msgClassType = new AddMeMessage(msgDocument, currentNode);
    } else {
      //create a null message
      msgClassType = new NullMessage(msgDocument, currentNode);
    }
    return msgClassType;
  }
  
  public NodeInfo getCurrentNode() {
    return this.currentNode;
  }
  
}
