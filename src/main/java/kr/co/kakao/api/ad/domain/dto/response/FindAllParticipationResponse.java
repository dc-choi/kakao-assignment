package kr.co.kakao.api.ad.domain.dto.response;

import kr.co.kakao.api.ad.domain.entity.AdParticipation;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindAllParticipationResponse(
        LocalDateTime cratedAt,
        Long memberId,
        Long adId,
        String adName,
        int rewardAmount
) {
    public static FindAllParticipationResponse toDto(AdParticipation adParticipation) {
        return FindAllParticipationResponse.builder()
                .cratedAt(adParticipation.getCreatedAt())
                .memberId(adParticipation.getMember().getId())
                .adId(adParticipation.getAd().getId())
                .adName(adParticipation.getName())
                .rewardAmount(adParticipation.getRewardAmount())
                .build();
    }
}
