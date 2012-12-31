package dBalancer.overlay;

import java.net.InetAddress;

public class NodeInfo {
  private InetAddress IP;
  private Integer Port;

  public NodeInfo(InetAddress IP, Integer Port) {
    this.IP = IP;
    this.Port = Port;    
  }

  public void setIP(InetAddress IP) {
    this.IP = IP;
  }

  public void setPort(int Port) {
    this.Port = Port;
  }

  public Integer getHashCode() {
    String s = this.IP.toString().concat(":").concat(Port.toString());
    return s.hashCode();
  }
  
  public InetAddress getIP() {
    return this.IP;
  }
  
  public Integer getPort() {
    return this.Port;
  }

}
