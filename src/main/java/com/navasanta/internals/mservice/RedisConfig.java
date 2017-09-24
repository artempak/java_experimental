package com.navasanta.internals.mservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by: artemp
 * Date: 23 Sep, 2017
 */
@ComponentScan("com.navasanta.internals.mservice")
@Configuration
public class RedisConfig {
  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
    jedisConFactory.setHostName("localhost");
    jedisConFactory.setPort(6379);
    jedisConFactory.setUsePool(true);
    return jedisConFactory;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setKeySerializer(new StringRedisSerializer());
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }
}
