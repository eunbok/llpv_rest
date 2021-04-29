package com.llnote.llpvrest.repository;

import com.llnote.llpvrest.mapper.CollectMapper;
import com.llnote.llpvrest.mapper.RankMapper;
import com.llnote.llpvrest.model.CateVO;
import com.llnote.llpvrest.model.DataVO;
import com.llnote.llpvrest.model.RankVO;
import com.llnote.llpvrest.util.CmnUt;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MariaDBRepository {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @NonNull
  private final CollectMapper collectMapper;
  @NonNull
  private final RankMapper rankMapper;


  public void save(DataVO dataVO) {
    String large_category = null;
    String medium_category = null;

    CateVO cateVO = collectMapper.findLargeCategory(dataVO.getRun_file());
    if (cateVO != null) {
      large_category = cateVO.getLarge_category();
      medium_category = cateVO.getMedium_category();
    }

    if (large_category == null && medium_category == null) {
      large_category = "미분류";
      medium_category = "미분류";
    } else if (large_category != null && medium_category == null) {
      cateVO = collectMapper.findMediumCategory(large_category, dataVO.getRun_title());
      if (cateVO != null) {
        medium_category = cateVO.getMedium_category();
      } else {
        medium_category = "미분류";
      }
    }

    dataVO.setLarge_category(large_category);
    dataVO.setMedium_category(medium_category);
    collectMapper.insert(dataVO);
  }

  public JSONArray getRank() {
    JSONArray processRankArr = new JSONArray();
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
    Date date = new Date();
    String strDate = sdf.format(date);
    List<RankVO> rankVOList = rankMapper.getRank(strDate);

    for (RankVO rankVO : rankVOList) {
      if (rankVO != null) {
        processRankArr.put(new JSONObject(rankVO.getRank_result()));
      }
    }
    return processRankArr;
  }

  public void saveRank() {
    String rank_type = "all";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfWithE = new SimpleDateFormat("yyyy-MM-dd (E)");

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -1);
    Date date = calendar.getTime();
    String strDate = sdf.format(date);
    String key = sdfWithE.format(date);
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();


    List<DataVO> dataVOList = rankMapper.getYesterDayRank(strDate);
    for (DataVO dataVO : dataVOList) {
      JSONObject temp = new JSONObject();
      temp.put("run_file",dataVO.getRun_file());
      temp.put("run_sec", CmnUt.secToTime(dataVO.getRun_sec()));
      jsonArray.put(temp);
    }
    jsonObject.put(key, jsonArray);

    RankVO rankVO = new RankVO();
    rankVO.setRank_date(strDate);
    rankVO.setRank_result(jsonObject.toString());
    rankVO.setRank_type(rank_type);
    rankMapper.insertRank(rankVO);
  }
}
