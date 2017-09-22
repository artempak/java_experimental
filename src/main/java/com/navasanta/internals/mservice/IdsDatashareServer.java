package com.navasanta.internals.mservice;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by artemp on 9/19/17.
 * Finnet Limited
 */
@SpringBootApplication
public class IdsDatashareServer {
  private static final Logger logger = Logger.getLogger(IdsDatashareServer.class.getName());

  private final int port;
  private final Server server;

  public IdsDatashareServer() throws IOException {
    this(8980);
  }

  public IdsDatashareServer(int port) throws IOException {
    this.port = port;
    server = ServerBuilder.forPort(port).addService(new IDSDatashareService())
        .build();

  }

  /** Start serving requests. */
  public void start() throws IOException {
    server.start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may has been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        IdsDatashareServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  /** Stop serving requests and shutdown resources. */
  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main method.  This comment makes the linter happy.
   */
  public static void main(String[] args) throws Exception {
    SpringApplication.run(IdsDatashareServer.class, args);

//    IdsDatashareServer server = new IdsDatashareServer(8980);
//    server.start();
//    server.blockUntilShutdown();
  }

}
