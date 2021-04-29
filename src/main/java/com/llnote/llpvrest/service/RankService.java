package com.llnote.llpvrest.service;

import com.llnote.llpvrest.mapper.RankMapper;
import com.llnote.llpvrest.model.RankVO;
import com.llnote.llpvrest.repository.MariaDBRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class RankService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @NonNull
  private final MariaDBRepository mariaDBRepository;

  public ResponseEntity<String> rank() {
    int responseCode = 200;
    String msg = "ok";

    try {
      HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
          .currentRequestAttributes()).getRequest();
      String ip = req.getHeader("X-FORWARDED-FOR");
      if (ip == null) {
        ip = req.getRemoteAddr();
      }

      JSONArray processRankArr = mariaDBRepository.getRank();



      msg = processRankArr.toString();
    } catch (Exception e) {
      e.printStackTrace();

      responseCode = 500;
      msg = e.getMessage();
    }

    return ResponseEntity.status(responseCode).body(msg);
  }
}
