package dBalancer.msgProtocol.message;

import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Helpers;

public class InfoMessage implements Message {
  
  private final Helpers helper;
  
  public InfoMessage() {
    helper = new Helpers();
  }
   

  @Override
  public String build() {
    
    Date date = new Date();
    Document response = DocumentHelper.createDocument();
    Element root = response.addElement( "DBalancerMsg" );
    Element type = root.addElement( "type")
                      .addAttribute("timestamp", date.toString())
                      .addAttribute("ID", "Client")
                      .addText( "INFO" );
    
    Element nodes = type.addElement("nodes")
                      .addAttribute("number", "1");
    
    nodes.addElement("node").addText("Server1");
    
    return helper.linate(response.asXML());
  }


  @Override
  public void handle(String msg) {
    
    
  }
  


}
