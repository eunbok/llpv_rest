package com.llnote.llpvrest.controller;

import com.llnote.llpvrest.repository.KafkaRepository;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class CollectionController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final KafkaRepository kafkaRepository;

  public CollectionController(KafkaRepository kafkaRepository) {
    this.kafkaRepository = kafkaRepository;
  }

  @RequestMapping(value = "/rest", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> rest(@RequestBody String data) {
    int responseCode = 200;
    String msg = "ok";
    try {
      HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
          .currentRequestAttributes()).getRequest();
      String ip = req.getHeader("X-FORWARDED-FOR");
      if (ip == null) {
        ip = req.getRemoteAddr();
      }

      JSONArray arr = new JSONArray(data);
      for (int i = 0; i < arr.length(); i++) {
        boolean isCheck = true;
        JSONObject jo = arr.getJSONObject(i);
        String _datetime = jo.getString("_datetime");
        String run_file = jo.getString("run_file");
        String run_title = jo.getString("run_title");
        int run_sec = jo.getInt("run_sec");
        String stored_time = jo.getString("stored_time");

        if ("".equals(ip)) //ip 검증해야함
        {
          isCheck = false;
        }
        if ("".equals(_datetime)) {
          isCheck = false;
        }
        if ("".equals(run_file)) {
          isCheck = false;
        }
        if ("".equals(run_title)) {
          isCheck = false;
        }
        if ("".equals(stored_time)) {
          isCheck = false;
        }
        if (run_sec < 1) {
          isCheck = false;
        }

        if (isCheck) {
          JSONObject param = new JSONObject();
          param.put("client_ip", ip);
          param.put("_datetime", _datetime);
          param.put("run_file", run_file);
          param.put("run_title", run_title);
          param.put("run_sec", run_sec);
          param.put("stored_time", stored_time);

          kafkaRepository.saveKafka(param);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();

      responseCode = 500;
      msg = e.getMessage();
    }
    logger.info("rest 요청응답 (" + responseCode + ")");
    return ResponseEntity.status(responseCode).body(msg);
  }


}
