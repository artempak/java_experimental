package com.navasanta.internals.mservice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.navasanta.internals.protobuf.Common;
import com.navasanta.internals.protobuf.IDSDatashareGrpc;
import com.navasanta.internals.protobuf.Ids;
import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by artemp on 8/31/17.
 * Finnet Limited
 */
@GrpcService(IDSDatashareGrpc.class)
public class IDSDatashareService extends IDSDatashareGrpc.IDSDatashareImplBase {

  @Autowired
  private IDSHashRepository dataRepository;
  @Autowired
  private IDSStringRepository stringRepository;

  @Override
  public void getVehicleByVRM(Common.VRMRequest request, StreamObserver<Ids.IDSVehicleResponseLite> responseObserver) {

    Ids.IDSVehicleDataLite dataLite = Ids.IDSVehicleDataLite.newBuilder()
        .setVrm("MC03KLS")
        .setMake("PEUGEOT")
        .setModel("HATCHBACK 206")
        .setCo2Emission(123)
        .build();
    Ids.IDSVehicleResponseLite responseLite = Ids.IDSVehicleResponseLite.newBuilder()
        .setStatus(Ids.IDSResponseStatus.OK)
        .setData(dataLite)
        .build();

    dataRepository.getData();

    dataRepository.saveVehicle(dataLite);

    Ids.IDSVehicleDataLite dataLite2 = dataRepository.getVehicle("MC03KLS");

    System.out.println(dataLite2.toString());

    try {
      System.out.println(JsonFormat.printer().print(dataLite2));
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }

    stringRepository.saveVehicleJson(dataLite);

    Ids.IDSVehicleDataLite dataLite3 = stringRepository.getVehicle("MC03KLS");

    responseObserver.onNext(responseLite);
    responseObserver.onCompleted();
//    super.getVehicleByVRM(request, responseObserver);
  }
}
