package com.navasanta.internals.mservice;

import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;
import com.navasanta.internals.protobuf.IDSDatashareGrpc;
import com.navasanta.internals.protobuf.Ids;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by artemp on 9/19/17.
 * Finnet Limited
 */
public class IdsDatashareClient {
  private static final Logger logger = Logger.getLogger(IdsDatashareClient.class.getName());

  private final ManagedChannel channel;
  private final IDSDatashareGrpc.IDSDatashareBlockingStub blockingStub;
  private final IDSDatashareGrpc.IDSDatashareStub asyncStub;


  private TestHelper testHelper;

  /** Construct client for accessing RouteGuide server at {@code host:port}. */
  public IdsDatashareClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
  }

  /** Construct client for accessing RouteGuide server using the existing channel. */
  public IdsDatashareClient(ManagedChannelBuilder<?> channelBuilder) {
    channel = channelBuilder.build();
    blockingStub = IDSDatashareGrpc.newBlockingStub(channel);
    asyncStub = IDSDatashareGrpc.newStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /**
   * Blocking unary call example.  Calls getFeature and prints the response.
   */
  public void getDataBlocking(String vrm) {
    info("*** getDataBlocking: VRM={0}", vrm);

    Ids.VRMRequest request = Ids.VRMRequest.newBuilder().setVrm(vrm).build();

    Ids.IDSVehicleResponseLite response;
    try {
      response = blockingStub.getVehicleByVRM(request);
      if (testHelper != null) {
        testHelper.onMessage(response);
      }
    } catch (StatusRuntimeException e) {
      warning("RPC failed: {0}", e.getStatus());
      if (testHelper != null) {
        testHelper.onRpcError(e);
      }
      return;
    }
    info("Responded data {0}", response.getData().toString());
  }


  private void info(String msg, Object... params) {
    logger.log(Level.INFO, msg, params);
  }

  private void warning(String msg, Object... params) {
    logger.log(Level.WARNING, msg, params);
  }

  public static void main(String[] args) throws InterruptedException {

    IdsDatashareClient client = new IdsDatashareClient("localhost", 9090);
    try {
      // Looking for a valid feature
      client.getDataBlocking("MC03KLS");

    } finally {
      client.shutdown();
    }
  }

  /**
   * Only used for helping unit test.
   */
  @VisibleForTesting
  interface TestHelper {
    /**
     * Used for verify/inspect message received from server.
     */
    void onMessage(Message message);

    /**
     * Used for verify/inspect error received from server.
     */
    void onRpcError(Throwable exception);
  }

  @VisibleForTesting
  void setTestHelper(TestHelper testHelper) {
    this.testHelper = testHelper;
  }
}
