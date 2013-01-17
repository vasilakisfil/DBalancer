package dBalancer;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Helpers {
  private SecureRandom random = new SecureRandom();
  
  public String linate(final String s) {
    return s.replace("\n", "").replace("\r",  "").replace("\r\n",  "");
  }
  
  public String newNodeId() {
    return new BigInteger(130, random).toString(32);
  }

}
