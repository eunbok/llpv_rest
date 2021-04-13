package com.llnote.llpvrest.repository;


import com.llnote.llpvrest.model.AppConfig;
import com.llnote.llpvrest.util.CmnUt;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ElasticRepository {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  RestClient restClient;
  @Autowired
  AppConfig appConfig;

  @PostConstruct
  public void postConstruct() throws Exception {
    getInstance();
  }

  private void getInstance() throws Exception {
    Path trustStorePath = Paths.get(appConfig.getTrustStorePath());
    Path keyStorePath = Paths.get(appConfig.getKeyStorePath());

    KeyStore trustStore = KeyStore.getInstance(appConfig.getTrustStore());
    KeyStore keyStore = KeyStore.getInstance(appConfig.getKeyStore());
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
    credentialsProvider.setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(appConfig.getElasticId(), appConfig.getElasticPw()));

    String hosts[] = appConfig.getHosts().split(",");
    int elasticPort = appConfig.getElasticPort();
    String elasticProto = appConfig.getElasticProto();
    HttpHost[] httphosts = new HttpHost[hosts.length];
    for (int i = 0; i < hosts.length; i++) {
      httphosts[i] = new HttpHost(hosts[i], elasticPort, elasticProto);
    }
    restClient = RestClient
        .builder(httphosts)
        .setHttpClientConfigCallback(new HttpClientConfigCallback() {
          @Override
          public HttpAsyncClientBuilder customizeHttpClient(
              HttpAsyncClientBuilder httpClientBuilder) {
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                .setSSLContext(sslContext).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
          }
        }).build();

  }

  public JSONArray getRank() throws Exception {
    StringBuilder str = new StringBuilder();
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd (E)");

    Calendar startDt = Calendar.getInstance();

    JSONArray resultArr = new JSONArray();
    for (int i = 0; i < 4; i++) {
      str.append("{\"index\" : \"llpv-log\"}\n");
      str.append("{\"size\":\"0\",\"query\":{\"range\":{\"stored_time\":{\"from\":\"" + sdf1
          .format(startDt.getTime()) + "\",\"to\":\"" + sdf2.format(startDt.getTime())
          + "\"}}},\"aggs\":{\"rank\":{\"terms\":{\"field\":\"run_file\", \"size\": 2147483647},\"aggs\":{\"rank_sum\":{\"sum\":{\"field\":\"run_sec\"}},\"result_sort\":{\"bucket_sort\":{\"sort\":[{\"rank_sum\":{\"order\":\"desc\"}}],\"size\":10}}}}}}\n");

      JSONObject resultJo = new JSONObject();
      resultJo.put(sdf3.format(startDt.getTime()), new JSONObject());
      resultArr.put(resultJo);

      startDt.add(Calendar.DATE, -1);
    }

    Request request = new Request("POST", "/_msearch");
    request.setEntity(new NStringEntity(str.toString(), ContentType.APPLICATION_JSON));
    Response response = restClient.performRequest(request);
    JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));

    JSONArray jarr = responseBody.getJSONArray("responses");

    for (int i = 0; i < jarr.length(); i++) {
      JSONObject jo = jarr.getJSONObject(i);
      JSONArray bucket = jo.getJSONObject("aggregations").getJSONObject("rank")
          .getJSONArray("buckets");
      JSONArray tempArr = new JSONArray();
      for (int j = 0; j < bucket.length(); j++) {
        JSONObject temp = new JSONObject();
        temp.put("run_file", bucket.getJSONObject(j).getString("key"));
        temp.put("run_sec",
            CmnUt.secToTime(bucket.getJSONObject(j).getJSONObject("rank_sum").getInt("value")));
        tempArr.put(temp);
      }
      Iterator<String> iter = resultArr.getJSONObject(i).keys();
      String key = "";
      while (iter.hasNext()) {
        key = iter.next();
      }
      resultArr.getJSONObject(i).put(key, tempArr);
    }

    return resultArr;
  }
}