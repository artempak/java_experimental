package com.navasanta.internals.mservice;

import com.navasanta.internals.protobuf.Ids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * Created by artemp on 9/21/17.
 * Finnet Limited
 */
@Repository
public class IDSHashRepository {
  private static final String KEY = "IDS";

  private RedisTemplate<String, Object> redisTemplate;
  private HashOperations hashOps;

  @Autowired
  public IDSHashRepository(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  public void init() {
    hashOps = redisTemplate.opsForHash();
  }

  String getData() {
    return "test";
  }

  public void saveVehicle(Ids.IDSVehicleDataLite vehicle) {
    vehicle.getVrm();

    hashOps.put(KEY, vehicle.getVrm(), vehicle);
  }

  public Ids.IDSVehicleDataLite getVehicle(String vrm) {
    return (Ids.IDSVehicleDataLite) hashOps.get(KEY, vrm);
  }

}
