package dBalancer.msgProtocol.message.requestmsg;

import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class RequestInfoMessage implements RequestMessage {

  @Override
  public String build() {
    Date date = new Date();
    Document initRequest = DocumentHelper.createDocument();
    Element root = initRequest.addElement( "DBalancerMsg" );
    root.addElement("type")
        .addAttribute("timestamp", date.toString())
        .addAttribute("ID", "Client")
        .addText( "RetrieveInfo" );
    
    return this.linate(initRequest.asXML());
  }
  
  @Override
  public void handle(final String msg) {
    
  }
  
  private String linate(final String s) {
    return s.replace("\n", "").replace("\r",  "").replace("\r\n",  "");
  }




}
