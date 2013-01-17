package dBalancer.overlay;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import dBalancer.Helpers;

public class OverlayManager {
  private static OverlayManager instance;
  private InetAddress myIP;
  private Integer myPort;
  private String myID;
  private Helpers helper;
  private final ConcurrentHashMap<String, NodeInfo> hm;
  
  private OverlayManager() {
    helper = new Helpers();
    this.hm = new ConcurrentHashMap<String, NodeInfo>();
  }

  public static OverlayManager getInstance() {
    if (instance == null) {
      instance = new OverlayManager();
    }
    return instance;
  }
  
  public void setMyInfo(final InetAddress IP, final Integer Port) {
    this.myIP = IP;
    this.myPort = Port;
    this.myID = helper.newNodeId();
  }
  public String getMyInfo() {
    String s = " myIP: " + this.myIP.getHostAddress() + " " 
                        + "myPort: " + this.myPort + " "
                        + "myID: " + this.myID;
    return s;
  }

  public void addNode(final NodeInfo node) {
    hm.put(node.getID(), node);
  }
  
  public NodeInfo getNode(final NodeInfo node) {
    return hm.get(node.getID());
  }
  
  public NodeInfo getNode(final String ID) {
    return hm.get(ID);
  }
  
  public void removeNode(final NodeInfo node) {
    hm.remove(node.getID());
  }
  
  public void removeNode(final String ID) {
    hm.remove(ID);
  }
  
  public Boolean containsNode(final String ID) {
    if (this.hm.containsKey(ID)) {
      return true;
    } else return false;
  }
  
  public Boolean containsNode(final NodeInfo nf) {
    if (this.hm.containsKey(nf.getID())) {
      return true;
    } else return false;
  }
  
  public Integer getSize() {
    return hm.size();
  }
  
  public ArrayList<NodeInfo> getAllNodes() {
    return new ArrayList<NodeInfo>(this.hm.values());
  }
  
  public InetAddress getMyIP() {
    return this.myIP;
  }
  
  public String getMyStringIP() {
    return this.myIP.getHostAddress();
  }
  
  public Integer getMyPort() {
    return this.myPort;
  }
  
  public String getMyID() {
    return this.myID;
  }
  
  public String showAllNodes() {
    ArrayList<NodeInfo> nodeInfo = this.getAllNodes(); 
    String show = "";
    
    Integer counter = 0; //for-each loops do not have built in counter
    for (NodeInfo nf : nodeInfo) {
      show += counter.toString() + ". ";
      show += "IP: " + nf.getStringIP() + " ";
      show += "Port: " + nf.getServerPort().toString() + " ";
      show += "Port: " + nf.getComPort().toString() + " ";
      show += "ID: " + nf.getID();
      show += "\n";
      counter++;
    }
    
    return show;
  }
  
  public void dispatchMsg(final String msg, final String nodeID) {
    if (nodeID == null) {
      //dispatch it to all
    } else {
      this.getNode(nodeID).getOut().println(msg);
    }
  }

}