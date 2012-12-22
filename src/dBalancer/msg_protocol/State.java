package dBalancer.msg_protocol;

public interface State {
  void process(final MessageProtocol msgPrc, final String msg);
}

class Initialization implements State {

  @Override
  public void process(final MessageProtocol msgPrc, final String msg) {
    msgPrc.process(msg);
  }
  
}

