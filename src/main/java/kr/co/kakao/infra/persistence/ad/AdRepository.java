package kr.co.kakao.infra.persistence.ad;

import kr.co.kakao.api.ad.domain.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {}
