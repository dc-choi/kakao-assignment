package kr.co.kakao.api.ad.domain.dto.request;

public record CreateParticipationRequest(
        Long adId,
        Long memberId
) {}
