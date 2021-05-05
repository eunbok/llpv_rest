package com.llnote.llpvrest.util;

import com.llnote.llpvrest.model.AppConfig;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElasticUt {

  private final AppConfig appConfig;
  private RestClient restClient = null;

  private ElasticUt(AppConfig appConfig) throws Exception {
    this.appConfig = appConfig;
    Path trustStorePath = Paths.get(this.appConfig.getTrustStorePath());
    Path keyStorePath = Paths.get(this.appConfig.getKeyStorePath());

    KeyStore trustStore = KeyStore.getInstance(this.appConfig.getTrustStore());
    KeyStore keyStore = KeyStore.getInstance(this.appConfig.getKeyStore());
    try (InputStream is = Files.newInputStream(trustStorePath)) {
      trustStore.load(is, "".toCharArray());
    }
    try (InputStream is = Files.newInputStream(keyStorePath)) {
      keyStore.load(is, "".toCharArray());
    }
    SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(trustStore, null)
        .loadKeyMaterial(keyStore, "".toCharArray());

    final SSLContext sslContext = sslBuilder.build();

    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
        this.appConfig.getElasticId(), this.appConfig.getElasticPw()));
    String hosts[] = this.appConfig.getHosts().split(",");
    int elasticPort = this.appConfig.getElasticPort();
    String elasticProto = this.appConfig.getElasticProto();
    HttpHost[] httphosts = new HttpHost[hosts.length];
    for (int i = 0; i < hosts.length; i++) {
      httphosts[i] = new HttpHost(hosts[i], elasticPort, elasticProto);
    }
    this.restClient =
        RestClient.builder(httphosts).setHttpClientConfigCallback(new HttpClientConfigCallback() {
          @Override
          public HttpAsyncClientBuilder customizeHttpClient(
              HttpAsyncClientBuilder httpClientBuilder) {
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                .setSSLContext(sslContext).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
          }
        }).build();
  }

  public RestClient getConnection() {
    return restClient;
  }
}
