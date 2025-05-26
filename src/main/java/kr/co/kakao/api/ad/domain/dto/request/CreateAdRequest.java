package kr.co.kakao.api.ad.domain.dto.request;

import kr.co.kakao.api.ad.domain.entity.Ad;
import kr.co.kakao.api.member.domain.enumerated.ParticipationQualifications;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
public record CreateAdRequest(
    String name,
    int rewardAmount,
    int participationCount,
    String description,
    String imageUrl,
    LocalDate startedAt,
    LocalDate endedAt,
    List<ParticipationQualifications> participationQualifications
) {
    public static Ad toEntity(CreateAdRequest createAdRequest) {
        return Ad.builder()
                .name(createAdRequest.name())
                .rewardAmount(createAdRequest.rewardAmount())
                .participationCount(createAdRequest.participationCount())
                .description(createAdRequest.description())
                .imageUrl(createAdRequest.imageUrl())
                .startedAt(createAdRequest.startedAt())
                .endedAt(createAdRequest.endedAt())
                .participationQualifications(createAdRequest.participationQualifications()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(ParticipationQualifications::getDescription)
                        .collect(Collectors.joining(",")))
                .build();
    }
}
