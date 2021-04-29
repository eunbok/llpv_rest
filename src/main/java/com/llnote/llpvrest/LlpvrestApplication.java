package com.llnote.llpvrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = { "classpath:db.properties" })
public class LlpvrestApplication {

  public static void main(String[] args) {
    SpringApplication.run(LlpvrestApplication.class, args);
  }

}
