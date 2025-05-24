package kr.co.kakao.infra.persistence.member;

import kr.co.kakao.api.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {}
