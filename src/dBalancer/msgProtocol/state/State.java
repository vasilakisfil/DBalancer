package dBalancer.msgProtocol.state;

import org.dom4j.Document;

import dBalancer.msgProtocol.message.Message;

public interface State {
  
  String process(final StateWrapper context,
                  final Document msgPrc,
                  final Message msgType);
}


