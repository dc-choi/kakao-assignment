package kr.co.kakao.infra.persistence.ad;

import kr.co.kakao.api.ad.domain.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    Ad findByName(String name);

    @Query("""
        SELECT a FROM Ad a
        WHERE a.participationCount >= 1
        AND a.startedAt <= :today
        AND a.endedAt >= :today
        ORDER BY a.rewardAmount DESC
    """)
    List<Ad> findTop10ValidAds(LocalDate today);
}
