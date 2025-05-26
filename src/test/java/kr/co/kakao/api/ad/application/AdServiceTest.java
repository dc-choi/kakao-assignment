package kr.co.kakao.api.ad.application;

import kr.co.kakao.api.ad.domain.dto.request.CreateAdRequest;
import kr.co.kakao.api.ad.domain.dto.response.CreateAdResponse;
import kr.co.kakao.api.ad.domain.entity.Ad;
import kr.co.kakao.api.member.domain.enumerated.ParticipationQualifications;
import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.exception.BusinessException;
import kr.co.kakao.infra.persistence.ad.AdRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {
    @InjectMocks
    private AdService adService;

    @Mock
    private AdRepository adRepository;

    @Test
    @DisplayName("광고 생성 성공")
    void createAd() {
        // given
        CreateAdRequest request = CreateAdRequest.builder()
                .name("광고1")
                .rewardAmount(1000)
                .participationCount(1)
                .description("내용")
                .imageUrl("http://example.com/image.jpg")
                .startedAt(LocalDate.now())
                .endedAt(LocalDate.now().plusDays(7))
                .participationQualifications(List.of(ParticipationQualifications.FIRST_TIME))
                .build();

        Ad adEntity = CreateAdRequest.toEntity(request);

        Ad savedEntity = Ad.builder()
                .id(1L)
                .name("광고1")
                .rewardAmount(1000)
                .participationCount(1)
                .description("내용")
                .imageUrl("http://example.com/image.jpg")
                .startedAt(LocalDate.now())
                .endedAt(LocalDate.now().plusDays(7))
                .participationQualifications(ParticipationQualifications.FIRST_TIME.getDescription())
                .build();

        CreateAdResponse expectedResponse = CreateAdResponse.toDto(savedEntity);

        // mock static: CreateAdRequest.toEntity() 와 CreateAdResponse.toDto()
        try (MockedStatic<CreateAdRequest> mockedRequest = mockStatic(CreateAdRequest.class);
             MockedStatic<CreateAdResponse> mockedResponse = mockStatic(CreateAdResponse.class)) {

            mockedRequest.when(() -> CreateAdRequest.toEntity(request)).thenReturn(adEntity);
            mockedResponse.when(() -> CreateAdResponse.toDto(savedEntity)).thenReturn(expectedResponse);

            when(adRepository.findByName("광고1")).thenReturn(null);
            when(adRepository.save(adEntity)).thenReturn(savedEntity);

            // when
            CreateAdResponse actual = adService.createAd(request);

            // then
            assertThat(actual).isEqualTo(expectedResponse);
        }
    }

    @Test
    @DisplayName("광고 생성 실패 - 중복된 광고 이름")
    void duplicatedAd() {
        // given
        CreateAdRequest request = CreateAdRequest.builder()
                .name("광고1")
                .rewardAmount(1000)
                .participationCount(1)
                .description("내용")
                .imageUrl("http://example.com/image.jpg")
                .startedAt(LocalDate.now())
                .endedAt(LocalDate.now().plusDays(7))
                .participationQualifications(List.of(ParticipationQualifications.FIRST_TIME))
                .build();

        Ad duplicatedAd = CreateAdRequest.toEntity(request);

        when(adRepository.findByName("광고1")).thenReturn(duplicatedAd);

        // when & then
        assertThatThrownBy(() -> adService.createAd(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(FailHttpMessage.CONFLICT.getMessage());
    }
}