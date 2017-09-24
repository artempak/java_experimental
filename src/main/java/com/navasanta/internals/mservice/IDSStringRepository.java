package com.navasanta.internals.mservice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.navasanta.internals.protobuf.Ids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * Created by artemp on 9/22/17.
 * Finnet Limited
 */
@Repository
public class IDSStringRepository {

  private RedisTemplate<String, Object> redisTemplate;
  private ValueOperations valueOps;

  @Autowired
  public IDSStringRepository(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  public void init() {
    valueOps = redisTemplate.opsForValue();
  }

  String getData() {
    return "test";
  }

  public void saveVehicle(Ids.IDSVehicleDataLite vehicle) {
    vehicle.getVrm();

    valueOps.set(vehicle.getVrm(), vehicle);
  }

  public void saveVehicleJson(Ids.IDSVehicleDataLite vehicle) {
    vehicle.getVrm();

    String json = null;
    try {
      json = JsonFormat.printer().print(vehicle);
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
    valueOps.set(vehicle.getVrm() + "_JSON", json);
  }

  public Ids.IDSVehicleDataLite getVehicle(String vrm) {
    return (Ids.IDSVehicleDataLite) valueOps.get(vrm);
  }
}
