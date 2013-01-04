package dBalancer.overlay;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

import dBalancer.Coordinator;
import dBalancer.Helpers;

public class OverlayManager {
  private static OverlayManager om;
  private final InetAddress myIP;
  private final Integer myPort;
  private final String myID;
  private final Helpers helper;
  @SuppressWarnings("unused")
  private Coordinator coo;
  private ConcurrentHashMap<String, NodeInfo> hm;
  
  public OverlayManager(InetAddress IP, Integer Port) {
    helper = new Helpers();
    this.myIP = IP;
    this.myPort = Port;
    this.myID = helper.newNodeId();
    OverlayManager.om = this;
    this.coo = Coordinator.getInstance();
    this.hm = new ConcurrentHashMap<String, NodeInfo>();
  }

  public static OverlayManager getInstance() {
    return OverlayManager.om;
  }

  public void addNode(NodeInfo node) {
    hm.put(node.getID(), node);
  }
  
  public NodeInfo getNode(NodeInfo node) {
    return hm.get(node.getID());
  }
  
  public NodeInfo getNode(String ID) {
    return hm.get(ID);
  }
  
  public void removeNode(NodeInfo node) {
    hm.remove(node.getID());
  }
  
  public void removeNode(String ID) {
    hm.remove(ID);
  }
  
  public Boolean containsNode(String ID) {
    if (this.hm.containsKey(ID)) {
      return true;
    } else return false;
  }
  
  public Boolean containsNode(NodeInfo nf) {
    if (this.hm.containsKey(nf.getID())) {
      return true;
    } else return false;
  }
  
  public Integer getSize() {
    return hm.size();
  }
  
  public NodeInfo[] getAllNodes() {
    //NodeInfo[] ArrayNodeInfo = new NodeInfo[this.getSize()];
    return this.hm.values().toArray(new NodeInfo[this.getSize()]);
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
    NodeInfo[] nodeInfo = new NodeInfo[this.getSize()]; 
    nodeInfo = this.getAllNodes();
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

}