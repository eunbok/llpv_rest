package com.llnote.llpvrest.mapper;


import com.llnote.llpvrest.model.DataVO;
import com.llnote.llpvrest.model.RankVO;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RankMapper {
  @Select("SELECT * FROM `llpv-rank` WHERE rank_date >= DATE_ADD(#{rank_date}, INTERVAL -4 DAY) and rank_date < #{rank_date} and rank_type = 'all' order by rank_date desc")
  List<RankVO> getRank(String rank_date);

  @Select("SELECT run_file, sum(run_sec) as run_sec FROM `llpv-log` WHERE DATE_FORMAT(_datetime, '%Y-%m-%d') = #{rank_date} group by run_file, run_title order by run_sec desc, run_title asc limit 10")
  List<DataVO> getYesterDayRank(String rank_date);

  @Insert("insert into `llpv-rank` values(#{rank_date}, #{rank_result}, #{rank_type})")
  void insertRank(RankVO rankVO);
}
