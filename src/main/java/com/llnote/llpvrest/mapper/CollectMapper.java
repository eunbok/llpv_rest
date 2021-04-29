package com.llnote.llpvrest.mapper;

import com.llnote.llpvrest.model.CateVO;
import com.llnote.llpvrest.model.DataVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface CollectMapper {

  @Select("SELECT large_category, medium_category FROM `llpv-cate` WHERE run_file = #{run_file}")
  CateVO findLargeCategory(String run_file);

  @Select("SELECT large_category, medium_category FROM `llpv-cate` WHERE large_category = #{large_category} and INSTR(#{run_title},run_title)>0")
  CateVO findMediumCategory(String large_category, String run_title);

  @Insert("insert into `llpv-log`(run_file, run_title, run_sec, _datetime, stored_time, client_ip, large_category, medium_category) values(#{run_file}, #{run_title}, #{run_sec}, #{_datetime}, #{stored_time}, #{client_ip}, #{large_category}, #{medium_category})")
  void insert(DataVO dataVO);
}
