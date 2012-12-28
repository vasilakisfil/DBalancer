package dBalancer.msgProtocol.state;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;
import dBalancer.msgProtocol.state.Idle;
import dBalancer.msgProtocol.state.State;

public class StateWrapper {
  private State currentState;
  //private final Coordinator coo;

  public StateWrapper() {
    this.setState(new Idle());
    //this.coo = Coordinator.getInstance();
  }
  
  public StateWrapper(final State state) {
    this.setState(state);
    //this.coo = Coordinator.getInstance();
  }
  
  public String process(final String msg) {
    System.out.println(msg);
    Document msgDocument = null;
    String response = null;
    try {
      msgDocument = DocumentHelper.parseText(msg);
    } catch (DocumentException e) {
      // HANDLE ERROR HERE
      response = null;
      e.printStackTrace();
    }
    response = this.getState().process(this, msgDocument,
                                        getMsgType(msgDocument));

    
    return response;
  }
  
  public State getState() {
    return this.currentState;
  }
  
  protected void setState(final State state) {
    this.currentState = state;
  }
  
  private Message getMsgType(Document msgDocument) {
    
    return messageClassType(messageStrType(msgDocument));
  }
  
  private Message messageClassType(String typeStr) {
    final Message msgClassType;    
    
    if (typeStr.equals("INFOMESSAGE"))
    {
      msgClassType = new InfoMessage();
    } else {
      msgClassType = null;
    }
    return msgClassType;
  }
  
  private String messageStrType(Document msgDocument) {
    final String msgType;
    Element root = msgDocument.getRootElement();
    Element type = root.element("type");
    msgType = type.getText();
    return msgType;
  }
  
}
