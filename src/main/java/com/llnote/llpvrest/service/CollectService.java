package com.llnote.llpvrest.service;

import com.llnote.llpvrest.model.DataVO;
import com.llnote.llpvrest.repository.MariaDBRepository;
import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Service
public class CollectService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @NonNull
  private final MariaDBRepository mariaDBRepository;

  public ResponseEntity<String> save(String data) {
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

        if ("".equals(ip))
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
          DataVO dataVO = new DataVO();
          dataVO.setRun_file(run_file);
          dataVO.setRun_title(run_title);
          dataVO.setRun_sec(run_sec);
          dataVO.set_datetime(_datetime);
          dataVO.setStored_time(stored_time);
          dataVO.setClient_ip(ip);
          mariaDBRepository.save(dataVO);
        }
      }
    } catch (Exception e) {
      if("Value run_title of type java.lang.String cannot be converted to JSONArray".equals(e.getMessage())){
        responseCode = 400;
      }else{
        responseCode = 500;
      }
      msg = e.getMessage();
    }
    logger.info("rest 요청응답 (" + responseCode + ")");
    return ResponseEntity.status(responseCode).body(msg);
  }
}
