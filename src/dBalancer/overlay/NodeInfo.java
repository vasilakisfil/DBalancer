package dBalancer.overlay;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class NodeInfo {
  private final InetAddress IP;
  private final Integer serverPort, comPort;
  @SuppressWarnings("unused")
  private final Long created;
  private String nodeID;
  private Socket nodeSd;
  private PrintWriter out;

  public NodeInfo(InetAddress IP, Integer serverPort, Integer comPort,
      Socket nodeSd, PrintWriter out, String nodeID) {
    this.created = System.currentTimeMillis();
    this.IP = IP;
    this.serverPort = serverPort;
    this.comPort = comPort;
    this.nodeSd = nodeSd;
    this.out = out;
    this.nodeID = nodeID;
  }
  
  public NodeInfo(InetAddress IP, Integer comPort, Socket nodeSd,
      PrintWriter out) {
    this(IP, 0, comPort, nodeSd, out, "null");
  }

  
  public void setOut(PrintWriter out) {
    this.out = out;
  }
  public void setNodeSd(Socket sd) {
    this.nodeSd = sd;
  }
  public void setID(String nodeID) {
    this.nodeID = nodeID;
  }

  public String getID() {
    return this.nodeID;
  }
  public InetAddress getIP() {
    return this.IP;
  }
  public String getStringIP() {
    return this.IP.getHostAddress();
  }
  public Integer getComPort() {
    return this.comPort;
  }
  public Integer getServerPort() {
    return this.serverPort;
  }
  public Socket getSd() {
    return this.nodeSd;
  }
  public PrintWriter getOut() {
    return this.out;
  }

}
