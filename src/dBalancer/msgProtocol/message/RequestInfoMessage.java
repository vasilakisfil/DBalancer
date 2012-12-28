package dBalancer.msgProtocol.message;

import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dBalancer.Helpers;

public class RequestInfoMessage implements Message {
  private final Helpers helper;
  
  public RequestInfoMessage() {
    helper = new Helpers();
  }
   

  @Override
  public String build() {
    Date date = new Date();
    Document initRequest = DocumentHelper.createDocument();
    Element root = initRequest.addElement( "DBalancerMsg" );
    root.addElement("type")
        .addAttribute("timestamp", date.toString())
        .addAttribute("ID", "Client")
        .addText( "REQUESTINFO" );
    
    return helper.linate(initRequest.asXML());
  }
  
  @Override
  public void handle(final String msg) {
    System.out.println(msg);    
  }




}
