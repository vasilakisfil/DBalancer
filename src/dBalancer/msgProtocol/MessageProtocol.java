package dBalancer.msgProtocol;

import org.dom4j.*;

import dBalancer.Coordinator;

public class MessageProtocol {
  private State currentState;
  public static Coordinator coo;

  public MessageProtocol(final Coordinator coo) {
    this.setState(new Idle());
    MessageProtocol.coo = coo;
  }
  
  public MessageProtocol(final State state, final Coordinator coo) {
    this.setState(state);
    MessageProtocol.coo = coo;
  }
  
  public Document process(final Document msgPrc) {
    Document response = this.getState().process(this, msgPrc);
    return response;
  }
  
  protected State getState() {
    return this.currentState;
  }
  
  protected void setState(final State state) {
    this.currentState = state;
  }
  
  
}
