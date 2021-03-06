package com.llnote.llpvrest.controller;

import com.llnote.llpvrest.service.RankService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RankController {

  @NonNull
  private final RankService rankService;

  @CrossOrigin("*")
  @RequestMapping(value = "/rank", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> rank() {
    return rankService.rank();
  }
}
