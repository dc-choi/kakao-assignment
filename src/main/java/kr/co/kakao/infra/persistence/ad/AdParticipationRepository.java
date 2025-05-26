package kr.co.kakao.infra.persistence.ad;

import kr.co.kakao.api.ad.domain.entity.AdParticipation;
import kr.co.kakao.api.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AdParticipationRepository extends JpaRepository<AdParticipation, Long> {
    @Query("""
        SELECT ap
        FROM AdParticipation ap
        WHERE ap.member = :member
        AND ap.createdAt BETWEEN :startedAt AND :endedAt
        ORDER BY ap.createdAt ASC
    """)
    Page<AdParticipation> findAllByDateRange(Member member, LocalDateTime startedAt, LocalDateTime endedAt, Pageable pageable);
}
