package testDBalancer;

import java.io.IOException;
import java.net.InetAddress;
import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

import dBalancer.DBalancer;
import dBalancer.DBlncrException;


public class TestDBalancer {
  
  private static void printUsage() {
    System.err.println("Usage: OptionTest [{-s, --server | -c, --client}] " +
        "[{--ip} ip] [{--serverport} port] [{--remoteport} remoteport] " +
        "[{-d --debug}]");
  }
  
  public static void main( String[] args ) throws IOException {
    //Initializations
    DBalancer dBlncr = new DBalancer();
    CmdLineParser parser = new CmdLineParser();    
    
    Option<Boolean> serverOption = parser.addBooleanOption('s', "Server");
    Option<Boolean> clientOption = parser.addBooleanOption('c', "client");
    Option<String> ipOption = parser.addStringOption("ip");
    Option<String> remoteportOption = parser.addStringOption("remoteport");
    Option<String> serverportOption = parser.addStringOption("serverport");
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
    String ip = parser.getOptionValue(ipOption);
    String remoteport = parser.getOptionValue(remoteportOption);
    String serverport = parser.getOptionValue(serverportOption);
    Boolean debug = parser.getOptionValue(debugOption, Boolean.FALSE);
    
    if (server.equals(Boolean.FALSE) && client.equals(Boolean.FALSE)) {
      printUsage();
      System.exit(2);
    }
    if (server.equals(Boolean.TRUE)) {
      if (remoteport != null) {
        System.err.println("Ignoring remoteport in server mode");
      }
      
      dBlncr.start(InetAddress.getByName(ip), Integer.valueOf(serverport),
                    debug);
      
    }
    else {      
      try {
        dBlncr.start(InetAddress.getByName(ip), Integer.valueOf(remoteport),
                      Integer.valueOf(serverport), debug);
      } catch (DBlncrException e) {
        System.out.println("Could not connect to server");
        e.printStackTrace();
      }
    }
  }

}
