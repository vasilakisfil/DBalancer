package dBalancer;

import java.io.IOException;
import java.net.InetAddress;
import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;


public class TestDBalancer {
  
  private static void printUsage() {
    System.err.println("Usage: OptionTest [{-m, --mode} Client|Server] " +
        "[{-c, --connect} ip]");
  }
  
  public static void main( String[] args ) throws IOException {
    //Initializations
    DBalancer dBlncr = new DBalancer();
    InetAddress IP = null;
    CmdLineParser parser = new CmdLineParser();
    
    
    Option<String> modeOption = parser.addStringOption('m', "mode");
    Option<String> connectOption = parser.addStringOption('c', "connect");

    try {
        parser.parse(args);
    }
    catch ( CmdLineParser.OptionException e ) {
        System.err.println(e.getMessage());
        printUsage();
        System.exit(2);
    }
    
    String mode = parser.getOptionValue(modeOption);
    String connectValue = parser.getOptionValue(connectOption);
    
    IP = InetAddress.getByName(connectValue);
    
    if (mode.equals("Server")) {
      dBlncr.start(4561);
    }
    else if (mode.equals("Client")) {
      dBlncr.connectToServer(IP, 4561);
    }


    System.exit(0);
  }

}
