package com.llnote.llpvrest.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

  public String getElasticProto() {
    return elasticProto;
  }

  public void setElasticProto(String elasticProto) {
    this.elasticProto = elasticProto;
  }

  public String getElasticId() {
    return elasticId;
  }

  public void setElasticId(String elasticId) {
    this.elasticId = elasticId;
  }

  public String getElasticPw() {
    return elasticPw;
  }

  public void setElasticPw(String elasticPw) {
    this.elasticPw = elasticPw;
  }

  public int getElasticPort() {
    return elasticPort;
  }

  public void setElasticPort(int elasticPort) {
    this.elasticPort = elasticPort;
  }

  public String getHosts() {
    return hosts;
  }

  public void setHosts(String hosts) {
    this.hosts = hosts;
  }

  public int getKafkaPort() {
    return kafkaPort;
  }

  public void setKafkaPort(int kafkaPort) {
    this.kafkaPort = kafkaPort;
  }

  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public String getTrustStorePath() {
    return trustStorePath;
  }

  public void setTrustStorePath(String trustStorePath) {
    this.trustStorePath = trustStorePath;
  }

  public String getKeyStorePath() {
    return keyStorePath;
  }

  public void setKeyStorePath(String keyStorePath) {
    this.keyStorePath = keyStorePath;
  }

  public String getTrustStore() {
    return trustStore;
  }

  public void setTrustStore(String trustStore) {
    this.trustStore = trustStore;
  }

  public String getKeyStore() {
    return keyStore;
  }

  public void setKeyStore(String keyStore) {
    this.keyStore = keyStore;
  }
}