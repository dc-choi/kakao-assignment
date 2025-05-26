package kr.co.kakao.api.ad.domain.dto.response;

import kr.co.kakao.api.ad.domain.entity.Ad;
import kr.co.kakao.api.member.domain.enumerated.ParticipationQualifications;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Builder
public record CreateAdResponse(
        Long id,
        String name,
        int rewardAmount,
        int participationCount,
        String description,
        String imageUrl,
        LocalDate startedAt,
        LocalDate endedAt,
        List<ParticipationQualifications> participationQualifications
) {
    public static CreateAdResponse toDto(Ad ad) {
        return CreateAdResponse.builder()
                .id(ad.getId())
                .name(ad.getName())
                .rewardAmount(ad.getRewardAmount())
                .participationCount(ad.getParticipationCount())
                .description(ad.getDescription())
                .imageUrl(ad.getImageUrl())
                .startedAt(ad.getStartedAt())
                .endedAt(ad.getEndedAt())
                .participationQualifications(Arrays.stream(ad.getParticipationQualifications().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(ParticipationQualifications::valueOf)
                        .toList())
                .build();
    }
}
