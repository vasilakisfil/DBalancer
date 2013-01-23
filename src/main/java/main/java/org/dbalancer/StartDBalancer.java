package main.java.org.dbalancer;

import java.io.IOException;
import java.net.InetAddress;
import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

import main.java.org.dbalancer.DBalancer;
import main.java.org.dbalancer.DBlncrException;


public class StartDBalancer {
  
  private static void printUsage() {
    System.err.println("Usage: OptionTest [{-s, --server | -c, --client}] " +
        "[{--serverip} ip] [{--serverport} port]" +
        "[{--seedip}{--seedport} seedport] " +
        "[{-d --debug}]");
  }
  
  public static void main( String[] args ) throws IOException {
    //Initializations
    DBalancer dBlncr = new DBalancer();
    CmdLineParser parser = new CmdLineParser();    
    
    Option<Boolean> serverOption = parser.addBooleanOption('s', "Server");
    Option<Boolean> clientOption = parser.addBooleanOption('c', "client");
    Option<String> serverIpOption = parser.addStringOption("serverip");
    Option<String> seedIpOption = parser.addStringOption("seedip");
    Option<String> seedPortOption = parser.addStringOption("seedport");
    Option<String> serverPortOption = parser.addStringOption("serverport");
    Option<Boolean> debugOption = parser.addBooleanOption('d', "debug");

    try {
        parser.parse(args);
    }
    catch ( CmdLineParser.OptionException e ) {
        System.err.println(e.getMessage());
        printUsage();
        System.exit(2);
    }

    Boolean server = parser.getOptionValue(serverOption, Boolean.FALSE);
    Boolean client = parser.getOptionValue(clientOption, Boolean.FALSE);
    String serverIp = parser.getOptionValue(serverIpOption);
    String seedIp = parser.getOptionValue(seedIpOption);
    String seedPort = parser.getOptionValue(seedPortOption);
    String serverPort = parser.getOptionValue(serverPortOption);
    Boolean debug = parser.getOptionValue(debugOption, Boolean.FALSE);
    
    if (server.equals(Boolean.FALSE) && client.equals(Boolean.FALSE)) {
      printUsage();
      System.exit(2);
    }
    if (server.equals(Boolean.TRUE)) {
      if (seedPort != null) {
        System.err.println("Ignoring seedport in server mode");
      }
      
      dBlncr.start(InetAddress.getByName(serverIp), Integer.valueOf(serverPort),
                    debug);      
    }
    else {      
      try {
        dBlncr.start(InetAddress.getByName(serverIp),
                      Integer.valueOf(serverPort),
                      InetAddress.getByName(seedIp),
                      Integer.valueOf(seedPort), debug);
      } catch (DBlncrException e) {
        System.out.println("Could not connect to server");
        e.printStackTrace();
      }
    }
  }

}
