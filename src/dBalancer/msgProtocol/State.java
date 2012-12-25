package dBalancer.msgProtocol;

import java.util.Date;

import org.dom4j.*;

import dBalancer.Coordinator;

public interface State {
  Document process(final MessageProtocol context, final Document msgPrc);
}

class Idle implements State {

  public Document process(final MessageProtocol context, final Document msgPrc) {

    Date date = new Date();
    Document response = DocumentHelper.createDocument();
    Element root = response.addElement( "DBalancerMsg" );
    Element type = root.addElement( "type")
                      .addAttribute("timestamp", date.toString())
                      .addAttribute("ID", "Client")
                      .addText( "ProvideInfo" );
    
    Element nodes = type.addElement("nodes")
                      .addAttribute("number", "1");
    
    Element node = nodes.addElement("node")
                      .addText("Server1");
    
    return response;
  }
  
}

