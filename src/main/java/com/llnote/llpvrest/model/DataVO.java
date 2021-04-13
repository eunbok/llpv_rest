package com.llnote.llpvrest.model;


public class DataVO {
  private String run_file;
  private String run_title;
  private int run_sec;
  private String _datetime;
  private String stored_time;
  private String client_ip;

  public String getRun_file() {
    return run_file;
  }

  public void setRun_file(String run_file) {
    this.run_file = run_file;
  }

  public String getRun_title() {
    return run_title;
  }

  public void setRun_title(String run_title) {
    this.run_title = run_title;
  }

  public int getRun_sec() {
    return run_sec;
  }

  public void setRun_sec(int run_sec) {
    this.run_sec = run_sec;
  }

  public String get_datetime() {
    return _datetime;
  }

  public void set_datetime(String _datetime) {
    this._datetime = _datetime;
  }

  public String getStored_time() {
    return stored_time;
  }

  public void setStored_time(String stored_time) {
    this.stored_time = stored_time;
  }

  public String getClient_ip() {
    return client_ip;
  }

  public void setClient_ip(String client_ip) {
    this.client_ip = client_ip;
  }

}
