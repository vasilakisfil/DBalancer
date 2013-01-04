package dBalancer.msgProtocol.message;


import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.Element;

import dBalancer.Helpers;
import dBalancer.overlay.NodeInfo;
import dBalancer.overlay.OverlayManager;

public abstract class AbstractMessage {
  @SuppressWarnings("unused")
  private final Helpers helper;
  private final OverlayManager om;
  private final NodeInfo currentNode;
  
  private final Document msgDocument;
  private Element header, body;
  private String recipient, sender;
  private Boolean isRequest;
  private Boolean isNull;
  private long msgDate;
  
  public enum MsgType {
    REQUEST, REPLYREQUEST;
  }
  
  public enum MsgName {
    INFOMESSAGE, ADDMEMESSAGE;
  }
  
  AbstractMessage(Document msgDocument, String nodeID) {
    helper = new Helpers();
    this.om = OverlayManager.getInstance();
    this.currentNode = this.om.getNode(nodeID);
    
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
  
  AbstractMessage(Document msgDocument, NodeInfo currentNode) {
    helper = new Helpers();
    this.om = OverlayManager.getInstance();
    this.currentNode = currentNode;
    
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
  
  protected InetAddress getCurrentNodeIP() {
    return this.currentNode.getIP();
  }
  protected String getCurrentNodeStringIP() {
    return this.currentNode.getStringIP();
  }
  protected Integer getCurrentNodePort() {
    return this.currentNode.getServerPort();
  }
  protected Socket getCurrentNodeSd() {
    return this.currentNode.getSd();
  }
  protected PrintWriter getCurrentNodeOut() {
    return this.currentNode.getOut();
  }
  protected NodeInfo getCurrentPartlyNode() {
    return this.currentNode;
  }
  protected String getCurrentNodeID() {
    return this.sender.toString();
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
  protected String getMsgRecipient() {
    return this.recipient.toString();    
  }
  protected String getMsgSender() {
    return this.sender.toString();    
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
    this.recipient = this.header.attributeValue("recipient-id");
    this.sender = this.header.attributeValue("sender-id");
    this.msgDate = Long.valueOf(this.header.attributeValue("timestamp"));    
  }
  
  protected Element buildMsgHeader(Element root, MsgType type, MsgName name) {
    Date date = new Date();
    root.addElement("header")
        .addAttribute("timestamp", String.valueOf(date.getTime()))
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
      System.err.println("Could not identify message type");
    //throw exception here if smth goes wrong
    } 
  }
}
