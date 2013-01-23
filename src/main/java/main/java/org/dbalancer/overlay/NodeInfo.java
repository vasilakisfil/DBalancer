package main.java.org.dbalancer.overlay;

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

  public NodeInfo(final InetAddress IP, final Integer serverPort,
      final Integer comPort, final Socket nodeSd, final PrintWriter out,
      final String nodeID) {
    this.created = System.currentTimeMillis();
    this.IP = IP;
    this.serverPort = serverPort;
    this.comPort = comPort;
    this.nodeSd = nodeSd;
    this.out = out;
    this.nodeID = nodeID;
  }
  
  public NodeInfo(final InetAddress IP, final Integer comPort,
      final Socket nodeSd, final PrintWriter out) {
    this(IP, 0, comPort, nodeSd, out, "null");
  }

  
  public void setOut(final PrintWriter out) {
    this.out = out;
  }
  public void setNodeSd(final Socket sd) {
    this.nodeSd = sd;
  }
  public void setID(final String nodeID) {
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
