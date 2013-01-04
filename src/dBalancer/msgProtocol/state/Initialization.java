package dBalancer.msgProtocol.state;

import org.dom4j.Document;

import dBalancer.Helpers;
import dBalancer.Node;
import dBalancer.msgProtocol.message.AddMeMessage;
import dBalancer.msgProtocol.message.InfoMessage;
import dBalancer.msgProtocol.message.Message;
import dBalancer.overlay.NodeInfo;
import dBalancer.overlay.OverlayManager;

public class Initialization implements State {
  @SuppressWarnings("unused")
  private final Helpers helper;
  private final OverlayManager om;
  private StateEnum internalState;
  
  private enum StateEnum {
    ADDME, GETNODES
  }
  
  public Initialization() {
    helper = new Helpers();
    this.om = OverlayManager.getInstance();
    this.internalState = StateEnum.ADDME;
  }

  public String process(final StateWrapper wrapper,
                        final Document msgDocument,
                        final Message msgType) {
    String response = null;
    
    if (msgType.getClass().equals(AddMeMessage.class)) {
      if (this.internalState.equals(StateEnum.ADDME)) {
        System.out.println("Handling response to AddMe message");
        msgType.handleMsg();
        
        new InfoMessage(null, msgType.getCurrentNodeID());
        
        //this.om.getNode(msgType.getCurrentNodeID())
          //      .getOut().println(im.handleMsg());
        this.internalState = StateEnum.GETNODES;
      }
    //for other general purpose messages just reply the request
    } else if (msgType.getClass().equals(InfoMessage.class)) {
      if (this.internalState.equals(StateEnum.GETNODES)) {
        System.out.println("Handling info message");
        msgType.handleMsg();
        
        InfoMessage info = (InfoMessage) msgType;
        NodeInfo[] nf = info.returnNodes();
        if (nf[0] != null) {
          for(NodeInfo nodeInfo : nf) {
            if (!this.om.containsNode(nodeInfo.getID())) {
              this.om.addNode(nodeInfo);
            }
            System.out.println(nodeInfo.getServerPort());
          }
        }
        
        nf = this.om.getAllNodes();
        for(NodeInfo nodeInfo : nf) {
          if ( nodeInfo.getID().equals(wrapper.getCurrentNode().getID())) {
            System.out.println(nodeInfo.getServerPort());
            /* start a new thread to handle the new node */
            Thread t = new Thread(new Node(nodeInfo.getIP(),
                                            nodeInfo.getServerPort(),
                                            nodeInfo.getID()));
            t.start();
          }
        }
        
        wrapper.setState(new Idle());
      }
    }
    
    else {
      response = msgType.handleMsg();
    }
    
    //NodeInfo[] nodeInfo = new NodeInfo[this.om.getSize()];
    
    //for (NodeInfo nf : nodeInfo) {
      /* start a new thread to handle the new node */
     /* Thread t = new Thread(new Node(nf.getIP(), nf.getPort() ));
      t.start();
    }*/
    
    
    //wrapper.setState(new Idle());
    return response;
  }
}
