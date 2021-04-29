package com.llnote.llpvrest.controller;

import com.llnote.llpvrest.service.CollectService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CollectController {
  @NonNull
  private final CollectService collectService;

  @RequestMapping(value = "/rest", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> rest(@RequestBody String data) {
    return collectService.save(data);
  }
}
