package dBalancer.overlay;

import java.util.concurrent.ConcurrentHashMap;

import dBalancer.Coordinator;

public class OverlayManager {
  private static OverlayManager om;
  private Coordinator coo;
  private ConcurrentHashMap<Integer, NodeInfo> hm;
  
  public OverlayManager() {
    OverlayManager.om = this;
    this.coo = Coordinator.getInstance();
    this.hm = new ConcurrentHashMap<Integer, NodeInfo>();
  }

  public OverlayManager getInstance() {
    return OverlayManager.om;
  }

  public void addNode(NodeInfo node) {
    hm.put(node.getHashCode(), node);
  }
  
  public NodeInfo getNode(NodeInfo node) {
    return hm.get(node.getHashCode());
  }
  
  public NodeInfo getNode(Integer hashCode) {
    return hm.get(hashCode);
  }
  
  public void removeNode(NodeInfo node) {
    hm.remove(node.getHashCode());
  }
  
  public void removeNode(Integer hashCode) {
    hm.remove(hashCode);
  }

}