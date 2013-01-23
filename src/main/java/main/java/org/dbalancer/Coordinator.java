package main.java.org.dbalancer;

import java.util.Arrays;

import org.apache.log4j.Logger;

import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import main.java.org.dbalancer.overlay.OverlayManager;


public class Coordinator {
  private static Coordinator instance;
  Cluster myCluster;
  ColumnFamilyDefinition cfDef;
  KeyspaceDefinition newKeyspace;
  @SuppressWarnings("unused")
  private static OverlayManager om;
  private static final Logger logger = Logger.getLogger(Coordinator
                                                        .class
                                                        .getName());
  Coordinator() {
    myCluster = HFactory.getOrCreateCluster("test-cluster", "localhost:9160");
    cfDef = HFactory.createColumnFamilyDefinition("MyKeyspace",
                      "ColumnFamilyName", ComparatorType.BYTESTYPE);
    newKeyspace = HFactory.createKeyspaceDefinition("MyKeyspace",
                            ThriftKsDef.DEF_STRATEGY_CLASS, 1, 
                            Arrays.asList(cfDef));
  }

  static public Coordinator getInstance() {
    if (instance == null) {
      instance = new Coordinator();
      om = OverlayManager.getInstance();
    }
    return instance;
  }
  
  public void start() {
    
  }
  
  public String getLoad() {
    String load = "Empty Load";
    return load;
  }

}
