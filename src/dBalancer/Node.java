package dBalancer;

import java.io.*;
import java.net.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import dBalancer.msgProtocol.MessageProtocol;



public class Node implements Runnable {

  private Coordinator coo;
  private Socket nodeFd;
  private MessageProtocol msgPrc;
  
  private BufferedReader in;
  private PrintWriter out;
  
  Node(final Socket nodeFd, final Coordinator coo) {
	this.nodeFd = nodeFd;
	System.out.println(nodeFd.toString());
	System.out.println("LPort: " + nodeFd.getLocalPort() + "LAddress: " + 
	                  nodeFd.getLocalAddress());
    System.out.println("Port: " + nodeFd.getPort() + "Address: " + 
                      nodeFd.getInetAddress());
	this.coo = coo;
	msgPrc = new MessageProtocol(coo);
  }
  
  public void run() {
    in = null;
    out = null;
    try {
      /* obtain an input stream to the node ... */
      in = new BufferedReader(new InputStreamReader(
                                nodeFd.getInputStream() ));
	  /* ... and an output stream to the same node */
      out = new PrintWriter(nodeFd.getOutputStream(), true);
    }
    catch (IOException e) {
	  System.err.println(e);
	  return;
    }

    String msg;
    Document response = null;
    try {
      while ((msg = in.readLine()) != null) {
        
        System.out.println(msg);
        Document msgDocument = DocumentHelper.parseText(msg);
		response = msgPrc.process(msgDocument);
		out.println(this.linate(response.asXML()));
      }
    }
    catch (IOException e) {
      System.out.println("Error reading from client");
      System.err.println(e);
    } catch (DocumentException e) {
      System.out.println("Wrong XML format response");
      System.out.println("Reseting state and ingoring");
      //reset state
      e.printStackTrace();
    }
  }
  
  private String linate(final String s) {
    return s.replace("\n", "").replace("\r",  "").replace("\r\n",  "");
  }
  
  


}
