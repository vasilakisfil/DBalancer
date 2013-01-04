package dBalancer.msgProtocol.message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.DBlncrException;
import dBalancer.Helpers;
import dBalancer.msgProtocol.message.AbstractMessage.MsgType;
import dBalancer.overlay.NodeInfo;
import dBalancer.overlay.OverlayManager;

public class AddMeMessage extends AbstractMessage implements Message {
  private final Helpers helper;
  private final OverlayManager om;
  
  public AddMeMessage(Document msgDocument, String nodeID) {
    super(msgDocument, nodeID);
    this.helper = new Helpers();
    this.om = OverlayManager.getInstance();
  }
  
  public AddMeMessage(Document msgDocument, NodeInfo currentNode) {
    super(msgDocument, currentNode);
    this.helper = new Helpers();
    this.om = OverlayManager.getInstance();
  }
  /*
  public AddMeMessage(Document msgDocument, NodeInfo currentNode, Socket sd)
      throws DBlncrException {
    super(msgDocument, currentNode);
    this.helper = new Helpers();
    this.om = OverlayManager.getInstance();
    
    PrintWriter out = null;
    try {
      out = new PrintWriter(sd.getOutputStream(), true);
    } catch (IOException e1) {
      System.err.println("Could not create in out buffers");
      e1.printStackTrace();
      throw new DBlncrException();
    }
  }
*/
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
    return helper.linate(this.createAddMeInfo(MsgType.REQUEST).asXML());
  }   
  
  @Override
  public String replyRequest() {
    NodeInfo nf = this.createNodeInfo();
    this.om.addNode(nf);
    System.out.println("AddMeReply" + this.om.showAllNodes());
    return helper.linate(this.createAddMeInfo(MsgType.REPLYREQUEST).asXML());
  }
  
  @Override
  public void handleReplyRequest() {
    NodeInfo nf = this.createNodeInfo();
    this.om.addNode(nf);
    System.out.println("AddMeHandle" + this.om.showAllNodes());
  }
  
  public String getCurrentNodeID() {
    return super.getCurrentNodeID();
  }
  
  
  private NodeInfo createNodeInfo() {
    InetAddress IP = null;
    Integer serverPort = null;
    String nodeID = null;
    
    Element node = super.getMsgBody().element("node");
    try {
      IP = InetAddress.getByName(node.attributeValue("IP"));
    } catch (UnknownHostException e) {
      System.err.println("Fatal error: could not recognise IP");
      e.printStackTrace();
    }
    serverPort = Integer.valueOf(node.attributeValue("serverport"));
    nodeID = node.attributeValue("nodeID");
    
    NodeInfo nf = new NodeInfo(IP, serverPort,
                                super.getCurrentNodePort(),
                                super.getCurrentNodeSd(), 
                                super.getCurrentNodeOut(),
                                nodeID);
    return nf;
  }
  
  private Document createAddMeInfo(MsgType type) {
    Document request = DocumentHelper.createDocument();
    Element root = request.addElement( "DBalancerMsg" );
    
    root = super.buildMsgHeader(root, type, MsgName.ADDMEMESSAGE);

    Element body = root.addElement("body");
    body.addElement("node")
        .addAttribute("nodeID", this.om.getMyID())
        .addAttribute("IP", this.om.getMyStringIP())
        .addAttribute("serverport", this.om.getMyPort().toString());
    
    return request;
  }
}