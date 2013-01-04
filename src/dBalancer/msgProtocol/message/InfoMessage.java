package dBalancer.msgProtocol.message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Helpers;
import dBalancer.overlay.NodeInfo;
import dBalancer.overlay.OverlayManager;

public class InfoMessage extends AbstractMessage implements Message {
  private final Helpers helper;
  private final OverlayManager om;
  private NodeInfo[] nodeInfo;
  
  public InfoMessage(Document msgDocument, String nodeID) {
    super(msgDocument, nodeID);
    this.helper = new Helpers();
    this.om = OverlayManager.getInstance();
    
    this.om.getNode(nodeID).getOut().println(this.handleMsg());
  }
  
  public InfoMessage(Document msgDocument, NodeInfo currentNode) {
    super(msgDocument, currentNode);
    this.helper = new Helpers();
    this.om = OverlayManager.getInstance();
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
    root = super.buildMsgHeader(root, MsgType.REQUEST, MsgName.INFOMESSAGE);
    Element body = root.addElement("body")
                        .addText("request");
    
    return helper.linate(request.asXML());
  }   
  
  @Override
  public String replyRequest() {
    NodeInfo[] nodeInfo = new NodeInfo[this.om.getSize()]; 
        nodeInfo = this.om.getAllNodes();
        
    Document response = DocumentHelper.createDocument();
    Element root = response.addElement( "DBalancerMsg" );
    //enums are static in Java
    root = super.buildMsgHeader(root, MsgType.REPLYREQUEST,
                                MsgName.INFOMESSAGE);
    Element body = root.addElement("body");
    Element nodes = body.addElement("nodes")
                        .addAttribute("size", this.om.getSize().toString());
    Integer counter = 0; //for-each loops do not have built in counter
    for (NodeInfo nf : nodeInfo) {
      //if my address appears in the received list skip it
      if (nf.getID() == this.om.getMyID()) {
        continue;
      }
      Element node = nodes.addElement("node");
      node.addAttribute("IP", nf.getStringIP());
      node.addAttribute("Port", nf.getServerPort().toString());
      node.addAttribute("ID", nf.getID());
      node.addText(counter.toString());
      counter++;
    }
    
    return helper.linate(response.asXML());
  }
  
  @Override
  public void handleReplyRequest() {
    InetAddress IP = null;
    Integer Port = null;
    String nodeID = null;
    
    Element nodes = super.getMsgBody().element("nodes");
    Integer size = Integer.valueOf(nodes.attributeValue("size").toString());
    this.nodeInfo = new NodeInfo[size];
    Integer counter = 0;
    for ( Iterator<Element> i = nodes.elementIterator("node"); i.hasNext(); ) {
      Element node = (Element) i.next();
      try {
        IP = InetAddress.getByName(node.attributeValue("IP").toString());
      } catch (UnknownHostException e) {
        System.out.println("Fatal error: Could not identify given IP");
        e.printStackTrace();
      }
      System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
      Port = Integer.valueOf(node.attributeValue("Port").toString());
      nodeID = node.attributeValue("ID").toString();
      System.out.println(nodeID);
      if (nodeID.equals(this.om.getMyID())) continue;
      this.nodeInfo[counter] = new NodeInfo(IP, Port, Port, null, null, nodeID);
      counter++;
    }
    System.out.println(this.om.showAllNodes());
  }
  
  public String getCurrentNodeID() {
    return super.getCurrentNodeID();
  }
  
  public NodeInfo[] returnNodes() {
    return this.nodeInfo;
  }
  
  
}