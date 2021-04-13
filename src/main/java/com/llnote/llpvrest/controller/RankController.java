package com.llnote.llpvrest.controller;

import com.llnote.llpvrest.repository.ElasticRepository;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class RankController {

  private final ElasticRepository elasticRepository;

  public RankController(ElasticRepository elasticRepository) {
    this.elasticRepository = elasticRepository;
  }

  @CrossOrigin("*")
  @RequestMapping(value = "/rank", method = RequestMethod.POST)
  @ResponseBody
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
      JSONArray processRankJo = elasticRepository.getRank();
      msg = processRankJo.toString();
    } catch (Exception e) {
      e.printStackTrace();

      responseCode = 500;
      msg = e.getMessage();
    }

    return ResponseEntity.status(responseCode).body(msg);
  }
}
