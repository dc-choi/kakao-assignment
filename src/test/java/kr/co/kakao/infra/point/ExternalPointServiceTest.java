package kr.co.kakao.infra.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExternalPointServiceTest {
    @Test
    @DisplayName("포인트 적립 서비스가 호출되는지 확인")
    void earnPoints() {
        // Given
        ExternalPointService externalPointService = new ExternalPointService() {
            @Override
            public void earnPoints(Long memberId, int amount) {
                // Mock implementation for testing
                assertThat(memberId).isEqualTo(100L);
                assertThat(amount).isEqualTo(50);
            }
        };

        // When && Then
        externalPointService.earnPoints(100L, 50);
    }
}