package main.java.org.dbalancer.msgProtocol.message;


public interface Message {
  //handles the response whatever the msg is
  public String handleMsg();
  //returns the request of a new message
  public String request();
  //returns the reply of a request
  public String replyRequest();
  //handles the reply of the request
  public void handleReplyRequest();
  //true if the message is a new message
  public boolean isNull();
  //true if the message is a request
  public boolean isRequest();
  //true if the message is a reply on a request
  public boolean isReply();
  
  public String getSenderNodeID();
}
