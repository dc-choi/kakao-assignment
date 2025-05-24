package kr.co.kakao.infra.persistence.ad;

import kr.co.kakao.api.ad.domain.entity.AdParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdParticipationRepository extends JpaRepository<AdParticipation, Long> {}
