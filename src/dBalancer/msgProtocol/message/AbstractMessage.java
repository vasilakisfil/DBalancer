package dBalancer.msgProtocol.message;


import java.util.Date;

import org.dom4j.Document;
import org.dom4j.Element;

import dBalancer.Helpers;

public abstract class AbstractMessage {
  @SuppressWarnings("unused")
  private final Helpers helper;
  private final Document msgDocument;
  private Element header, body;
  private String recipient, sender;
  private Boolean isRequest;
  private Boolean isNull;
  private long msgDate;
  
  public enum MsgType {
    REQUEST, REPLYREQUEST;
  }
  
  AbstractMessage(Document msgDocument) {
    helper = new Helpers();
    if (msgDocument != null)
    {
      this.msgDocument = msgDocument;
      this.isNull = false;
      this.decomposeMsgDocument();
      setRequestReplyType();
    } else {
      this.msgDocument = null;
      this.isNull = true;
      this.isRequest = false;
    }
  }
  
  public boolean isNull() {
    return this.isNull.booleanValue();
  }
  
  public boolean isRequest() {
    return this.isRequest.booleanValue();
  }
  
  public boolean isReply() {
    return !this.isRequest.booleanValue();
  }
  
  protected Document getMsgDocument() {
    return this.msgDocument;
  }
  
  private void decomposeMsgDocument() {
    //set header element
    Element root = getMsgDocument().getRootElement();
    this.header = root.element("header");
    
    //set message type
    String type = this.header.attributeValue("type");
    if (type.equals(MsgType.REQUEST.toString())) {
      this.isRequest = true;
    }
    else if (type.equals(MsgType.REPLYREQUEST.toString())) {
      this.isRequest = false;
    }
    
    //set message body element
    this.body = root.element("body");
    
    //set message properties
    this.recipient = this.header.attributeValue("recipient-id");
    this.sender = this.header.attributeValue("sender-id");
    this.msgDate = Long.valueOf(this.header.attributeValue("timestamp"));    
  }
  
  protected Element getMsgHeader() { 
    return this.header;
  }
  
  protected Element buildMsgHeader(Element root, MsgType type) {
    Date date = new Date();
    root.addElement("header")
        .addAttribute("timestamp", String.valueOf(date.getTime()))
        .addAttribute("sender-id", "Client0")
        .addAttribute("recipient-id", "Client1")
        .addAttribute("type", type.toString())
        .addText( "INFOMESSAGE" );
    return root;
  }
  
  private void setRequestReplyType() {
    if(this.header.attribute("type").getValue()
                  .equals(MsgType.REQUEST.toString())) {
      this.isRequest = true;
    } else if (this.header.attribute("type").getValue()
                          .equals(MsgType.REPLYREQUEST.toString())) {
      this.isRequest = false;
    } else {
      System.err.println("Could not identify message type");
    //throw exception here if smth goes wrong
    } 
  }
  
  protected Element getMsgBody() {
    return this.body;
  }
  
  protected String getMsgRecipient() {
    return this.recipient;    
  }
  
  protected String getMsgSender() {
    return this.sender;    
  }
  
  protected Long getMsgTime() {
    return this.msgDate;
  }
}
