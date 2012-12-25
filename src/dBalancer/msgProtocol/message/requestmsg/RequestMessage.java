package dBalancer.msgProtocol.message.requestmsg;

import dBalancer.msgProtocol.message.Message;;

public interface RequestMessage extends Message {
  void handle(final String msg);
}
