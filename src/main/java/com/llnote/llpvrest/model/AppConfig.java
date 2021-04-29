package com.llnote.llpvrest.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AppConfig {

  @Value("${elastic.proto}")
  private String elasticProto;
  @Value("${elastic.id}")
  private String elasticId;
  @Value("${elastic.pw}")
  private String elasticPw;
  @Value("${elastic.port}")
  private int elasticPort;
  @Value("${hosts}")
  private String hosts;
  @Value("${kafka.port}")
  private int kafkaPort;

  @Value("${topic.name}")
  private String topicName;
  @Value("${trust.store.path}")
  private String trustStorePath;
  @Value("${key.store.path}")
  private String keyStorePath;
  @Value("${trust.store}")
  private String trustStore;
  @Value("${key.store}")
  private String keyStore;

}