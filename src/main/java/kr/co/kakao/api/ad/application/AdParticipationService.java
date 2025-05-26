package kr.co.kakao.api.ad.application;

import kr.co.kakao.api.ad.domain.dto.request.CreateParticipationRequest;
import kr.co.kakao.api.ad.domain.dto.response.CreateParticipationResponse;
import kr.co.kakao.api.ad.domain.dto.response.FindAllParticipationResponse;
import kr.co.kakao.api.ad.domain.entity.Ad;
import kr.co.kakao.api.ad.domain.entity.AdParticipation;
import kr.co.kakao.api.member.domain.entity.Member;
import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.exception.BusinessException;
import kr.co.kakao.global.lock.DistributedLock;
import kr.co.kakao.infra.persistence.ad.AdParticipationRepository;
import kr.co.kakao.infra.persistence.ad.AdRepository;
import kr.co.kakao.infra.persistence.member.MemberRepository;
import kr.co.kakao.infra.point.ExternalPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdParticipationService {
    private final MemberRepository memberRepository;
    private final AdRepository adRepository;
    private final AdParticipationRepository adParticipationRepository;
    private final ExternalPointService externalPointService;

    @DistributedLock(key = "#createParticipationRequest.adId")
    @Transactional
    public CreateParticipationResponse participation(CreateParticipationRequest createParticipationRequest) {
        Member member = memberRepository.findById(createParticipationRequest.memberId())
                .orElseThrow(() -> new BusinessException(FailHttpMessage.NOT_FOUND_MEMBER));

        Ad ad = adRepository.findById(createParticipationRequest.adId())
                .orElseThrow(() -> new BusinessException(FailHttpMessage.NOT_FOUND_AD));

        // INFO: 포인트를 적립하는 외부 서비스 호출
        externalPointService.earnPoints(member.getId(), ad.getRewardAmount());
        // INFO: 광고 참여 횟수를 감소시키고, 참여 횟수가 0 이하가 되면 예외 발생
        ad.decrementParticipationCount();

        AdParticipation adParticipation = AdParticipation.builder()
                .name(ad.getName())
                .rewardAmount(ad.getRewardAmount())
                .member(member)
                .ad(ad)
                .build();
        adParticipationRepository.save(adParticipation);

        return new CreateParticipationResponse(ad.getId(), member.getId());
    }

    public Page<FindAllParticipationResponse> findAllParticipation(Long memberId, Pageable pageable, LocalDate startedAt, LocalDate endedAt) {
        if (startedAt.isAfter(endedAt)) {
            throw new BusinessException(FailHttpMessage.STARTED_AT_IS_AFTER_ENDED_AT);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(FailHttpMessage.NOT_FOUND_MEMBER));

        Page<AdParticipation> allByDateRange = adParticipationRepository.findAllByDateRange(member, startedAt.atStartOfDay(), endedAt.atTime(23, 59, 59), pageable);
        long count = adParticipationRepository.count();

        return new PageImpl<>(
                allByDateRange.stream()
                .map(FindAllParticipationResponse::toDto)
                .toList(),
                pageable,
                count
        );
    }
}
