package com.llnote.llpvrest.repository;


import com.llnote.llpvrest.util.CmnUt;
import com.llnote.llpvrest.util.ElasticUt;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticRepository {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  RestClient restClient;
  public ElasticRepository(ElasticUt elasticUt) throws Exception {
    restClient = elasticUt.getConnection();
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