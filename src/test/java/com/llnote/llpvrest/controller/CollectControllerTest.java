package com.llnote.llpvrest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CollectControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 수집테스트() throws Exception{
    String json1 = "[{\"run_title\":\"workspace-eclipse - llpv_agent/src/llproj/llpv/core/SendDataThread.java - Eclipse IDE\",\"_datetime\":\"2021-04-30 22:18:03\",\"run_sec\":2,\"stored_time\":\"2021-05-04 22:18:06\",\"run_file\":\"eclipse.exe\"},{\"run_title\":\"llprocessView\",\"_datetime\":\"2021-04-30 22:18:06\",\"run_sec\":16,\"stored_time\":\"2021-05-05 22:18:22\",\"run_file\":\"javaw.exe\"},{\"run_title\":\"나 일론머스크 이제 테슬라 ceo 아니야!!!! #shorts - YouTube - Chrome\",\"_datetime\":\"2021-04-30 22:18:22\",\"run_sec\":12,\"stored_time\":\"2021-04-30 22:18:34\",\"run_file\":\"chrome.exe\"},{\"run_title\":\"llprocessView\",\"_datetime\":\"2021-05-05 22:18:34\",\"run_sec\":1,\"stored_time\":\"2021-04-30 22:18:35\",\"run_file\":\"javaw.exe\"}]";
    String json2 = "\"run_title\":\"workspace-eclipse - llpv_agent/src/llproj/llpv/core/SendDataThread.java - Eclipse IDE\",\"_datetime\":\"2021-04-30 22:18:03\",\"run_sec\":2,\"stored_time\":\"2021-05-04 22:18:06\",\"run_file\":\"eclipse.exe\"},{\"run_title\":\"llprocessView\",\"_datetime\":\"2021-04-30 22:18:06\",\"run_sec\":16,\"stored_time\":\"2021-05-05 22:18:22\",\"run_file\":\"javaw.exe\"},{\"run_title\":\"나 일론머스크 이제 테슬라 ceo 아니야!!!! #shorts - YouTube - Chrome\",\"_datetime\":\"2021-04-30 22:18:22\",\"run_sec\":12,\"stored_time\":\"2021-04-30 22:18:34\",\"run_file\":\"chrome.exe\"},{\"run_title\":\"llprocessView\",\"_datetime\":\"2021-05-05 22:18:34\",\"run_sec\":1,\"stored_time\":\"2021-04-30 22:18:35\",\"run_file\":\"javaw.exe\"}";
    mockMvc.perform(post("/rest")
        .content(json1))
        .andExpect(status().isOk());

    mockMvc.perform(post("/rest")
        .content(json2))
        .andExpect(status().isBadRequest());
  }
}