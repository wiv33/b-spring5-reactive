package org.psawesome.testFlux.using;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public class PsConnection implements Closeable {

  public static final Logger log = LoggerFactory.getLogger(PsConnection.class);

  @Override
  public void close() throws IOException {
    log.info("IO Connection Closed");
  }


}
