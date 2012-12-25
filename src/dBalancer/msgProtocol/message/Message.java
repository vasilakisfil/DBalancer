package dBalancer.msgProtocol.message;

public interface Message {
  String build();
  void handle();
}
