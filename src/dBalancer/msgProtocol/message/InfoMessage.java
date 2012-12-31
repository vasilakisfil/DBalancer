package dBalancer.msgProtocol.message;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Helpers;

public class InfoMessage extends AbstractMessage implements Message {
  private final Helpers helper;
  
  public InfoMessage(Document msgDocument) {
    super(msgDocument);
    helper = new Helpers();
  }
  
  public String handleMsg() {
    String tmpReturn = null;
    if (super.isNull()) {
      tmpReturn = this.request();
    }
    else if (super.isRequest()) {
      tmpReturn = this.replyRequest();
    }
    else if (super.isReply()) {
      this.handleReplyRequest();
      tmpReturn = null;
    }
    else {
      System.err.println("Fatal Error");
    }
    return tmpReturn;
  }
  
  @Override
  public String request() {

    Document request = DocumentHelper.createDocument();
    Element root = request.addElement( "DBalancerMsg" );
    
    root = super.buildMsgHeader(root, MsgType.REQUEST);

    Element body = root.addElement("body")
                        .addText("request");
    
    return helper.linate(request.asXML());
  }   
  
  @Override
  public String replyRequest() {

    Document response = DocumentHelper.createDocument();
    Element root = response.addElement( "DBalancerMsg" );
    //enums are static in Java
    root = super.buildMsgHeader(root, MsgType.REPLYREQUEST);
    
    Element body = root.addElement("body")
        .addText("replyrequest");
    
    return helper.linate(response.asXML());
  }
  
  @Override
  public void handleReplyRequest() { 
  }
}