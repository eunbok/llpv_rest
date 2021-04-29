package com.llnote.llpvrest.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@NonNull
@Setter
@Getter
@ToString(includeFieldNames=true)
public class CateVO {
  private String run_file;
  private String run_title;
  private String _datetime;
  private String large_category;
  private String medium_category;
}
