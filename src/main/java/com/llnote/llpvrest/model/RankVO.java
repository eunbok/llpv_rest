package com.llnote.llpvrest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(includeFieldNames=true)
public class RankVO {
  private String rank_date;
  private String rank_result;
  private String rank_type;
}
