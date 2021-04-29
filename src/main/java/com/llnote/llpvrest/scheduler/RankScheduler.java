package com.llnote.llpvrest.scheduler;

import com.llnote.llpvrest.repository.MariaDBRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RankScheduler {

  @NonNull
  private final MariaDBRepository mariaDBRepository;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Scheduled(cron = "0 5 0 * * *")
  public void makeRankJob() {
    logger.info("start makeRankJob");
    mariaDBRepository.saveRank();
    logger.info("end makeRankJob");
  }
}
