package kr.co.kakao.api.ad.domain.dto.response;

import kr.co.kakao.api.ad.domain.entity.Ad;
import lombok.Builder;

@Builder
public record FindAllAdResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        int rewardAmount
) {
    public static FindAllAdResponse toDto(Ad ad) {
        return FindAllAdResponse.builder()
                .id(ad.getId())
                .name(ad.getName())
                .description(ad.getDescription())
                .imageUrl(ad.getImageUrl())
                .rewardAmount(ad.getRewardAmount())
                .build();
    }
}
