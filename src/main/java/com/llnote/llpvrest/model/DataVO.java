package com.llnote.llpvrest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(includeFieldNames=true)
public class DataVO {
  private String run_file;
  private String run_title;
  private int run_sec;
  private String _datetime;
  private String stored_time;
  private String client_ip;
  private String _timestamp;
  private String large_category;
  private String medium_category;
}
