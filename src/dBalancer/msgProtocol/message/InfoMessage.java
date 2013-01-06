package dBalancer.msgProtocol.message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Helpers;
import dBalancer.Node;
import dBalancer.overlay.NodeInfo;
import dBalancer.overlay.OverlayManager;

public class InfoMessage extends AbstractMessage implements Message {
  private final Helpers helper;
  private final OverlayManager om;
  private ArrayList<NodeInfo> nodeInfo;
  private static final Logger logger = Logger.getLogger(InfoMessage
                                                        .class
                                                        .getName());
  
  public InfoMessage(final Document msgDocument) {
    super(msgDocument);
    this.helper = new Helpers();
    this.om = OverlayManager.getInstance();
    //initializing the arraylist to avoid null check
    this.nodeInfo = new ArrayList<NodeInfo>();
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
      logger.fatal("Fatal Error");
    }
    return tmpReturn;
  }
  
  @Override
  public String request() {

    Document request = DocumentHelper.createDocument();
    Element root = request.addElement( "DBalancerMsg" );
    root = super.buildMsgHeader(root, MsgType.REQUEST, MsgName.INFOMESSAGE);
    @SuppressWarnings("unused")
    Element body = root.addElement("body")
                        .addText("request");
    
    return helper.linate(request.asXML());
  }   
  
  @Override
  public String replyRequest() {
    ArrayList<NodeInfo> nodeInfo = this.om.getAllNodes();
        
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
    for ( Iterator<Element> i = nodes.elementIterator("node"); i.hasNext(); ) {
      Element node = (Element) i.next();
      try {
        IP = InetAddress.getByName(node.attributeValue("IP").toString());
      } catch (UnknownHostException e) {
        logger.error("Could not identify given IP");
        logger.error(e);
      }
      Port = Integer.valueOf(node.attributeValue("Port").toString());
      nodeID = node.attributeValue("ID").toString();
      if (nodeID.equals(this.om.getMyID())) continue;
      this.nodeInfo.add(new NodeInfo(IP, Port, Port, null, null, nodeID));
    }
    System.out.println(this.om.showAllNodes());
  }
  
  public String getSenderNodeID() {
    return super.getSenderNodeID();
  }
  
  public ArrayList<NodeInfo> getNodes() {
    return this.nodeInfo;
  }
  
  public void connectToNodes() {  
    for(NodeInfo nf : this.nodeInfo){
      PrintWriter out = null;
      Socket nodeSd = null;
      
      try {
        nodeSd = new Socket(nf.getIP(), nf.getServerPort());
      } catch (IOException e) {
        logger.error("Could not connect to server");
        logger.error(e);
      }
      
      try {
        out = new PrintWriter(nodeSd.getOutputStream(), true);
      } catch (IOException e1) {
        logger.error("Could not create in out buffers");
        logger.error(e1);
      }
      
      //add the node to the Overlay Manager
      this.om.addNode(new NodeInfo(nf.getIP(),
                                    nf.getServerPort(),
                                    nf.getComPort(),
                                    nodeSd,
                                    out,
                                    nf.getID()));
      
      //send AddMeMessage
      AddMeMessage addMe = new AddMeMessage(null);
      this.om.dispatchMsg(addMe.request(), nf.getID());
      
      /* start a new thread to handle the new node */
      Thread t = new Thread(new Node(nf.getID()));
      t.start();
    }
  }
  
  
}