package kr.co.kakao.api.ad.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AdTest {
    @Test
    @DisplayName("광고 참여 횟수 감소 테스트")
    void decrementParticipationCount() {
        // given
        Ad ad = Ad.builder()
                .participationCount(1)
                .build();

        // when
        ad.decrementParticipationCount();

        // then
        assertThat(ad.getParticipationCount()).isEqualTo(0);
    }

}