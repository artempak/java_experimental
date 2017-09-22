package com.navasanta.internals.mservice;

import com.finnetlimited.automyze.protobuf.Ids;
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

  public Ids.IDSVehicleDataLite getVehicle(String vrm) {
    return (Ids.IDSVehicleDataLite) valueOps.get(vrm);
  }
}
