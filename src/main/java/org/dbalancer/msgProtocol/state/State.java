package main.java.org.dbalancer.msgProtocol.state;

import org.dom4j.Document;

import main.java.org.dbalancer.msgProtocol.message.Message;

public interface State {
  
  String process(final StateWrapper context,
                  final Document msgDocument,
                  final Message msgType);
}


