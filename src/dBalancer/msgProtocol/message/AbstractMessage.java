package dBalancer.msgProtocol.message;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import dBalancer.Helpers;
import dBalancer.overlay.OverlayManager;

public abstract class AbstractMessage {
  @SuppressWarnings("unused")
  private final Helpers helper;
  private final OverlayManager om;
  
  private final Document msgDocument;
  private Element header, body;
  private String recipientID, senderID;
  private Boolean isRequest;
  private Boolean isNull;
  private long msgDate;
  private static final Logger logger = Logger.getLogger(AbstractMessage
                                                        .class
                                                        .getName());
  
  public enum MsgType {
    REQUEST, REPLYREQUEST;
  }
  
  public enum MsgName {
    INFOMESSAGE, ADDMEMESSAGE;
  }
  
  AbstractMessage(final Document msgDocument) {
    helper = new Helpers();
    this.om = OverlayManager.getInstance();
    
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
  protected Element getMsgHeader() { 
    return this.header;
  }
  protected Element getMsgBody() {
    return this.body;
  }
  protected String getRecipientNodeID() {
    return this.recipientID.toString();    
  }
  protected String getSenderNodeID() {
    return this.senderID.toString();
  }
  protected Long getMsgTime() {
    return this.msgDate;
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
    this.recipientID = this.header.attributeValue("recipient-id");
    this.senderID = this.header.attributeValue("sender-id");
    this.msgDate = Long.valueOf(this.header.attributeValue("timestamp"));    
  }
  
  protected Element buildMsgHeader(final Element root, final MsgType type,
      final MsgName name) {
    root.addElement("header")
        .addAttribute("timestamp", String.valueOf(System.currentTimeMillis()))
        .addAttribute("sender-id", this.om.getMyID())
        .addAttribute("recipient-id", "???")
        .addAttribute("type", type.toString())
        .addText( name.toString() );
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
      logger.error("Could not identify message type");
    //throw exception here if smth goes wrong
    } 
  }
  
}
