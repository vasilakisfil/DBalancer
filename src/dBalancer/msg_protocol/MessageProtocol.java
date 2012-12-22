package dBalancer.msg_protocol;

public class MessageProtocol {
  private State currentState;

  public MessageProtocol() {
    this.setState(new Initialization());
    
  }
  
  public String process(final String msg) {
    this.getState().process(this, msg);
    return msg;
  }
  
  protected State getState() {
    return this.currentState;
  }
  
  protected void setState(final State state) {
    this.currentState = state;
  }
  
  
}
