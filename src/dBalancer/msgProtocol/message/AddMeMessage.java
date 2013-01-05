package dBalancer.msgProtocol.message;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Helpers;
import dBalancer.overlay.NodeInfo;
import dBalancer.overlay.OverlayManager;

//the only message that does not implement the Message interface
public class AddMeMessage extends AbstractMessage {
  private final Helpers helper;
  private final OverlayManager om;
  private String nodeID;
  private static final Logger logger = Logger.getLogger(AddMeMessage
                                                        .class
                                                        .getName());
  
  public AddMeMessage(Document msgDocument) {
    super(msgDocument);
    this.helper = new Helpers();
    this.om = OverlayManager.getInstance();
  }  

  public String request() {
    return helper.linate(this.createAddMeInfo(MsgType.REQUEST).asXML());
  }
  
  public void processAdd(InetAddress IP, Integer serverPort, Socket nodeSd,
      PrintWriter out) {
    String nodeID;
    
    Element node = super.getMsgBody().element("node");
    try {
      IP = InetAddress.getByName(node.attributeValue("IP"));
    } catch (UnknownHostException e) {
      logger.error("Could not recognise IP");
      logger.error(e);
    }
    serverPort = Integer.valueOf(node.attributeValue("serverport"));
    nodeID = node.attributeValue("nodeID");
    
    NodeInfo nf = new NodeInfo(IP, serverPort, serverPort, nodeSd,out, nodeID);
    
    this.om.addNode(nf);
    
    //check if superclass is usefull for this
    this.nodeID = nodeID;
  }
  
  public String processAddResponse(InetAddress IP, Integer serverPort,
      Socket nodeSd, PrintWriter out) {
    String nodeID;
    
    Element node = super.getMsgBody().element("node");
    try {
      IP = InetAddress.getByName(node.attributeValue("IP"));
    } catch (UnknownHostException e) {
      logger.error("Ccould not recognise IP");
      logger.error(e);
    }
    serverPort = Integer.valueOf(node.attributeValue("serverport"));
    nodeID = node.attributeValue("nodeID");
    
    NodeInfo nf = new NodeInfo(IP, serverPort, serverPort, nodeSd, out, nodeID);
    
    this.om.addNode(nf);
    //check if superclass is usefull for this
    this.nodeID = nodeID;
    
    return helper.linate(this.createAddMeInfo(MsgType.REQUEST).asXML());
  }
  
  public String getSenderNodeID() {
    return this.nodeID;
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