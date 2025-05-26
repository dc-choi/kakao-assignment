package kr.co.kakao.api.ad.application;

import kr.co.kakao.api.ad.domain.dto.request.CreateParticipationRequest;
import kr.co.kakao.api.ad.domain.dto.response.CreateParticipationResponse;
import kr.co.kakao.api.ad.domain.dto.response.FindAllParticipationResponse;
import kr.co.kakao.api.ad.domain.entity.Ad;
import kr.co.kakao.api.ad.domain.entity.AdParticipation;
import kr.co.kakao.api.member.domain.entity.Member;
import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.exception.BusinessException;
import kr.co.kakao.infra.persistence.ad.AdParticipationRepository;
import kr.co.kakao.infra.persistence.ad.AdRepository;
import kr.co.kakao.infra.persistence.member.MemberRepository;
import kr.co.kakao.infra.point.ExternalPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdParticipationServiceTest {
    @InjectMocks
    private AdParticipationService adParticipationService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private AdParticipationRepository adParticipationRepository;

    @Mock
    private ExternalPointService externalPointService;

    private CreateParticipationRequest createParticipationRequest;
    private Member mockMember;
    private Ad mockAd;

    @BeforeEach
    void setUp() {
        Long memberId = 1L;
        Long adId = 101L;
        int rewardAmount = 1000;

        createParticipationRequest = new CreateParticipationRequest(memberId, adId);

        mockMember = Member.builder()
                .id(memberId)
                .build();

        mockAd = Ad.builder()
                .id(adId)
                .name("Test Ad")
                .rewardAmount(rewardAmount)
                .participationCount(5)
                .build();
    }

    @DisplayName("광고 참여 성공")
    @Test
    void participation_Success() {
        // given
        when(memberRepository.findById(createParticipationRequest.memberId()))
                .thenReturn(Optional.of(mockMember));
        when(adRepository.findById(createParticipationRequest.adId()))
                .thenReturn(Optional.of(mockAd));
        // externalPointService.earnPoints는 void 메서드이므로 doNothing()을 사용
        doNothing().when(externalPointService).earnPoints(anyLong(), anyInt());
        when(adParticipationRepository.save(any(AdParticipation.class)))
                .thenReturn(AdParticipation.builder().id(1L).build()); // save는 저장된 객체를 반환한다고 가정

        // when
        CreateParticipationResponse response = adParticipationService.participation(createParticipationRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.adId()).isEqualTo(mockAd.getId());
        assertThat(response.memberId()).isEqualTo(mockMember.getId());

        verify(memberRepository, times(1)).findById(createParticipationRequest.memberId());
        verify(adRepository, times(1)).findById(createParticipationRequest.adId());
        verify(externalPointService, times(1)).earnPoints(mockMember.getId(), mockAd.getRewardAmount());
    }

    @DisplayName("광고 참여 실패 - 회원을 찾을 수 없음")
    @Test
    void participation_Fail_MemberNotFound() {
        // given
        when(memberRepository.findById(createParticipationRequest.memberId()))
                .thenReturn(Optional.empty()); // 멤버를 찾을 수 없도록 설정

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adParticipationService.participation(createParticipationRequest));

        assertThat(exception.getMessage()).isEqualTo(FailHttpMessage.NOT_FOUND_MEMBER.getMessage());

        verify(adRepository, times(0)).findById(any());
        verify(externalPointService, times(0)).earnPoints(anyLong(), anyInt());
        verify(adParticipationRepository, times(0)).save(any());
    }

    @DisplayName("광고 참여 실패 - 광고를 찾을 수 없음")
    @Test
    void participation_Fail_AdNotFound() {
        // given
        when(memberRepository.findById(createParticipationRequest.memberId()))
                .thenReturn(Optional.of(mockMember));
        when(adRepository.findById(createParticipationRequest.adId()))
                .thenReturn(Optional.empty()); // 광고를 찾을 수 없도록 설정

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adParticipationService.participation(createParticipationRequest));

        assertThat(exception.getMessage()).isEqualTo(FailHttpMessage.NOT_FOUND_AD.getMessage());

        verify(externalPointService, times(0)).earnPoints(anyLong(), anyInt());
        verify(adParticipationRepository, times(0)).save(any());
    }

    @DisplayName("광고 참여 실패 - 광고 참여 횟수가 부족함 (0 이하)")
    @Test
    void participation_Fail_NotEnoughParticipationCount() {
        // given
        mockAd = Ad.builder()
                .id(createParticipationRequest.adId())
                .name("Test Ad")
                .rewardAmount(1000)
                .participationCount(0) // 0으로 설정하여 감소 시 예외 발생하도록
                .build();

        when(memberRepository.findById(createParticipationRequest.memberId()))
                .thenReturn(Optional.of(mockMember));
        when(adRepository.findById(createParticipationRequest.adId()))
                .thenReturn(Optional.of(mockAd));
        doNothing().when(externalPointService).earnPoints(anyLong(), anyInt());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adParticipationService.participation(createParticipationRequest));

        assertThat(exception.getMessage()).isEqualTo(FailHttpMessage.NOT_ENOUGH_PARTICIPATION_COUNT.getMessage());

        verify(memberRepository, times(1)).findById(createParticipationRequest.memberId());
        verify(adRepository, times(1)).findById(createParticipationRequest.adId());
        verify(externalPointService, times(1)).earnPoints(mockMember.getId(), mockAd.getRewardAmount());
    }

    @Test
    @DisplayName("회원 참여 내역 조회 성공 - 참여 내역이 있는 경우")
    void findAllParticipation() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate startedAt = LocalDate.of(2023, 1, 1);
        LocalDate endedAt = LocalDate.of(2023, 1, 31);

        Member mockMember = Member.builder().id(memberId).build();

        Ad mockAd = Ad.builder().id(101L).build();
        Ad mockAd2 = Ad.builder().id(102L).build();

        AdParticipation adParticipation1 = AdParticipation.builder()
                .id(1L)
                .member(mockMember)
                .ad(mockAd)
                .createdAt(LocalDateTime.of(2023, 1, 15, 10, 0))
                .build();
        AdParticipation adParticipation2 = AdParticipation.builder()
                .id(2L)
                .member(mockMember)
                .ad(mockAd2)
                .createdAt(LocalDateTime.of(2023, 1, 20, 14, 30))
                .build();

        Page<AdParticipation> mockList = new PageImpl<>(Arrays.asList(adParticipation1, adParticipation2), pageable, 2);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
        when(adParticipationRepository.findAllByDateRange(
                any(Member.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(mockList);

        // when
        Page<FindAllParticipationResponse> responses = adParticipationService.findAllParticipation(memberId, pageable, startedAt, endedAt);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.getContent().get(0).adId()).isEqualTo(101L);
        assertThat(responses.getContent().get(1).adId()).isEqualTo(102L);
    }

    @DisplayName("회원 참여 내역 조회 성공 - 참여 내역이 없는 경우")
    @Test
    void findAllParticipationNoResult() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate startedAt = LocalDate.of(2023, 1, 1);
        LocalDate endedAt = LocalDate.of(2023, 1, 31);

        Member mockMember = Member.builder().id(memberId).build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
        when(adParticipationRepository.findAllByDateRange(
                any(Member.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // when
        Page<FindAllParticipationResponse> responses = adParticipationService.findAllParticipation(memberId, pageable, startedAt, endedAt);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses).isEmpty();
    }

    @DisplayName("회원 참여 내역 조회 실패 - 시작일이 종료일보다 늦은 경우")
    @Test
    void findAllParticipation_Fail_StartedAtAfterEndedAt() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate startedAt = LocalDate.of(2023, 1, 31);
        LocalDate endedAt = LocalDate.of(2023, 1, 1); // 시작일이 종료일보다 늦음

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adParticipationService.findAllParticipation(memberId, pageable, startedAt, endedAt));

        assertThat(exception.getMessage()).isEqualTo(FailHttpMessage.STARTED_AT_IS_AFTER_ENDED_AT.getMessage());
    }

    @DisplayName("회원 참여 내역 조회 실패 - 회원을 찾을 수 없는 경우")
    @Test
    void findAllParticipation_Fail_MemberNotFound() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate startedAt = LocalDate.of(2023, 1, 1);
        LocalDate endedAt = LocalDate.of(2023, 1, 31);

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adParticipationService.findAllParticipation(memberId, pageable, startedAt, endedAt));

        assertThat(exception.getMessage()).isEqualTo(FailHttpMessage.NOT_FOUND_MEMBER.getMessage());
    }
}