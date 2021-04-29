package com.llnote.llpvrest.repository;

import com.llnote.llpvrest.model.AppConfig;
import com.llnote.llpvrest.model.DataVO;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KafkaRepository {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  AppConfig appConfig;

  public void save(DataVO dataVO) {
    JSONObject param = new JSONObject();
    param.put("client_ip", dataVO.getClient_ip());
    param.put("_datetime", dataVO.get_datetime());
    param.put("run_file", dataVO.getRun_file());
    param.put("run_title", dataVO.getRun_title());
    param.put("run_sec", dataVO.getRun_sec());
    param.put("stored_time", dataVO.getStored_time());

    StringBuilder servers = new StringBuilder();

    String hosts[] = appConfig.getHosts().split(",");
    int kafkaPort = appConfig.getKafkaPort();
    String topicName = appConfig.getTopicName();

    for (String host : hosts) {
      if (servers.length() > 0) {
        servers.append(",");
      }
      servers.append(host + ":" + kafkaPort);
    }

    final String TOPIC_NAME = topicName;
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,servers.toString());
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
    ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, param.toString());

    //@todo 나중에 kafka 보안 생각하기 우선 로컬만 통신되게 해놨기때문에 상관 없음
    try {
      producer.send(record, (metadata, exception) -> {
        if (exception != null) {
          // some exception
        }
      });

    } catch (Exception e) {
      // exception
      e.printStackTrace();
    } finally {
      producer.flush();
      producer.close();
    }
  }
}
