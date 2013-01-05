package dBalancer.msgProtocol.state;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Coordinator;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;
import dBalancer.msgProtocol.message.NullMessage;

public class StateWrapper {
  private State currentState;
  @SuppressWarnings("unused")
  private static Coordinator coo;
  private static StateWrapper instance;

  private StateWrapper() {
  }
  
  public static StateWrapper getInstance() {
    if (instance == null) {
      instance = new StateWrapper();
      coo = Coordinator.getInstance();
    }
    return instance;
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
  public void setState(final State state) {
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
      msgClassType = new InfoMessage(msgDocument);
    } else {
      //create a null message
      msgClassType = new NullMessage(msgDocument);
    }
    return msgClassType;
    //return null;
  }
  
}
