package kr.co.kakao.api.ad.application;

import kr.co.kakao.api.ad.domain.dto.response.FindAllParticipationResponse;
import kr.co.kakao.api.ad.domain.entity.Ad;
import kr.co.kakao.api.ad.domain.entity.AdParticipation;
import kr.co.kakao.api.member.domain.entity.Member;
import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.exception.BusinessException;
import kr.co.kakao.infra.persistence.ad.AdParticipationRepository;
import kr.co.kakao.infra.persistence.ad.AdRepository;
import kr.co.kakao.infra.persistence.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

        List<AdParticipation> mockList = Arrays.asList(adParticipation1, adParticipation2);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
        when(adParticipationRepository.findAllByDateRange(
                any(Member.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(mockList);

        // when
        List<FindAllParticipationResponse> responses = adParticipationService.findAllParticipation(memberId, pageable, startedAt, endedAt);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).adId()).isEqualTo(101L);
        assertThat(responses.get(1).adId()).isEqualTo(102L);
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
        )).thenReturn(Collections.emptyList());

        // when
        List<FindAllParticipationResponse> responses = adParticipationService.findAllParticipation(memberId, pageable, startedAt, endedAt);

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